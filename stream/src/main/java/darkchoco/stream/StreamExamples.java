package darkchoco.stream;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExamples {

    public static void main(String[] args) {
        // https://stackoverflow.com/questions/409784/whats-the-simplest-way-to-print-a-java-array
        System.out.println("+++ 01. Multiple Each Item in a List by 2\n");

        int[] int_arr_1 = IntStream.range(1, 10)
                .map(i -> i * 2)
                .toArray();
        System.out.println(Arrays.toString(int_arr_1));

        IntStream.range(1, 10).map(i -> i * 2).forEach(System.out::println);

        System.out.println();
        System.out.println("+++ 02. Sum a List of Numbers\n");

        System.out.println(IntStream.range(1, 1000).sum());

        System.out.println();
        System.out.println("+++ 03. Verify if Exists in a String\n");

        List<String> wordList = Arrays.asList("java", "jdk", "spring", "maven");
        String tweet = "This is an example tweet talking about java and maven.";
        if (wordList.stream().anyMatch(tweet::contains))
            System.out.println("The matched word is in.");
        else
            System.out.println("No words are matched.");

        System.out.println();
        System.out.println("+++ 04. Happy Birthday to You!\n");

        IntStream.rangeClosed(1, 4)
                .mapToObj(i -> MessageFormat.format("Happy Birthday {0}", (i == 3) ? "dear JS" : "to You"))
                .forEach(System.out::println);

        System.out.println();
        System.out.println("+++ 05. Filter list of numbers\n");

        Map<Boolean, List<Integer>> passedFailedMap = Stream.of(49, 58, 76, 82, 88, 90)
                .collect(Collectors.partitioningBy(i -> i > 80));
        for (Boolean b : passedFailedMap.keySet()) {
            // https://stackoverflow.com/questions/10168066/how-to-print-out-all-the-elements-of-a-list-in-java
            System.out.println(b + ": " + Arrays.toString(passedFailedMap.get(b).toArray()));
        }

        System.out.println();
        System.out.println("+++ 06. Find minimum (or maximum) in a List\n");

        System.out.println(IntStream.of(14, 35, -7, 46, 98).min().orElseThrow(NoSuchElementException::new));
        System.out.println(Stream.of(14, 35, -7, 46, 98).min(Integer::compare).get());
//        System.out.println(Arrays.asList(14, 35, -7, 46, 98).stream().reduce(Integer::min));
        System.out.println(Stream.of(14, 35, -7, 46, 98).reduce(Integer::min).get());
        System.out.println(Collections.min(Arrays.asList(14, 35, -7, 46, 98)));

        System.out.println();
        System.out.println("+++ 07. Avoid nesting streams\n");

        var list1 = Arrays.asList("apple", "banana", "cherry");
        var list2 = Arrays.asList("orange", "pineapple", "mango");
        var result = Stream.concat(list1.stream(), list2.stream())
                .filter(s -> s.length() > 5)
                .toList();
        for (String s : result)
            System.out.println(s);
    }
}
