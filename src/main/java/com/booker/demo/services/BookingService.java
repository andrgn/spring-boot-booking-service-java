package com.booker.demo.services;

import com.booker.demo.data_base.BookingRepository;
import com.booker.demo.entities.Booking;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record BookingService(BookingRepository bookingRepository) {

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking findBookingById(Long id) {
        // similar behavior as when invoking db.deleteById(...)
        return bookingRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }
}
