package com.inditex.prices.infrastructure.adapter.out.persistence;

import com.inditex.prices.application.port.out.PriceRepositoryPort;
import com.inditex.prices.domain.model.Price;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository jpaRepository;

    PriceRepositoryAdapter(PriceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return jpaRepository.findApplicablePrice(brandId, productId, applicationDate).map(this::toDomain);
    }

    private Price toDomain(PriceEntity entity) {
        return new Price(entity.getBrandId(), entity.getProductId(), entity.getPriceList(),
                entity.getStartDate(), entity.getEndDate(), entity.getPrice(), entity.getCurrency());
    }
}
