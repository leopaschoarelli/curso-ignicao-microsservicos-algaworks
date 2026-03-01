package br.com.algadelivery.delivery.tracking.domain.service;

import br.com.algadelivery.delivery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {

    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);

}
