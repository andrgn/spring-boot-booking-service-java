package com.booker.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 1, max = 30)
    @JsonProperty("firstname")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 30)
    @JsonProperty("lastname")
    private String lastName;

    @NonNull
    // Если использовать @PositiveOrZero, то в схеме сущности в swagger-ui не будет явно описано ограничение
    @Min(0)
    @JsonProperty("totalprice")
    private Integer totalPrice;

    @NonNull
    @JsonProperty("depositpaid")
    private Boolean depositPaid;

    @NonNull
    @Size(max = 150)
    @JsonProperty("additionalneeds")
    private String additionalNeeds;

    @Embedded
    @NonNull
    @JsonProperty("bookingdates")
    private BookingDates bookingDates;

    @Embeddable
    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    static public class BookingDates {

        @NonNull
        @FutureOrPresent
        private LocalDate checkin;

        @NonNull
        @FutureOrPresent
        private LocalDate checkout;
    }
}
