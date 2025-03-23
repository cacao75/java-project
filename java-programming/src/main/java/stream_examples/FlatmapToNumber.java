package stream_examples;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class FlatmapToNumber {

    public static void main(String[] args) {
        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        );

        IntStream intStream = listOfLists.stream()
                .flatMapToInt(list -> list.stream().mapToInt(i -> i));
        intStream.forEach(System.out::println);

        List<long[]> longLists = Arrays.asList(
                new long[]{1L, 2L, 3L},
                new long[]{4L, 5L, 6L},
                new long[]{7L, 8L, 9L}
        );

        LongStream longStream = longLists.stream()
                .flatMapToLong(Arrays::stream);
        longStream.forEach(System.out::println);
    }
}
