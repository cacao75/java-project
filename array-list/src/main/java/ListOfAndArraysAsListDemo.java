import java.util.Arrays;
import java.util.List;

/**
 * List.of() and Arrays.asList() Demo.
 * <p>
 * List.of()
 *   - return 된 list는 변경 불가(immutable)
 *   - Fixed-size
 *   - NOT allow null element
 * <p>
 * Arrays.asList()
 *   - return 된 list는 element 변경 가능
 *   - The list is backed by the original array. 즉 list element 변경은 해당 array에 바로 적용됨 (그 반대도 마찬가지)
 *   - Fixed-size
 *   - Allow null element
 */
public class ListOfAndArraysAsListDemo {

    public static void main(String[] args) {

        String[] colorsArray = {"Red", "Green", "Blue"};

        // List.of()
        List<String> colors = List.of(colorsArray);

        // Array를 변경한다. (하지만 해당 List는 변경되지 않을 것이다)
        colorsArray[0] = "Yellow";
        System.out.println(Arrays.toString(colorsArray));

        // Accessing elements in the original array
        System.out.println(colors.get(0).equals(colorsArray[0]));  // Output: false
        System.out.println(colors.get(1).equals(colorsArray[1]));  // Output: true
        System.out.println(colors.get(2).equals(colorsArray[2]));  // Output: true

        // Arrays.asList()
        List<String> colors1 = Arrays.asList(colorsArray);

        // List를 변경한다. (원래 Array도 변경될 것이다)
        colors1.set(0, "Black");
        System.out.println(Arrays.toString(colorsArray));

        // Accessing elements in the original array
        System.out.println(colors1.get(0).equals(colorsArray[0]));  // Output: true
        System.out.println(colors1.get(1).equals(colorsArray[1]));  // Output: true
        System.out.println(colors1.get(2).equals(colorsArray[2]));  // Output: true

        // Array를 변경한다. (해당 List도 변경될 것이다)
        colorsArray[0] = "Yellow";
        System.out.println(colors1);
    }
}
