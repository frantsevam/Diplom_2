package usertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserDataTest {
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
    @DisplayName("Изменение данных с авторизацией")
    public void changeDataUserWithAuthorizationTest() {
        UserData userData = new UserData(userRegistration.getEmail(), userRegistration.getPassword());
        ValidatableResponse user = userRequests.authorizationUser(userData);
        accessToken = user.extract().path("accessToken");
        UserToken userToken = new UserToken(accessToken);
        ValidatableResponse changeData = userRequests.changeUserDataWithAuthorization(UserGenerator.getUserRandom(), userToken);
        changeData.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных без авторизации")
    public void changeDataUserWithoutAuthorizationTest() {
        ValidatableResponse changeData = userRequests.changeUserDataWithoutAuthorization(UserGenerator.getUserRandom());
        changeData.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
