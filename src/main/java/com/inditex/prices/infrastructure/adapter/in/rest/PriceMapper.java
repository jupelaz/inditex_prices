package com.inditex.prices.infrastructure.adapter.in.rest;

import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.adapter.in.rest.dto.PriceResponse;

final class PriceMapper {

    private PriceMapper() {
    }

    static PriceResponse toResponse(Price price) {
        return new PriceResponse()
                .productId(price.productId())
                .brandId(price.brandId())
                .priceList(price.priceList())
                .startDate(price.startDate())
                .endDate(price.endDate())
                .price(price.amount())
                .currency(price.currency());
    }
}
