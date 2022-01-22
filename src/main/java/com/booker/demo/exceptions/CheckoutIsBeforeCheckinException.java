package com.booker.demo.exceptions;

public class CheckoutIsBeforeCheckinException extends RuntimeException {

    public CheckoutIsBeforeCheckinException() {
        super("checkout must be on the day of checkin or later");
    }
}
