package com.booker.demo;

import com.booker.demo.assemblers.BookingModelAssembler;
import com.booker.demo.data_base.BookingRepository;
import com.booker.demo.entities.Booking;
import com.booker.demo.utils.BookingDatesChecker;
import com.booker.demo.exceptions.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
public class BookingController {

    private final BookingRepository repository;
    private final BookingModelAssembler assembler;

    @GetMapping("/bookings")
    public ResponseEntity<CollectionModel<EntityModel<Booking>>> getAllBookings() {

        List<EntityModel<Booking>> bookings = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Booking>> collectionModel = CollectionModel.of(bookings,
                linkTo(methodOn(BookingController.class).getAllBookings()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping("/bookings")
    public ResponseEntity<EntityModel<Booking>> createBooking(@Valid @RequestBody Booking newBooking) {

        BookingDatesChecker.checkoutIsBeforeCheckin(newBooking);

        EntityModel<Booking> entityModel = assembler.toModel(repository.save(newBooking));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<EntityModel<Booking>> getBooking(@PathVariable Long id) {

        Booking booking = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        EntityModel<Booking> entityModel = assembler.toModel(booking);

        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<EntityModel<Booking>> updateBooking(@PathVariable Long id, @Valid @RequestBody Booking newBooking) {

        BookingDatesChecker.checkoutIsBeforeCheckin(newBooking);

        Booking updatedBooking = repository.findById(id)
                .map(booking -> {
                    booking.setFirstName(newBooking.getFirstName());
                    booking.setLastName(newBooking.getLastName());
                    booking.setTotalPrice(newBooking.getTotalPrice());
                    booking.setDepositPaid(newBooking.getDepositPaid());
                    booking.setAdditionalNeeds(newBooking.getAdditionalNeeds());
                    return repository.save(booking);
                })
                .orElseGet(() -> {
                    newBooking.setId(id);
                    return repository.save(newBooking);
                });

        EntityModel<Booking> entityModel = assembler.toModel(updatedBooking);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<EntityModel<Booking>> deleteBooking(@PathVariable Long id) {
        
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
