package com.inditex.prices.infrastructure.adapter.in.rest;

import com.inditex.prices.application.port.in.GetApplicablePriceUseCase;
import com.inditex.prices.infrastructure.adapter.in.rest.api.PricesApi;
import com.inditex.prices.infrastructure.adapter.in.rest.dto.PriceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class PriceController implements PricesApi {

    private final GetApplicablePriceUseCase getApplicablePrice;

    public PriceController(GetApplicablePriceUseCase getApplicablePrice) {
        this.getApplicablePrice = getApplicablePrice;
    }

    @Override
    public ResponseEntity<PriceResponse> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return ResponseEntity.ok(
                PriceMapper.toResponse(getApplicablePrice.getApplicablePrice(brandId, productId, applicationDate)));
    }
}
