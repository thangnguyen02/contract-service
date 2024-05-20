package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.contract.service.OcrService;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final OcrService ocrService;
    @Override
    public String scanContract(List<MultipartFile> images) throws  IOException, TesseractException {
        AtomicReference<StringBuilder> result = new AtomicReference<>(new StringBuilder());
        for (MultipartFile file : images) {
            String text = String.valueOf(ocrService.ocr(file));
            result.get().append(text);
        }
        return result.get().toString();
    }
}
