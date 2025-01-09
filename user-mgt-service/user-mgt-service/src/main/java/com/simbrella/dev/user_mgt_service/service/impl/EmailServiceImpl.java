package com.simbrella.dev.user_mgt_service.service.impl;

import com.simbrella.dev.user_mgt_service.dto.EmailDto;
import com.simbrella.dev.user_mgt_service.exception.CustomValidationException;
import com.simbrella.dev.user_mgt_service.service.EmailService;
import com.simbrella.dev.user_mgt_service.service.UserService;
import com.simbrella.dev.user_mgt_service.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final AppUtil appUtil;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(EmailDto emailDto) {

        appUtil.validateEmailDomain(emailDto.getTo());
        if (!AppUtil.isEmailValid(emailDto.getTo())) {
            throw new CustomValidationException("Invalid email");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ewalletappllc@gmail.com");
        message.setTo(emailDto.getTo());
        message.setSentDate(new Date());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getBody());

        try {
            log.info("Beginning of log ******");
            log.info("Sending mail to: {}", emailDto.getTo());
            javaMailSender.send(message);

        } catch (MailException ex) {
            log.error(ex.getMessage());

        }
    }
}