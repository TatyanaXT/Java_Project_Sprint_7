import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scooter.Courier;
import org.scooter.CourierActions;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private int courierId;
    private final String createWithoutParamError = "Недостаточно данных для создания учетной записи";

    CourierActions actions = new CourierActions();


    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка, что:\n" +
            "- курьера можно создать;\n" +
            "- чтобы создать курьера, нужно передать в ручку все обязательные поля;\n" +
            "- запрос возвращает правильный код ответа 201;\n" +
            "- успешный запрос возвращает ok: true.")
    public void createCourierWithAllParametersTest() {
        String login = actions.generateCourierLogin();
        Courier courier = new Courier(login, "str0nGPasw0rd", "Курьер");

        Response response = actions.createCourier(courier);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);

        courierId = actions.getCourierId(courier);
    }

    @Test
    @DisplayName("Создание с имеющимся логином")
    @Description("Проверка, что если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createCourierDublicateLoginTest() {
        String createDublicateError = "Этот логин уже используется";
        String login = actions.generateCourierLogin();
        Courier courier = new Courier(login, "str0nGPasw0rd", "Курьер");

        actions.createCourier(courier);
        Response response = actions.createCourier(courier);
        
        response.then().assertThat().body("message", equalTo(createDublicateError))
                .and()
                .statusCode(SC_CONFLICT);

        courierId = actions.getCourierId(courier);
    }


    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что если одного из полей нет (логина), запрос возвращает ошибку")
    public void createCourierWithoutLoginTest() {
        Courier courier = new Courier(null, "str0nGPasw0rd", "Курьер");

        Response response = actions.createCourier(courier);
        response.then().assertThat().body("message", equalTo(createWithoutParamError))
                .and()
                .statusCode(SC_BAD_REQUEST);

        courierId = actions.getCourierId(courier);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что если одного из полей нет (пароля), запрос возвращает ошибку")
    public void createCourierWithoutPasswordTest() {
        String login = actions.generateCourierLogin();
        Courier courier = new Courier(login, null, "Курьер");

        Response response = actions.createCourier(courier);
        response.then().assertThat().body("message", equalTo(createWithoutParamError))
                .and()
                .statusCode(SC_BAD_REQUEST);

        courierId = actions.getCourierId(courier);
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Проверка, что если одного из полей нет (имени), запрос возвращает ошибку")
    public void createCourierWithoutNameTest() {
        String login = actions.generateCourierLogin();
        Courier courier = new Courier(login, "str0nGPasw0rd", null);

        Response response = actions.createCourier(courier);
        response.then().assertThat().body("message", equalTo(createWithoutParamError))
                .and()
                .statusCode(SC_BAD_REQUEST);

        courierId = actions.getCourierId(courier);
    }

    @AfterEach
    public void deleteCourier(){
        if (courierId != 0) {
            actions.deleteCourier(courierId);
        }
    }


}
