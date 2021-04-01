package com.booker.demo.assemblers;

import com.booker.demo.entities.Booking;
import com.booker.demo.BookingController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookingModelAssembler implements RepresentationModelAssembler<Booking, EntityModel<Booking>> {

    @Override
    public EntityModel<Booking> toModel(Booking booking) {

        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBooking(booking.getId())).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"));
    }
}
