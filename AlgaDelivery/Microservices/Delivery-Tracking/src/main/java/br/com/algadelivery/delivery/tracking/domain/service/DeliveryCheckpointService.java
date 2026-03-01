package br.com.algadelivery.delivery.tracking.domain.service;

import br.com.algadelivery.delivery.tracking.api.model.CourierIdInput;
import br.com.algadelivery.delivery.tracking.domain.exception.DomainException;
import br.com.algadelivery.delivery.tracking.domain.model.Delivery;
import br.com.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCheckpointService {

    private final DeliveryRepository deliveryRepository;

    public void place(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DomainException::new);
        delivery.place();
        deliveryRepository.saveAndFlush(delivery);
    }

    public void pickUp(UUID deliveryId, UUID courierId) {
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DomainException::new);
        delivery.pickUp(courierId);
        deliveryRepository.saveAndFlush(delivery);
    }

    public void complete(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DomainException::new);
        delivery.markAsDelivered();
        deliveryRepository.saveAndFlush(delivery);
    }

}
