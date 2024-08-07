package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.request.ContractRequest;
import com.fpt.servicecontract.contract.dto.request.PartyRequest;
import com.fpt.servicecontract.contract.dto.response.ContractAppendicesResponse;
import com.fpt.servicecontract.contract.dto.response.ContractResponse;
import com.fpt.servicecontract.contract.enums.SignContractStatus;
import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.contract.model.ContractAppendices;
import com.fpt.servicecontract.contract.model.ContractStatus;
import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.ContractAppendicesRepository;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.repository.ContractStatusRepository;
import com.fpt.servicecontract.contract.service.*;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.PdfUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractAppendicesServiceImpl implements ContractAppendicesService {

    private final ContractAppendicesRepository contractAppendicesRepository;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final ContractStatusService contractStatusService;
    private final ContractHistoryService contractHistoryService;
    private final ContractStatusRepository contractStatusRepository;
    private final ContractRepository contractRepository;
    private final PdfUtils pdfUtils;
    private final CloudinaryService cloudinaryService;
    private final ContractService contractService;


    public BaseResponse getAll(Pageable p, String email, String statusSearch, String contractId) {
        List<String> statusListSearch = getListStatusSearch(statusSearch);
        Page<Object[]> page = contractAppendicesRepository.findAllContractAppendices(p, statusListSearch, contractId);
        List<ContractAppendicesResponse> responses = new ArrayList<>();
        for (Object[] obj : page) {
            ContractAppendicesResponse response = ContractAppendicesResponse.builder()
                    .name(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .createdBy(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .file(Objects.nonNull(obj[2]) ? obj[2].toString() : null)
                    .createdDate(Objects.nonNull(obj[3]) ? obj[3].toString() : null)
                    .id(Objects.nonNull(obj[4]) ? obj[4].toString() : null)
                    .status(Objects.nonNull(obj[5]) ? obj[5].toString() : null)
                    .approvedBy(Objects.nonNull(obj[6]) ? obj[6].toString() : null)
                    .statusCurrent(Objects.nonNull(obj[7]) ? obj[7].toString() : null)
                    .canSend(true)
                    .canApprove(false)
                    .canSign(true)
                    .value(Objects.nonNull(obj[9]) ? (Double) obj[9] : null)
                    .canRejectSign(false)
                    .contractId(Objects.nonNull(obj[11]) ? obj[11].toString() : null)
                    .build();
            String status = response.getStatusCurrent();
//            List<String> statusList = contractStatusService.checkDoneSign(response.getId());
            if (SignContractStatus.APPROVED.name().equals(status)) {
                response.setCanSendForMng(true);
                response.setCanSend(false);
            }


            // man hinh sale send contract cho office-admin
            if (SignContractStatus.WAIT_APPROVE.name().equals(status)) {
                response.setCanSend(false);
                response.setCanApprove(true);
                response.setCanSign(false);
                response.setCanSendForCustomer(false);
                response.setCanUpdate(true);
                response.setCanDelete(true);
            }

            //officer-admin reject
            if (SignContractStatus.APPROVE_FAIL.name().equals(status)) {
                response.setCanSend(true);
                response.setCanApprove(false);
                response.setCanSign(false);
                response.setCanSendForCustomer(false);
                ContractStatus contractStatus = contractStatusRepository.findByContractLastStatusObject(response.getId());
                response.setRejectedBy(contractStatus.getSender());
                response.setCanUpdate(true);
                response.setCanDelete(true);
            }

            //send office_admin
            if (SignContractStatus.NEW.name().equals(status)) {
                response.setCanResend(false);
                response.setCanUpdate(true);
                response.setCanDelete(true);
                response.setCanApprove(true);
                response.setCanSign(false);
                response.setCanSendForCustomer(false);
            }


            if (SignContractStatus.SIGN_B_FAIL.name().equals(status)
                    || SignContractStatus.SIGN_A_FAIL.name().equals(status)) {
                response.setCanUpdate(true);
                response.setCanDelete(true);
                response.setCanSend(true);
                response.setCanSign(false);
            }

            if (SignContractStatus.SIGN_A_FAIL.name().equals(status)) {
                response.setCanSendForCustomer(false);
            }

            if (SignContractStatus.WAIT_SIGN_B.name().equals(status)
                    || SignContractStatus.WAIT_SIGN_A.name().equals(status)) {
                response.setCanUpdate(false);
                response.setCanDelete(false);
                response.setCanSend(false);
                response.setCanSendForCustomer(false);
                response.setCanSendForMng(false);
                response.setCanSign(true);
                response.setCanRejectSign(true);
            }


            List<String> statusDb = contractStatusService.checkDoneSign(response.getId());

            if (status.equals(SignContractStatus.SIGN_A_OK.name())
            ) {
                response.setCanSend(false);
                response.setCanUpdate(false);
                response.setCanDelete(false);
                if (!statusDb.contains(SignContractStatus.SUCCESS.name())) {
                    response.setCanSendForCustomer(true);
                    response.setCanSendForMng(false);
                }
            }

            if (status.equals(SignContractStatus.SIGN_B_OK.name())
            ) {
                response.setCanSend(false);
                response.setCanUpdate(false);
                response.setCanDelete(false);
                if (!statusDb.contains(SignContractStatus.SUCCESS.name())) {
                    response.setCanSendForMng(true);
                    response.setCanSendForCustomer(false);
                }
            }

            responses.add(response);
        }
        Page<ContractAppendicesResponse> result = new PageImpl<>(responses, p,
                page.getTotalElements());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, result);
    }

    public BaseResponse getById(String id) {
        Optional<ContractAppendices> contractAppendices = contractAppendicesRepository.findById(id);
        return contractAppendices.map(appendices -> new BaseResponse(Constants.ResponseCode.SUCCESS, "Found", true, appendices)).orElseGet(() -> new BaseResponse(Constants.ResponseCode.NOT_FOUND, "Not found", true, null));
    }

    public BaseResponse save(ContractAppendices contractAppendices, String email)  {
        ContractAppendices appendices = ContractAppendices
                .builder()
                .id(contractAppendices.getId())
                .contractId(contractAppendices.getContractId())
                .name(contractAppendices.getName())
                .number(contractAppendices.getNumber())
                .rule(contractAppendices.getRule())
                .term(contractAppendices.getTerm())
                .createdBy(email)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .status(Constants.STATUS.NEW)
                .build();

//        get contract to get party
        ContractRequest contractRequest = contractService.findById(contractAppendices.getContractId());
        Context context = new Context();
        context.setVariable("partyA", contractRequest.getPartyA());
        context.setVariable("partyB", contractRequest.getPartyB());
        context.setVariable("info", appendices);
        context.setVariable("date", LocalDateTime.now().toLocalDate());

        String html = pdfUtils.templateEngine().process("templates/new_contract.html", context);
        File file = null;

        try {
            file = pdfUtils.generatePdf(html, contractAppendices.getName() + "_" + UUID.randomUUID());
            contractAppendices.setFile(cloudinaryService.uploadPdf(file));
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Can not generate pdf file", true, null);
        }
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.warn("Failed to delete the file: {}", file.getAbsolutePath());
            }
        }
        if (contractAppendices.getId() == null) {
            ContractAppendices result;
            appendices.setStatus(Constants.STATUS.NEW);
            appendices.setCreatedDate(LocalDateTime.now());
            try {
                result = contractAppendicesRepository.save(appendices);
                appendices.setId(result.getId());
            } catch (Exception e) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
            }
            contractHistoryService.createContractHistory(result.getId(), result.getName(), result.getCreatedBy(), "", Constants.STATUS.NEW);
            contractStatusService.create(email, null, result.getId(), Constants.STATUS.NEW, "new appendices contract");
        } else {
            appendices.setUpdatedDate(LocalDateTime.now());
            contractHistoryService.createContractHistory(appendices.getId(), appendices.getName(), appendices.getCreatedBy(), "", Constants.STATUS.UPDATE);
            contractAppendicesRepository.save(appendices);
            String[] emails = new String[]{email};
            try {
                mailService.sendNewMail(emails, null, "Contract Update", "<h1>The contract have been updated<h1>", null);
            } catch (MessagingException e) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
            }
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Successfully", true, appendices);
    }

    public BaseResponse deleteById(String id) {
        Optional<ContractAppendices> optional = contractAppendicesRepository.findById(id);
        if (optional.isPresent()) {
            optional.get().setMarkDeleted(true);
            contractAppendicesRepository.save(optional.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete successfully", true, null);
        }
        return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "The Contract not exist", true, null);
    }

    @Override
    public BaseResponse update(String id, ContractAppendices contractAppendices) {
        Optional<ContractAppendices> optional = contractAppendicesRepository.findById(id);
        if (optional.isPresent()) {
            BeanUtils.copyProperties(contractAppendices, optional.get());
            contractAppendicesRepository.save(optional.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete successfully", true, null);
        }
        return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "The Contract not exist", true, null);
    }

    @Override
    public BaseResponse sendMail(String bearerToken, String[] to, String[] cc, String subject, String htmlContent, MultipartFile[] attachments, String contractId, String status, String description) {
        List<String> statusList = getListStatusSearch(SignContractStatus.ALL.name());
        if (!statusList.contains(status)) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "status not exist", true, null);
        }
        String email = jwtService.extractUsername(bearerToken.substring(7));
        //Contract status
        List<String> receivers = new ArrayList<>();
        for (String recipient : to) {
            receivers.add(recipient.trim());
        }
        if (cc != null) {
            for (String recipient : cc) {
                receivers.add(recipient.trim());
            }
        }
        Optional<ContractAppendices> contract = contractAppendicesRepository.findById(contractId);
        if (contract.isEmpty()) {
            return null;
        }
