package com.inditex.prices.application.service;

import com.inditex.prices.application.port.in.GetApplicablePriceUseCase;
import com.inditex.prices.application.port.out.PriceRepositoryPort;
import com.inditex.prices.domain.exception.PriceNotFoundException;
import com.inditex.prices.domain.model.Price;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepository;

    public GetApplicablePriceService(PriceRepositoryPort priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceRepository.findApplicablePrice(brandId, productId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(brandId, productId, applicationDate));
    }
}
