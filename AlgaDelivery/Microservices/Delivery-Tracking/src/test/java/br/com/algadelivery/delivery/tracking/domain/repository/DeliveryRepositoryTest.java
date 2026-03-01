package br.com.algadelivery.delivery.tracking.domain.repository;

import br.com.algadelivery.delivery.tracking.domain.model.ContactPoint;
import br.com.algadelivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist() {
        var delivery = Delivery.draft();

        delivery.editPreparationDetails(createValidPreparationDetails());
        delivery.addItem("Computador", 2);
        delivery.addItem("Notebook", 2);

        deliveryRepository.saveAndFlush(delivery);

        var persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2, persistedDelivery.getItems().size());

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