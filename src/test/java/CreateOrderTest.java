import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.scooter.Order;
import org.scooter.OrderActions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {

    OrderActions actions = new OrderActions();

    private static Stream<Arguments> orderDetails() {
        return Stream.of(
                Arguments.of("Петр", "Григорьев", "Ленина, 32", "1", "+79218763245", 1, "2026-02-15", "Спасибо", Arrays.asList("BLACK")),
                Arguments.of("Олег", "Баклажанов", "Мира, 84", "2", "+79990973468", 2, "2026-02-20", "До свидания", Arrays.asList("GREY")),
                Arguments.of("Саня", "Хлебов", "Лесная, 61", "3", "+79531645843", 3, "2026-02-17", "...", Arrays.asList("BLACK", "GREY")),
                Arguments.of("Антон?", "Непомнящий", "Московская, 43", "4", "89119119191", 4, "2026-02-22", "-", Arrays.asList())
        );
    }

    @ParameterizedTest
    @DisplayName("Создание заказов")
    @Description("Проверка, что при создании заказа:\n" +
            "можно указать один из цветов — BLACK или GREY;\n" +
            "можно указать оба цвета;\n" +
            "можно совсем не указывать цвет;\n" +
            "тело ответа содержит track.")
    @MethodSource("orderDetails")
    public void createOrderTest(
            String firstName, String lastName, String address,
            String metroStation, String phone, int rentTime,
            String deliveryDate, String comment, List color) {
        Order order = new Order(
                firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);

        Response response = actions.createOrder(order);
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);

    }
}
