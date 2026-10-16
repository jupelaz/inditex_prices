package com.inditex.prices.application.service;

import com.inditex.prices.application.port.out.PriceRepositoryPort;
import com.inditex.prices.domain.exception.PriceNotFoundException;
import com.inditex.prices.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetApplicablePriceServiceTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2020, 6, 14, 10, 0);

    @Mock
    private PriceRepositoryPort priceRepository;

    @InjectMocks
    private GetApplicablePriceService service;

    @Test
    void returnsPriceFoundByRepository() {
        Price price = new Price(1L, 35455L, 1, DATE.minusDays(1), DATE.plusDays(1), new BigDecimal("35.50"), "EUR");
        when(priceRepository.findApplicablePrice(1L, 35455L, DATE)).thenReturn(Optional.of(price));

        assertThat(service.getApplicablePrice(1L, 35455L, DATE)).isEqualTo(price);
    }

    @Test
    void throwsWhenNoPriceIsApplicable() {
        when(priceRepository.findApplicablePrice(1L, 35455L, DATE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getApplicablePrice(1L, 35455L, DATE))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("35455");
    }
}
