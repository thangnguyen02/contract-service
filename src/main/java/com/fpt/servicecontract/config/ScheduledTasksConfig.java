package com.fpt.servicecontract.config;

import com.fpt.servicecontract.contract.service.PaySlipService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class ScheduledTasksConfig {
    private final MailService mailService;
    private final PaySlipService paySlipService;

    @Scheduled(cron = "0 0 15 * * ?")
    public void scheduleTaskUsingCronExpression() throws MessagingException {
        LocalDate today = LocalDate.now();
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        if (today.equals(lastDayOfMonth)) {
            paySlipService.CalculateAllPaySlip();
            System.out.println("Running job on the last day of the month: " + today);
        }
    }
}
