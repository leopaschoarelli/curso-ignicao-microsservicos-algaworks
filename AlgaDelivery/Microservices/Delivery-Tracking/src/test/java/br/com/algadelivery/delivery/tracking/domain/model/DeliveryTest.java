package br.com.algadelivery.delivery.tracking.domain.model;

import br.com.algadelivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shouldChangeToPlaced() {
        var delivery = Delivery.draft();

        delivery.editPreparationDetails(createValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    @Test
    public void shouldNotPlace() {
        var delivery = Delivery.draft();

        assertThrows(DomainException.class, delivery::place);

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createValidPreparationDetails() {

        var sender = ContactPoint.builder()
                                 .zipCode("00000-000")
                                 .street("Rua São Paulo")
                                 .number("100")
                                 .complement("Sala 404")
                                 .name("Leonardo Paschoarelli")
                                 .phone("(44) 12345-6789")
                                 .build();

        var recipient = ContactPoint.builder()
                                    .zipCode("12345-789")
                                    .street("Rua Brasil")
                                    .number("500")
                                    .complement("")
                                    .name("João")
                                    .phone("(44) 98765-4321")
                                    .build();

        return Delivery.PreparationDetails.builder()
                                          .sender(sender)
                                          .recipient(recipient)
                                          .distanceFee(new BigDecimal("15.00"))
                                          .courierPayout(new BigDecimal("5.00"))
                                          .expectedDeliveryTime(Duration.ofHours(5))
                                          .build();

    }

}