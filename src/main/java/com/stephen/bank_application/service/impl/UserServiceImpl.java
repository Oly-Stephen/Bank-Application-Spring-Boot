package com.stephen.bank_application.service.impl;

import com.stephen.bank_application.dto.AccountInfo;
import com.stephen.bank_application.dto.BankResponse;
import com.stephen.bank_application.dto.EmailDetails;
import com.stephen.bank_application.dto.UserRequest;
import com.stephen.bank_application.entity.User;
import com.stephen.bank_application.repository.UserRepository;
import com.stephen.bank_application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        //Creating an account - saving a new user into the db
        // check if user already has an account
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
      User newUser = User.builder()
              .firstName(userRequest.getFirstName())
              .lastName(userRequest.getLastName())
              .otherName(userRequest.getOtherName())
              .gender(userRequest.getGender())
              .address(userRequest.getAddress())
              .stateOfOrigin(userRequest.getStateOfOrigin())
              .accountNumber(AccountUtils.generateAccountNumber())
              .accountBalance(BigDecimal.ZERO)
              .email(userRequest.getEmail())
              .phoneNumber(userRequest.getPhoneNumber())
              .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
              .status("ACTIVE") //Todo: change the status in entity to be enumerate
              .build();

        User savedUser = userRepository.save(newUser);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has been Successfully Created. \n Your Account Details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n" +
                        "Account Number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName((savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName()))
                        .build())
                .build();
    }
}