//        //màn hình hợp đồng của OFFICE_ADMIN:
//         btn phê duyệt hợp đồng : OFFICE_ADMIN approve thì sale sẽ enable btn gửi cho MANAGER (approve rồi disable)
        if (status.equals(SignContractStatus.WAIT_APPROVE.name())) {
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message("Bạn có hợp đồng mới cần kiểm tra")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }
        if (status.equals(SignContractStatus.APPROVED.name())) {
            String approved = jwtService.extractUsername(bearerToken.substring(7));
            contract.get().setApprovedBy(approved);
            contractAppendicesRepository.save(contract.get());
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + "đã duyệt hợp đồng")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }

        //officer-admin reject
        if (status.equals(SignContractStatus.APPROVE_FAIL.name())) {
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + "đã yêu cầu xem lại hợp đồng")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }

        // site a or b reject with reseon
        if (status.equals(SignContractStatus.SIGN_B_FAIL.name())
                || status.equals(SignContractStatus.SIGN_A_FAIL.name())
        ) {
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + " đã từ chối kí hợp đồng")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }
        List<String> statusDb = contractStatusService.checkDoneSign(contractId);

        if (status.equals(SignContractStatus.SIGN_A_OK.name())
        ) {
            if (SignContractStatus.SIGN_B_OK.name().equals(statusDb.get(1))) {
                contract.get().setStatus(Constants.STATUS.SUCCESS);
                contractAppendicesRepository.save(contract.get());
                status = SignContractStatus.SUCCESS.name();

                notificationService.create(Notification.builder()
                        .title(contract.get().getName())
                        .message(email + " đã kí hợp đồng thành công")
                        .typeNotification("CONTRACT")
                        .receivers(receivers)
                        .sender(email)
                        .build());
            }

        }

        if (status.equals(SignContractStatus.SIGN_B_OK.name())
        ) {
            if (SignContractStatus.SIGN_A_OK.name().equals(statusDb.get(1))) {
                status = SignContractStatus.SUCCESS.name();
                contract.get().setStatus(Constants.STATUS.SUCCESS);
                contractAppendicesRepository.save(contract.get());
                notificationService.create(Notification.builder()
                        .title(contract.get().getName())
                        .message(email + " đã kí hợp đồng thành công")
                        .typeNotification("CONTRACT")
                        .receivers(receivers)
                        .sender(email)
                        .build());
            }

        }


        if (status.equals(SignContractStatus.WAIT_SIGN_B.name()) || status.equals(SignContractStatus.WAIT_SIGN_A.name())) {
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + " đang chờ ký")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }
        contractStatusService.create(email, receivers, contractId, status, description);
        contractHistoryService.createContractHistory(contractId, contract.get().getName(), email, description, status);
        try {
            mailService.sendNewMail(to, cc, subject, htmlContent, attachments);
        } catch (MessagingException e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "ok", true, null);
    }

    @Override
    public BaseResponse getByContractId(String contractId, int page, int size) {
        var contract = contractAppendicesRepository.findByContractId(contractId, Pageable.ofSize(size).withPage(page));
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, contract);
    }



    private static List<String> getListStatusSearch(String statusSearch) {
        List<String> statusListSearch = new ArrayList<>();

        if (SignContractStatus.ALL.name().equals(statusSearch)) {
            statusListSearch.add(SignContractStatus.NEW.name());
            statusListSearch.add(SignContractStatus.APPROVED.name());
            statusListSearch.add(SignContractStatus.APPROVE_FAIL.name());
            statusListSearch.add(SignContractStatus.WAIT_APPROVE.name());
            statusListSearch.add(SignContractStatus.WAIT_SIGN_A.name());
            statusListSearch.add(SignContractStatus.WAIT_SIGN_B.name());
            statusListSearch.add(SignContractStatus.SIGN_B_FAIL.name());
            statusListSearch.add(SignContractStatus.SIGN_A_FAIL.name());
            statusListSearch.add(SignContractStatus.SIGN_A_OK.name());
            statusListSearch.add(SignContractStatus.SIGN_B_OK.name());
            statusListSearch.add(SignContractStatus.SUCCESS.name());
        }


        if (SignContractStatus.WAIT_SIGN.name().equals(statusSearch)) {
            statusListSearch.add(SignContractStatus.WAIT_SIGN_A.name());
            statusListSearch.add(SignContractStatus.WAIT_SIGN_B.name());
        }
        if (SignContractStatus.SIGN_OK.name().equals(statusSearch)) {
            statusListSearch.add(SignContractStatus.SIGN_A_OK.name());
            statusListSearch.add(SignContractStatus.SIGN_B_OK.name());
        }
        if (SignContractStatus.MANAGER_CONTRACT.name().equals(statusSearch)) {
            statusListSearch.add(SignContractStatus.NEW.name());
            statusListSearch.add(SignContractStatus.APPROVE_FAIL.name());
            statusListSearch.add(SignContractStatus.SIGN_B_FAIL.name());
            statusListSearch.add(SignContractStatus.SIGN_A_FAIL.name());
        } else {
            statusListSearch.add(statusSearch);
        }
        return statusListSearch;
    }

    @Override
    public BaseResponse publicSendMail(String[] to, String[] cc, String subject, String htmlContent, String createdBy, String contractAppendicesId, String status, String description) {
        if (!SignContractStatus.SIGN_B_OK.name().equals(status) && !SignContractStatus.SIGN_A_OK.name().equals(status)) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Contract not exist", false, null);
        }
        List<String> receivers = new ArrayList<>();
        for (String recipient : to) {
            receivers.add(recipient.trim());
        }
        if (cc != null) {
            for (String recipient : cc) {
                receivers.add(recipient.trim());
            }
        }
        List<String> statusDb = contractStatusService.checkDoneSign(contractAppendicesId);
        Optional<Contract> contract = contractRepository.findById(contractAppendicesId);
        if (contract.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Contract not exist", false, null);
        }

        if (status.equals(SignContractStatus.SIGN_B_OK.name())
        ) {
            if (SignContractStatus.SIGN_A_OK.name().equals(statusDb.get(1))) {
                status = SignContractStatus.SUCCESS.name();
                contract.get().setStatus(Constants.STATUS.SUCCESS);
                contractRepository.save(contract.get());
                notificationService.create(Notification.builder()
                        .title(contract.get().getName())
                        .message(createdBy + "đã kí hợp đồng thành công")
                        .typeNotification("CONTRACT")
                        .receivers(receivers)
                        .sender(createdBy)
                        .build());
            }

        }

        if (status.equals(SignContractStatus.SIGN_A_OK.name())
        ) {
            if (SignContractStatus.SIGN_B_OK.name().equals(statusDb.get(1))) {
                contract.get().setStatus(Constants.STATUS.SUCCESS);
                status = SignContractStatus.SUCCESS.name();
                contractRepository.save(contract.get());
                notificationService.create(Notification.builder()
                        .title(contract.get().getName())
                        .message(createdBy + "đã kí hợp đồng thành công")
                        .typeNotification("CONTRACT")
                        .receivers(receivers)
                        .sender(createdBy)
                        .build());
            }

        }
        try {
            mailService.sendNewMail(to, cc, subject, htmlContent, null);
        } catch (MessagingException e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Mail error", false, null);
        }
        contractStatusService.create(createdBy, receivers, contractAppendicesId, status, description);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "ok", true, null);
    }

    @Override
    public BaseResponse getContractSignById(String id) {
        var contractAppendices = contractAppendicesRepository.findByIdContractAppendices(id);
        return contractAppendices.map(appendices -> new BaseResponse(Constants.ResponseCode.SUCCESS, "Find Contract Appendices", true, appendices)).orElseGet(() -> new BaseResponse(Constants.ResponseCode.FAILURE, "Contract Appendices not exist", false, null));

    }

}
