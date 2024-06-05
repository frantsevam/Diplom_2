package constants;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;

public class Api {
    public static final String USER_REGISTER = "api/auth/register";
    public static final String USER = "api/auth/user";
    public static final String USER_LOGIN = "/api/auth/login";
    public static final String ORDERS = "/api/orders";
    public static final String URL = "https://stellarburgers.nomoreparties.site";

    public RequestSpecification setUp() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}
