package stream_examples;

import static stream_examples.Dish.menu;

import java.util.Arrays;
import java.util.List;

/**
 * 다양한 stream filter 예제
 */
public class Filtering {

    public static void main(String... args) {
        System.out.println("\n** Filtering with a predicate");
        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::vegetarian)
                .toList();
        vegetarianMenu.forEach(System.out::println);

        // 고유 요소로 거름
        System.out.println("\n** Filtering unique elements:");
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        // 스트림 슬라이스. 칼로리 값을 기준으로 리스트를 오름차순 정렬
        List<Dish> specialMenu = Arrays.asList(
                new Dish("season fruit", true, 120, DishType.OTHER),
                new Dish("prawns", false, 300, DishType.FISH),
                new Dish("rice", true, 350, DishType.OTHER),
                new Dish("chicken", false, 400, DishType.MEAT),
                new Dish("french fries", true, 530, DishType.OTHER));

        System.out.println("\n** Filtered sorted menu:");
        List<Dish> filteredMenu = specialMenu.stream()
                .filter(dish -> dish.calories() < 320)
                .toList();
        filteredMenu.forEach(System.out::println);

        System.out.println("\n** Sorted menu sliced with takeWhile():");
        List<Dish> slicedMenu1 = specialMenu.stream()
                .takeWhile(dish -> dish.calories() < 320)
                .toList();
        slicedMenu1.forEach(System.out::println);

        System.out.println("\n** Sorted menu sliced with dropWhile():");
        List<Dish> slicedMenu2 = specialMenu.stream()
                .dropWhile(dish -> dish.calories() < 320)
                .toList();
        slicedMenu2.forEach(System.out::println);

        // 스트림 연결
        List<Dish> dishesLimit3 = menu.stream()
                .filter(d -> d.calories() > 300)
                .limit(3)
                .toList();
        System.out.println("\n** Truncating a stream:");
        dishesLimit3.forEach(System.out::println);

        // 요소 생략
        List<Dish> dishesSkip2 = menu.stream()
                .filter(d -> d.calories() > 300)
                .skip(2)
                .toList();
        System.out.println("\n** Skipping elements:");
        dishesSkip2.forEach(System.out::println);
    }
}


enum DishType {
    MEAT,
    FISH,
    OTHER
}


record Dish(String name, boolean vegetarian, int calories, DishType type) {

    @Override
    public String toString() {
        return name;
    }

    public static final List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, DishType.MEAT),
            new Dish("beef", false, 700, DishType.MEAT),
            new Dish("chicken", false, 400, DishType.MEAT),
            new Dish("french fries", true, 530, DishType.OTHER),
            new Dish("rice", true, 350, DishType.OTHER),
            new Dish("season fruit", true, 120, DishType.OTHER),
            new Dish("pizza", true, 550, DishType.OTHER),
            new Dish("prawns", false, 400, DishType.FISH),
            new Dish("salmon", false, 450, DishType.FISH)
    );
}
