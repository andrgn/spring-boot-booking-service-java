package com.booker.demo.data_base;

import com.booker.demo.entities.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(BookingRepository repository) {
        Booking booking = new Booking()
                .setFirstName("John")
                .setLastName("Jackson")
                .setTotalPrice(123)
                .setDepositPaid(true)
                .setAdditionalNeeds("Breakfast")
                .setBookingDates(new Booking.BookingDates()
                        .setCheckin(LocalDate.now())
                        .setCheckout(LocalDate.now())
                );

        return args -> {
            log.info("=".repeat(100));
            log.info("Preloading: " + repository.save(booking));
        };
    }
}
