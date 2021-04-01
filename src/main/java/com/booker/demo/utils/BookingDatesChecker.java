package com.booker.demo.utils;

import com.booker.demo.entities.Booking;
import com.booker.demo.exceptions.CheckoutIsBeforeCheckinException;

import java.time.LocalDate;

public class BookingDatesChecker {

    public static void checkoutIsBeforeCheckin(Booking booking) {
        LocalDate checkin = booking.getBookingDates().getCheckin();
        LocalDate checkout = booking.getBookingDates().getCheckout();

        if (checkout.isBefore(checkin)) {
            throw new CheckoutIsBeforeCheckinException(checkin, checkout);
        }
    }
}
