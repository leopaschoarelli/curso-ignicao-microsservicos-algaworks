package br.com.algadelivery.delivery.tracking.domain.service;

import java.math.BigDecimal;

public interface CourierPayoutCalculationService {

    BigDecimal calculatePayout(Double distanceInKm);

}
