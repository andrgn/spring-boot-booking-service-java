package com.booker.demo;

import com.booker.demo.assemblers.BookingModelAssembler;
import com.booker.demo.entities.Booking;
import com.booker.demo.exceptions.CheckoutIsBeforeCheckinException;
import com.booker.demo.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.booker.demo.utils.BookingDatesUtils.isCheckoutBeforeCheckin;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
public class BookingController {

    private final BookingService bookingService;
    private final BookingModelAssembler assembler;

    @GetMapping("/bookings")
    public ResponseEntity<CollectionModel<EntityModel<Booking>>> getAllBookings() {
        var foundBookings = bookingService.findAllBookings();
        var entityModels = convertListOfBookingsToListOfEntityModels(foundBookings);
        var collectionModel = createCollectionModel(entityModels);

        return ResponseEntity.ok(collectionModel);
    }

    private List<EntityModel<Booking>> convertListOfBookingsToListOfEntityModels(List<Booking> bookings) {
        return bookings.stream().map(assembler::toModel).collect(Collectors.toList());
    }

    private CollectionModel<EntityModel<Booking>> createCollectionModel(List<EntityModel<Booking>> bookings) {
        var invocationValue = methodOn(BookingController.class).getAllBookings();
        var link = linkTo(invocationValue).withSelfRel();

        return CollectionModel.of(bookings, link);
    }

    @PostMapping("/bookings")
    public ResponseEntity<EntityModel<Booking>> createBooking(@Valid @RequestBody Booking booking) {
        if (isCheckoutBeforeCheckin(booking)) {
            throw new CheckoutIsBeforeCheckinException();
        }

        var createdBooking = bookingService.createBooking(booking);
        var entityModel = assembler.toModel(createdBooking);
        var location = createLocationFromEntityModel(entityModel);

        return ResponseEntity
                .created(location)
                .body(entityModel);
    }

    private <T> URI createLocationFromEntityModel(EntityModel<T> entityModel) {
        return entityModel
                .getRequiredLink(IanaLinkRelations.SELF)
                .toUri();
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<EntityModel<Booking>> getBooking(@PathVariable Long id) {
        var booking = bookingService.findBookingById(id);
        var entityModel = assembler.toModel(booking);

        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/bookings")
    public ResponseEntity<EntityModel<Booking>> updateBooking(@Valid @RequestBody Booking booking) {
        if (isCheckoutBeforeCheckin(booking)) {
            throw new CheckoutIsBeforeCheckinException();
        }

        var updatedBooking = bookingService.updateBooking(booking);
        var entityModel = assembler.toModel(updatedBooking);
        var location = createLocationFromEntityModel(entityModel);

        return ResponseEntity
                .created(location)
                .body(entityModel);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<EntityModel<Booking>> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBookingById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
