package darkchoco.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatmapExample {

    public static void main( String[] args ) {
        // map
        List<String> myList = Stream.of("Apple", "Banana")
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(myList);

        // flatMap
        List<List<String>> myList1 = new ArrayList<>();
        myList1.add(Arrays.asList("Apple", "Banana"));
        myList1.add(Arrays.asList("Mango", "Grape"));
        System.out.println(myList1);

        List<String> result = myList1.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(result);

        List<String> myList2 = Arrays.asList("Beijing, Tiananmen", "Shanghai Oriental Pearl", "厦门 鼓浪屿");
        myList2.stream().flatMap(item -> Arrays.stream(item.split(" "))).toList().forEach(System.out::println);
        myList2.stream().map(item -> Stream.of(item.split(" "))).forEach(System.out::println);
    }
}
