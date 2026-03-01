package br.com.algadelivery.delivery.tracking.domain.service;

import br.com.algadelivery.delivery.tracking.api.model.DeliveryInput;
import br.com.algadelivery.delivery.tracking.domain.exception.DomainException;
import br.com.algadelivery.delivery.tracking.domain.model.ContactPoint;
import br.com.algadelivery.delivery.tracking.domain.model.Delivery;
import br.com.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryTimeEstimationService deliveryTimeEstimationService;
    private final CourierPayoutCalculationService courierPayoutCalculationService;

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

        DeliveryEstimate estimate = deliveryTimeEstimationService.estimate(sender, recipient);
        BigDecimal payout = courierPayoutCalculationService.calculatePayout(estimate.getDistanceInKm());

        BigDecimal distanceFee = calculateFee(estimate.getDistanceInKm());

        var preparationDetails = Delivery.PreparationDetails.builder()
                                         .recipient(recipient)
                                         .sender(sender)
                                         .expectedDeliveryTime(estimate.getEstimatedTime())
                                         .courierPayout(payout)
                                         .distanceFee(distanceFee)
                                         .build();

        delivery.editPreparationDetails(preparationDetails);

        input.getItems().forEach(item -> delivery.addItem(item.getName(), item.getQuantity()));

    }

    private BigDecimal calculateFee(Double distanceInKm) {
        return new BigDecimal("3").multiply(new BigDecimal(distanceInKm))
                                      .setScale(2, RoundingMode.HALF_EVEN);
    }

}
