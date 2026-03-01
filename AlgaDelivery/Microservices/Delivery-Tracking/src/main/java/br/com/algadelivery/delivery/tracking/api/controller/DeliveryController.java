package br.com.algadelivery.delivery.tracking.api.controller;

import br.com.algadelivery.delivery.tracking.api.model.DeliveryInput;
import br.com.algadelivery.delivery.tracking.domain.model.Delivery;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput deliveryInput) {
        return null;
    }

    @PutMapping("/{deliveryId}")
    public Delivery edit(@PathVariable UUID deliveryId,
                         @RequestBody @Valid DeliveryInput deliveryInput) {
        return null;
    }

}
