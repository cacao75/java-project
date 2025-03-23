package stream_examples;

import java.util.Arrays;
import java.util.List;

public class FlatmapTest {

    public static void main(String[] args) {
        List<Order> orders = Arrays.asList(
                new Order(Arrays.asList("a", "b", "c")),
                new Order(Arrays.asList("d", "e", "f")));

        List<String> allItems = orders.stream()
                .flatMap(order -> order.lineItems().stream())
                .toList();
        System.out.println(allItems);
    }
}

record Order(List<String> lineItems) {

}
