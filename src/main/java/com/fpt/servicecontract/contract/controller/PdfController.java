package com.fpt.servicecontract.contract.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.servicecontract.utils.PdfUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PdfController {
    private final PdfUtils pdfUtils;

    private final Cloudinary cloudinary;

    @GetMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void generatePdf(HttpServletResponse response, @RequestParam String title, @RequestParam String content) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("content", content);
        String html = pdfUtils.templateEngine().process("templates/test.html", context);
        File file = pdfUtils.generatePdf(html, "file_hihi");
        // push to cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        while ((len = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        fis.close();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"generated-pdf.pdf\"");

        // Write PDF to response
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }
}
