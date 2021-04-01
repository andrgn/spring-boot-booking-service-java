package com.booker.demo.exceptions;

import java.time.LocalDate;

public class CheckoutIsBeforeCheckinException extends RuntimeException {

    public CheckoutIsBeforeCheckinException(LocalDate checkin, LocalDate checkout) {
        super(String.format("Checkout (%s) is before checkin (%s)", checkout, checkin));
    }
}
