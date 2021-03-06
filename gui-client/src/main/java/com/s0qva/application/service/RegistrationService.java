package com.s0qva.application.service;

import com.s0qva.application.dto.user.UserCreationDto;
import com.s0qva.application.http.RestRequestSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {
    @Value("${rest-server.url.users}")
    private String usersUrl;

    public boolean signUp(UserCreationDto userCreationDto) {
        ResponseEntity<Void> responseEntity = RestRequestSender.post(usersUrl, userCreationDto);

        return responseEntity.getStatusCode()
                .equals(HttpStatus.CREATED);
    }
}
