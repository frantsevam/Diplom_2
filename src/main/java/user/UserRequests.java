package user;

import constants.ApiAndUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserRequests extends ApiAndUrl {

    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserRegistration userRegistration) {
        return given()
                .spec(setUp())
                .body(userRegistration)
                .when()
                .post(USER_REGISTER)
                .then()
                .log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse authorizationUser(UserData userData) {
        return given()
                .spec(setUp())
                .body(userData)
                .when()
                .post(USER_LOGIN)
                .then()
                .log().all();
    }

    @Step("Изменение данных для авторизованного пользователя")
    public ValidatableResponse changeUserDataWithAuthorization(UserRegistration userRegistration, UserToken userToken) {
        return given()
                .spec(setUp())
                .header("Authorization", userToken.getToken())
                .body(userRegistration)
                .when()
                .patch(USER)
                .then()
                .log().all();
    }

    @Step("Изменение данных для неавторизованного пользователя")
    public ValidatableResponse changeUserDataWithoutAuthorization(UserRegistration userRegistration) {
        return given()
                .spec(setUp())
                .body(userRegistration)
                .when()
                .patch(USER)
                .then()
                .log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(setUp())
                .body(accessToken)
                .when()
                .delete(USER)
                .then()
                .log().all();
    }

}
