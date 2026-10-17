# Prices service

REST service that returns the price applicable to a product of a brand at a given date. If several price lists overlap, the one with the highest priority is returned.

Java 17, Spring Boot 3.3, Spring Data JPA, H2 in memory.

## Run

```bash
./mvnw spring-boot:run
```

The H2 database is created and loaded on startup from `schema.sql` and `data.sql` (the example data of the exercise).

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

Errors are returned as problem details (RFC 7807): 404 when no price applies, 400 when a parameter is missing or malformed.

## Tests

```bash
./mvnw verify
```

- `GetApplicablePriceServiceTest`: unit tests of the use case.
- `PriceControllerE2ETest`: REST Assured tests against the running app. They cover the five scenarios of the exercise plus the 404 and 400 cases.

## Notes

- Hexagonal layout: `domain` and `application` know nothing about Spring MVC or JPA. REST and persistence live under `infrastructure.adapter`.
- The API is defined first in `src/main/resources/openapi/prices-api.yaml`. The `PricesApi` interface and the response DTO are generated into `target/` during the build, so run `./mvnw generate-sources` (or reload Maven in the IDE) if they appear as missing.
- The rule "date in range, highest priority first" is resolved in the persistence query, not in Java.
