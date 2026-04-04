package com.pricematrix.pricematrix.pdf;

import com.lowagie.text.DocumentException;
import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import com.pricematrix.pricematrix.sales.service.SalesQuoteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URL;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;
    private final SalesQuoteService salesQuoteService;
    private final CompanyProperties companyProperties;

    public PdfController(PdfService pdfService, SalesQuoteService salesQuoteService, CompanyProperties companyProperties) {
        this.pdfService = pdfService;
        this.salesQuoteService = salesQuoteService;
        this.companyProperties = companyProperties;
    }

    @GetMapping("/quote/{id}")
    public ResponseEntity<byte[]> generateQuotePdf(@PathVariable Long id) throws DocumentException {
        SalesQuote quote = salesQuoteService.getQuoteById(id);
        String html = buildQuoteHtml(quote);

        byte[] pdf = pdfService.generatePdf(html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "quote-%d.pdf".formatted(quote.getId()));

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    private String buildQuoteHtml(SalesQuote quote) {
        // 取得字型路徑
        URL fontUrl = getClass().getClassLoader().getResource("fonts/NotoSansJP-Regular.ttf");
        String fontPath = fontUrl != null ? fontUrl.toString() : "";

        StringBuilder rows = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;

        for (var item : quote.getItems()) {
            BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);
            rows.append("""
                    <tr>
                        <td>%s</td>
                        <td style="text-align:right">%d</td>
                        <td style="text-align:right">¥%,d</td>
                        <td style="text-align:right">¥%,d</td>
                    </tr>
                    """.formatted(
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice().intValue(),
                    subtotal.intValue()
            ));
        }

        BigDecimal tax = total.multiply(new BigDecimal("0.10")).setScale(0, java.math.RoundingMode.DOWN);
        BigDecimal grandTotal = total.add(tax);

        return """
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="UTF-8"/>
                <style>
                    @font-face {
                        font-family: 'NotoSansJP';
                        src: url('%s');
                        -fs-pdf-font-embed: embed;
                        -fs-pdf-font-encoding: Identity-H;
                    }
                    body { font-family: 'NotoSansJP', sans-serif; font-size: 12px; margin: 40px; }
                    h1 { text-align: center; font-size: 20px; border-bottom: 2px solid black; padding-bottom: 8px; }
                    .header-table { width: 100%%; margin-bottom: 20px; }
                    .company-info { text-align: right; }
                    .customer-info { margin-bottom: 20px; }
                    table { width: 100%%; border-collapse: collapse; margin-top: 16px; }
                    th { background-color: #333; color: white; padding: 6px; }
                    td { border: 1px solid #ccc; padding: 6px; }
                    .total-row td { font-weight: bold; }
                    .note { margin-top: 20px; font-size: 11px; }
                </style>
                </head>
                <body>
                    <h1>御 見 積 書</h1>
                    <table class="header-table">
                        <tr>
                            <td class="customer-info">
                                <strong>%s 御中</strong><br/>
                                作成日：%s
                            </td>
                            <td class="company-info">
                                %s<br/>
                                %s<br/>
                                TEL: %s<br/>
                                %s
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <th>商品名</th><th>数量</th><th>単価</th><th>小計</th>
                        </tr>
                        %s
                        <tr class="total-row">
                            <td colspan="3" style="text-align:right">小計</td>
                            <td style="text-align:right">¥%,d</td>
                        </tr>
                        <tr>
                            <td colspan="3" style="text-align:right">消費税 (10%%)</td>
                            <td style="text-align:right">¥%,d</td>
                        </tr>
                        <tr class="total-row">
                            <td colspan="3" style="text-align:right">合計</td>
                            <td style="text-align:right">¥%,d</td>
                        </tr>
                    </table>
                    <div class="note">備考：%s</div>
                </body>
                </html>
                """.formatted(
                fontPath,
                quote.getCustomer().getName(),
                quote.getCreatedAt().toLocalDate(),
                companyProperties.getName(),
                companyProperties.getAddress(),
                companyProperties.getPhone(),
                companyProperties.getEmail(),
                rows.toString(),
                total.intValue(),
                tax.intValue(),
                grandTotal.intValue(),
                quote.getNote() != null ? quote.getNote() : ""
        );
    }
}