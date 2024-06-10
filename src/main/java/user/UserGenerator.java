package user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static UserRegistration getUserRandom() {
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        return new UserRegistration(name, email, password);
    }
}
