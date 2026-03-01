package br.com.algadelivery.courier.management.domain.service;

import br.com.algadelivery.courier.management.api.model.CourierInput;
import br.com.algadelivery.courier.management.domain.model.Courier;
import br.com.algadelivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {

    private final CourierRepository courierRepository;

    public Courier create(@Valid CourierInput input) {
        var courier = Courier.brandNew(input.getName(), input.getPhone());
        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, @Valid CourierInput input) {
        var courier = courierRepository.findById(courierId).orElseThrow();

        courier.setName(input.getName());
        courier.setPhone(input.getPhone());

        return courierRepository.saveAndFlush(courier);
    }

}
