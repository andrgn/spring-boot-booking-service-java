package com.booker.demo.utils;

import com.booker.demo.entities.Booking;

public class BookingDatesUtils {

    public static Boolean isCheckoutBeforeCheckin(Booking booking) {
        var checkin = booking.getBookingDates().getCheckin();
        var checkout = booking.getBookingDates().getCheckout();

        return checkout.isBefore(checkin);
    }
}
