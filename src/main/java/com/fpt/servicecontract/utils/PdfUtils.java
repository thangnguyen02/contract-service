package com.fpt.servicecontract.utils;

import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.lowagie.text.html.HtmlTags.HTML;
import static org.apache.commons.codec.CharEncoding.UTF_8;

@Slf4j
@RequiredArgsConstructor
@Component
public class PdfUtils {
    public File generatePdf(String htmlContent, String fileName) throws Exception {
        File pdfFile = new File(fileName);
        try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
            ITextRenderer renderer = new ITextRenderer();
            FontFactory.registerDirectory("/fonts");
            renderer.getFontResolver()
                    .addFont(new ClassPathResource("fonts/DejaVuSans.ttf").getPath(),
                            "DejaVuSans", BaseFont.IDENTITY_H, true, null);
            htmlContent = replaceSpecialCharacters(htmlContent);
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return pdfFile;
        } catch (Exception e) {
            log.error("filename {}, html Content {}", fileName, htmlContent, e);
            throw new Exception(e);
        }
    }

    public TemplateEngine templateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding(UTF_8);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    public String replaceSpecialCharacters(String html) {
        return html
                .replace("ᵒ", "&ordm;")
                .replace("& ", "")
                .replace("<br>", "<br/>");
    }
}
