package com.stephen.bank_application.service.impl;

import com.stephen.bank_application.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
