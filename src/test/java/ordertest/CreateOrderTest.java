package ordertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderData;
import order.OrderRequests;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    private UserRequests userRequests;
    private OrderRequests orderRequests;
    private UserRegistration userRegistration;
    private OrderData orderData;
    private String accessToken;
    ;
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
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthorizationAndIngredientsTest() {
        UserData userData = new UserData(userRegistration.getEmail(), userRegistration.getPassword());
        ValidatableResponse login = userRequests.authorizationUser(userData);
        login.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = login.extract().path("accessToken");
        orderData = new OrderData(ingredients);
        UserToken userToken = new UserToken(accessToken);
        ValidatableResponse order = orderRequests.createOrderWithAuthorization(orderData, userToken);
        order.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderNoAuthorizationNoIngredientsTest() {
        orderData = new OrderData(null);
        ValidatableResponse order = orderRequests.createOrderWithoutAuthorization(orderData);
        order.assertThat()
                .statusCode(400)
                .and()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthorizationAndWithoutIngredientsTest() {
        UserData userData = new UserData(userRegistration.getEmail(), userRegistration.getPassword());
        ValidatableResponse login = userRequests.authorizationUser(userData);
        login.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = login.extract().path("accessToken");
        UserToken userToken = new UserToken(accessToken);
        OrderData orderData = new OrderData(null);
        ValidatableResponse order = orderRequests.createOrderWithAuthorization(orderData, userToken);
        order.assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хешем ингредиентов")
    public void createOrderWithAuthorizationAndIncorrectHashIngredients() {
        UserData userData = new UserData(userRegistration.getEmail(), userRegistration.getPassword());
        ValidatableResponse login = userRequests.authorizationUser(userData);
        login.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = login.extract().path("accessToken");

        UserToken userToken = new UserToken(accessToken);
        OrderData orderData = new OrderData(ingredients);
        ingredients.set(0, "123457890123456");
        ValidatableResponse order = orderRequests.createOrderWithAuthorization(orderData, userToken);
        order.assertThat().
                statusCode(500);

    }
}
