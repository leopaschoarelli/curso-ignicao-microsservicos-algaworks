package br.com.algadelivery.courier.management.api.controller;

import br.com.algadelivery.courier.management.domain.model.Courier;
import br.com.algadelivery.courier.management.domain.repository.CourierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourierControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CourierRepository courierRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/couriers";
    }

    @Test
    public void shoulReturn201() {
        String requestBody = """
                             {
                                "name" : "Leonardo Paschoarelli",
                                "phone" : "(44) 12345-6789"
                             }
                             """;

        RestAssured.given()
                      .body(requestBody)
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                   .when()
                     .post()
                   .then()
                     .statusCode(HttpStatus.CREATED.value())
                     .body("id", Matchers.notNullValue())
                     .body("name", Matchers.equalTo("Leonardo Paschoarelli"));
    }

    @Test
    public void shouldReturn200() {
        var courierId = courierRepository.saveAndFlush(Courier.brandNew("Leonardo Paschoarelli", "(44) 12345-6789")).getId();

        RestAssured.given()
                      .pathParam("courierId", courierId)
                      .accept(ContentType.JSON)
                   .when()
                      .get("/{courierId}")
                   .then()
                      .statusCode(HttpStatus.OK.value())
                      .body("id", Matchers.equalTo(courierId.toString()))
                      .body("name", Matchers.equalTo("Leonardo Paschoarelli"))
                      .body("phone", Matchers.equalTo("(44) 12345-6789"));

    }


}