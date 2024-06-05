package usertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserGenerator;
import user.UserRegistration;
import user.UserRequests;

import static org.hamcrest.CoreMatchers.equalTo;


public class CreateUserTest {
    private UserRequests userRequests;
    private UserRegistration userRegistration;
    private String accessToken;

    @Before
    public void setUp() {
        userRequests = new UserRequests();
        userRegistration = UserGenerator.getUserRandom();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequests.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        ValidatableResponse user = userRequests.createUser(userRegistration);
        user.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = user.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createdUserAlreadyRegisteredTest() {
        ValidatableResponse userFirst = userRequests.createUser(userRegistration);
        ValidatableResponse userSecond = userRequests.createUser(userRegistration);
        userSecond.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
        accessToken = userFirst.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    public void createUserWithoutName() {
        userRegistration.setName(null);
        ValidatableResponse user = userRequests.createUser(userRegistration);
        user.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    public void createUserWithoutEmail() {
        userRegistration.setEmail(null);
        ValidatableResponse user = userRequests.createUser(userRegistration);
        user.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    public void createUserWithoutPassword() {
        userRegistration.setPassword(null);
        ValidatableResponse user = userRequests.createUser(userRegistration);
        user.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
