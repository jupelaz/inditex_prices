package com.inditex.prices.infrastructure.adapter.in.rest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceControllerE2ETest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "Test 1: 10:00 day 14,  2020-06-14T10:00:00, 1, 2020-06-14T00:00:00, 2020-12-31T23:59:59, 35.50",
            "Test 2: 16:00 day 14,  2020-06-14T16:00:00, 2, 2020-06-14T15:00:00, 2020-06-14T18:30:00, 25.45",
            "Test 3: 21:00 day 14,  2020-06-14T21:00:00, 1, 2020-06-14T00:00:00, 2020-12-31T23:59:59, 35.50",
            "Test 4: 10:00 day 15,  2020-06-15T10:00:00, 3, 2020-06-15T00:00:00, 2020-06-15T11:00:00, 30.50",
            "Test 5: 21:00 day 16,  2020-06-16T21:00:00, 4, 2020-06-15T16:00:00, 2020-12-31T23:59:59, 38.95"
    })
    void returnsApplicablePrice(String name, String applicationDate, int priceList,
                                String startDate, String endDate, float price) {
        given()
                .queryParam("applicationDate", applicationDate)
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/api/v1/prices")
        .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(priceList))
                .body("startDate", equalTo(startDate))
                .body("endDate", equalTo(endDate))
                .body("price", equalTo(price))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void returnsNotFoundWhenNoPriceApplies() {
        given()
                .queryParam("applicationDate", "2019-01-01T10:00:00")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/api/v1/prices")
        .then()
                .statusCode(404)
                .body("detail", containsString("No price found"));
    }

    @Test
    void returnsBadRequestWhenParameterIsMissing() {
        given()
                .queryParam("applicationDate", "2020-06-14T10:00:00")
                .queryParam("brandId", 1)
        .when()
                .get("/api/v1/prices")
        .then()
                .statusCode(400);
    }

    @Test
    void returnsBadRequestWhenParameterIsMalformed() {
        given()
                .queryParam("applicationDate", "not-a-date")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)
        .when()
                .get("/api/v1/prices")
        .then()
                .statusCode(400);
    }
}
