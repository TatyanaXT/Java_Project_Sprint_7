import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scooter.Order;
import org.scooter.OrderActions;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {
    OrderActions actions = new OrderActions();

    @Test
    @DisplayName("Ответ по списку заказов является списком")
    @Description("Проверка, что в тело ответа возвращается список заказов.")

    public void orderListInResponse(){
        Order order = new Order();
        Response response = actions.getOrders(order);
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

}
