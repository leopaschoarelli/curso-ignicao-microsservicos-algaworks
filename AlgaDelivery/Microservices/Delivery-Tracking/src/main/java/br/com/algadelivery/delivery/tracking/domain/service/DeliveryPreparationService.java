package br.com.algadelivery.delivery.tracking.domain.service;

import br.com.algadelivery.delivery.tracking.api.model.ContactPointInput;
import br.com.algadelivery.delivery.tracking.api.model.DeliveryInput;
import br.com.algadelivery.delivery.tracking.api.model.ItemInput;
import br.com.algadelivery.delivery.tracking.domain.exception.DomainException;
import br.com.algadelivery.delivery.tracking.domain.model.ContactPoint;
import br.com.algadelivery.delivery.tracking.domain.model.Delivery;
import br.com.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryInput input) {
        var delivery = Delivery.draft();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput input) {
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DomainException::new);

        delivery.removeItems();
        handlePreparation(input, delivery);

        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryInput input, Delivery delivery) {
        var senderInput = input.getSender();
        var recipientInput = input.getRecipient();

        var sender = ContactPoint.builder()
                                 .phone(senderInput.getPhone())
                                 .name(senderInput.getName())
                                 .complement(senderInput.getComplement())
                                 .number(senderInput.getNumber())
                                 .zipCode(senderInput.getZipCode())
                                 .street(senderInput.getStreet())
                                 .build();

        var recipient = ContactPoint.builder()
                                    .phone(recipientInput.getPhone())
                                    .name(recipientInput.getName())
                                    .complement(recipientInput.getComplement())
                                    .number(recipientInput.getNumber())
                                    .zipCode(recipientInput.getZipCode())
                                    .street(recipientInput.getStreet())
                                    .build();

        Duration expectedDeliveryTime = Duration.ofHours(3);
        BigDecimal distanceFee = new BigDecimal("10");

        BigDecimal payout = new BigDecimal("10");


        var preparationDetails = Delivery.PreparationDetails.builder()
                                         .recipient(recipient)
                                         .sender(sender)
                                         .expectedDeliveryTime(expectedDeliveryTime)
                                         .courierPayout(payout)
                                         .distanceFee(distanceFee)
                                         .build();

        delivery.editPreparationDetails(preparationDetails);

        input.getItems().forEach(item -> delivery.addItem(item.getName(), item.getQuantity()));

    }

}
