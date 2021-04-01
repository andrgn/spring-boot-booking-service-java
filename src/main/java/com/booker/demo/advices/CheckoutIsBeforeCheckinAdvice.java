package com.booker.demo.advices;

import com.booker.demo.exceptions.CheckoutIsBeforeCheckinException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CheckoutIsBeforeCheckinAdvice {

    @ResponseBody
    @ExceptionHandler(CheckoutIsBeforeCheckinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String checkoutIsBeforeCheckinHandler(CheckoutIsBeforeCheckinException exception) {
        return exception.getMessage();
    }
}
