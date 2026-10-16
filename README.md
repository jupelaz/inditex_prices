# Prices service

Spring Boot service exposing a REST endpoint that returns the price applicable to a product of a brand at a given date. When several price lists overlap, the one with the highest `PRIORITY` wins.

## Stack

Java 17 · Spring Boot 3.3 · Spring Data JPA · H2 (in memory) · OpenAPI Generator · JUnit 5, Mockito, REST Assured

## Architecture

Hexagonal (ports & adapters):

```
com.inditex.prices
├── domain
│   ├── model          Price (domain record)
│   └── exception      PriceNotFoundException
├── application
│   ├── port.in        GetApplicablePriceUseCase
│   ├── port.out       PriceRepositoryPort
│   └── service        GetApplicablePriceService
└── infrastructure.adapter
    ├── in.rest        PriceController, PriceMapper, GlobalExceptionHandler
    │                  (api/ and dto/ are generated from the OpenAPI spec)
    └── out.persistence PriceEntity, PriceJpaRepository, PriceRepositoryAdapter
```

The domain and application layers do not depend on web or persistence classes. The selection rule (date in range, highest priority first) is resolved in the persistence adapter query.

## API first

The contract lives in `src/main/resources/openapi/prices-api.yaml`. During `generate-sources` the OpenAPI Generator plugin produces the `PricesApi` interface and the `PriceResponse` DTO in `target/generated-sources`. `PriceController` implements `PricesApi`, so any change must start in the spec.

## Run

Requires JDK 17.

```bash
./mvnw spring-boot:run
```

The H2 database is created and loaded on startup from `schema.sql` and `data.sql` (the example data of the exercise).

## API

`GET /api/v1/prices?applicationDate={ISO date-time}&productId={id}&brandId={id}`

```bash
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

Errors are returned as RFC 7807 problem details:

| Status | Case |
|--------|------|
| 404 | No price applies for the brand, product and date |
| 400 | Missing parameter or wrong format (e.g. invalid date) |

## Tests

```bash
./mvnw verify
```

- `GetApplicablePriceServiceTest`: unit tests of the use case (Mockito).
- `PriceControllerE2ETest`: end-to-end tests with REST Assured against the running application (random port, H2). They cover the five requested scenarios (10:00, 16:00 and 21:00 on day 14; 10:00 on day 15; 21:00 on day 16 for product 35455 and brand 1) plus the 404 and 400 cases.
