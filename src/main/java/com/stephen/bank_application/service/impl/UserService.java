package com.stephen.bank_application.service.impl;

import com.stephen.bank_application.dto.BankResponse;
import com.stephen.bank_application.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

}
