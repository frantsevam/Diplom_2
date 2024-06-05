package ordertest;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderData;
import order.OrderRequests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest {
    private UserRequests userRequests;
    private OrderRequests orderRequests;
    private UserRegistration userRegistration;
    private OrderData orderData;
    private String accessToken;
    List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa70",
            "61c0c5a71d1f82001bdaaa72");

    @Before
    public void setUp() {
        userRequests = new UserRequests();
        userRegistration = UserGenerator.getUserRandom();
        userRequests.createUser(userRegistration);
        orderRequests = new OrderRequests();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequests.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов авторизированного пользователя")
    public void getOrdersWithAuthorizationTest() {
        UserData userData = new UserData(userRegistration.getEmail(), userRegistration.getPassword());
        ValidatableResponse login = userRequests.authorizationUser(userData);
        login.assertThat().statusCode(200).and().body("success", equalTo(true));
        accessToken = login.extract().path("accessToken");
        UserToken userToken = new UserToken(accessToken);
        orderData = new OrderData(ingredients);
        ValidatableResponse order = orderRequests.createOrderWithAuthorization(orderData, userToken);
        order.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());

        ValidatableResponse getOrder = orderRequests.getOrderWithAuthorization(userToken);
        getOrder.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    public void getOrdersWithoutAuthorizationTest() {
        orderData = new OrderData(ingredients);
        ValidatableResponse order = orderRequests.createOrderWithoutAuthorization(orderData);
        order.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());

        ValidatableResponse getOrder = orderRequests.getOrderWithoutAuthorization();
        getOrder.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

}

