import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.scooter.Courier;
import org.scooter.CourierActions;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    static CourierActions actions = new CourierActions();
    private static int courierId;
    private static final String login = actions.generateCourierLogin();
    private static final String invalidLogin = "ninja";
    private static final String invalidPassword = "qwerty";
    private final String loginWithInvalidDataError = "Учетная запись не найдена";
    private final String loginWithoutDataError = "Недостаточно данных для входа";




    @BeforeAll
    public static void createCourier() {
        Courier courier = new Courier(login, "str0nGPasw0rd", "Курьер");
        actions.createCourier(courier);
    }

    @Test
    @DisplayName("Логин курьера")
    @Description("Проверка, что:\n" +
            "- курьер может авторизоваться;\n" +
            "- для авторизации нужно передать все обязательные поля;\n" +
            "- успешный запрос возвращает id.")
    public void loginCourierWithAllParametersTest() {
        Courier courier = new Courier(login, "str0nGPasw0rd");

        Response response = actions.loginCourier(courier);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);

        courierId = response.path("id");
    }

    @Test
    @DisplayName("Логин курьера с неверным логином")
    @Description("Проверка, что:\n" +
            "- система вернёт ошибку, если неправильно указать логин;\n" +
            "- если авторизоваться под несуществующим пользователем, запрос возвращает ошибку.")
    public void loginCourierWithInvalidLoginTest() {
        Courier courier = new Courier(invalidLogin, "str0nGPasw0rd");

        Response response = actions.loginCourier(courier);
        response.then().assertThat().body("message", equalTo(loginWithInvalidDataError))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Логин курьера с неверным паролем")
    @Description("Проверка, что система вернёт ошибку, если неправильно указать пароль")
    public void loginCourierWithInvalidPasswordTest() {
        Courier courier = new Courier(login, invalidPassword);

        Response response = actions.loginCourier(courier);
        response.then().assertThat().body("message", equalTo(loginWithInvalidDataError))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Логин курьера без логина")
    @Description("Проверка, что если логина нет, запрос возвращает ошибку")
    public void loginCourierWithoutLoginTest() {
        Courier courier = new Courier("", invalidPassword);

        Response response = actions.loginCourier(courier);
        response.then().assertThat().body("message", equalTo(loginWithoutDataError))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    @Description("Проверка, что если пароля нет, запрос возвращает ошибку")
    public void loginCourierWithoutPasswordTest() {
        Courier courier = new Courier(login, "");

        Response response = actions.loginCourier(courier);
        response.then().assertThat().body("message", equalTo(loginWithoutDataError))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }



    @AfterAll
    public static void deleteCourier(){
        actions.deleteCourier(courierId);
    }

}
