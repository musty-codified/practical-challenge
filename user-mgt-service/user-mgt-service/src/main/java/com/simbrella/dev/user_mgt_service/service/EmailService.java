package com.simbrella.dev.user_mgt_service.service;

import com.simbrella.dev.user_mgt_service.dto.EmailDto;

public interface EmailService {
    void sendMail(EmailDto emailDto);
}
