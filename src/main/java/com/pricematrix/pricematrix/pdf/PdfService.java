package com.pricematrix.pricematrix.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class PdfService {

    public byte[] generatePdf(String html) throws DocumentException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ITextRenderer renderer = new ITextRenderer();

            // 嵌入日文字型
            URL fontUrl = getClass().getClassLoader().getResource("fonts/NotoSansJP-Regular.ttf");
            System.out.println("字型路徑: " + fontUrl);
            if (fontUrl != null) {
                renderer.getFontResolver().addFont(
                        fontUrl.toString(),
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED
                );
            }

            renderer.setDocumentFromString(html, "classpath:/");
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new DocumentException(e);
        }
    }
}