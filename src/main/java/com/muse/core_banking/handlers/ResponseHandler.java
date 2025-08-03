package com.muse.core_banking.handlers;

import com.muse.core_banking.enums.ApiMessage;
import lombok.*;

import java.util.Optional;

@Builder
@Setter
@Getter
public class ResponseHandler <T> {

    private Optional<T> data;
    private String message;
    private int statusCode;
}
