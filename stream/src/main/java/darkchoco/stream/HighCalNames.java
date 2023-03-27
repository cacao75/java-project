package darkchoco.stream;

import static darkchoco.stream.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class HighCalNames {

    public static void main(String[] args) {
        List<String> names = menu.stream()
                .filter(dish -> {
//                    System.out.println("filtering " + dish.getName());
                    return dish.getCalories() > 300;
                })
//                .map(dish -> {
//                    System.out.println("mapping " + dish.getName());
//                    return dish.getName();
//                })
                .map(Dish::getName)  // Method Reference
                .limit(3)
                .collect(toList());

        System.out.println(names);
    }
}
