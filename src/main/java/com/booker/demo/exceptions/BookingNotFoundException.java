package com.booker.demo.exceptions;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Long id) {
        super("Could not find booking with id: " + id);
    }
}
