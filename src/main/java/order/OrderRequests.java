package order;

import constants.ApiAndUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.UserToken;

import static io.restassured.RestAssured.given;

public class OrderRequests extends ApiAndUrl {
    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrderWithAuthorization(OrderData orderData, UserToken userToken) {
        return given()
                .spec(setUp())
                .header("Authorization", userToken.getToken())
                .body(orderData)
                .when()
                .post(ORDERS)
                .then()
                .log().all();
    }

    @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse createOrderWithoutAuthorization(OrderData orderData) {
        return given()
                .spec(setUp())
                .body(orderData)
                .when()
                .post(ORDERS)
                .then()
                .log().all();
    }

    @Step("Получение заказа авторизованным пользователем")
    public ValidatableResponse getOrderWithAuthorization(UserToken userToken) {
        return given()
                .spec(setUp())
                .header("Authorization", userToken.getToken())
                .when()
                .get(ORDERS)
                .then()
                .log().all();
    }

    @Step("Получение заказа неавторизованным пользователем")
    public ValidatableResponse getOrderWithoutAuthorization() {
        return given()
                .spec(setUp())
                .when()
                .get(ORDERS)
                .then()
                .log().all();
    }
}
