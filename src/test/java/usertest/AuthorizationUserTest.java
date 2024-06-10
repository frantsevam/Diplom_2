package usertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserData;
import user.UserGenerator;
import user.UserRegistration;
import user.UserRequests;

import static org.hamcrest.CoreMatchers.equalTo;

public class AuthorizationUserTest {
    private UserRequests userRequests;
    private UserRegistration userRegistration;
    private String accessToken;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
        userRegistration = UserGenerator.getUserRandom();
        userRequests.createUser(userRegistration);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequests.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void AuthorizationExistingUserTest() {
        UserData userData = new UserData(userRegistration.getEmail(), userRegistration.getPassword());
        ValidatableResponse user = userRequests.authorizationUser(userData);
        user.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = user.extract().path("accessToken");
    }

    @Test
    @DisplayName("Авторизация с неверным password")
    public void AuthorizationWithWrongPasswordTest() {
        UserData userData = new UserData(userRegistration.getEmail(), "password");
        ValidatableResponse user = userRequests.authorizationUser(userData);
        user.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void AuthorizationWithWrongEmailTest() {
        UserData userData = new UserData("email", userRegistration.getPassword());
        ValidatableResponse user = userRequests.authorizationUser(userData);
        user.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
