import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList Demo.
 */
public class ArrayListDemo {

	public static void main(String[] argv) {
		List<LocalDate> editions = new ArrayList<>();

		// Add lots of elements to the ArrayList...
		editions.add(LocalDate.of(2001, 06, 01));
		editions.add(LocalDate.of(2004, 06, 02));
		editions.add(LocalDate.of(2014, 06, 20));

		// Use normal 'for' loop for simpler access
		System.out.println("Retrieving by Iterable:");
		for (LocalDate dt : editions) {
			System.out.println("Edition " + dt);
		}
	}
}
