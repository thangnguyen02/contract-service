package com.fpt.servicecontract.config;

import com.fpt.servicecontract.contract.service.PaySlipService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksConfig {
    private final MailService mailService;
    private final PaySlipService paySlipService;

    @Scheduled(cron = "0 20 21 * * ?")
    public void scheduleTaskUsingCronExpression() throws MessagingException {
        LocalDate today = LocalDate.now();
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        log.info("Scheduled task triggered on: {} , {}" , today, lastDayOfMonth);

        if (today.equals(lastDayOfMonth)) {
            paySlipService.CalculateAllPaySlip();
            log.info("Running job on the last day of the month: " + today);
        }
    }
}
