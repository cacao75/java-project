package stream_examples;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 다양한 stream API 이용 방법. (groupingBy, TreeMap, ...)
 */
public class EmployeeAnalysis {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "HR", 4000, 45, "Bavaria", "Munich"),
                new Employee("Bob", "IT", 6000, 28, "Bavaria", "Nuremberg"),
                new Employee("Charlie", "HR", 3500, 33, "Berlin", "Berlin"),
                new Employee("David", "IT", 7000, 50, "Berlin", "Berlin"),
                new Employee("Eve", "Finance", 5000, 38, "Hesse", "Frankfurt")
        );

        // 1. Group by department
        Map<String, List<Employee>> byDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept));
        System.out.println("Employees by department: \n" + byDept);

        // 2. Sum of salaries by department (TreeMap)
        // TreeMap을 사용하는 이유:
        //  - 자동 정렬: 부서명(String)을 키로 하여 자동으로 정렬된 상태를 유지합니다.
        //  - 정렬된 순회: entrySet(), keySet(), values() 등을 통해 정렬된 순서로 데이터를 순회할 수 있습니다.
        //  - 범위 기한 작업: subMap(), headMap(), tailMap() 등의 메서드를 사용하여 특정 범위의 데이터에 쉽게 접근할 수 있습니다.
        // HashMap과의 차이점:
        //  - HashMap은 정렬을 보장하지 않습니다.
        //  - LinkedHashMap은 삽입 순서를 유지하지만, 키의 자연 순서로는 정렬되지 않습니다.
        // 결론:
        // 만약 정렬된 상태가 필요 없다면 HashMap을 사용해도 무방합니다.
        // 하지만 부서별로 정렬된 결과를 보여주는 것이 목적이라면 TreeMap이 적합합니다.
        Map<String, Integer> salarySumByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        TreeMap::new,
                        Collectors.summingInt(Employee::salary)));
        System.out.println("Salary sum by department (sorted): \n" + salarySumByDept);

        // 2-1. 역순
        Map<String, Integer> salarySumByDeptDesc = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        () -> new TreeMap<>(Comparator.reverseOrder()),
                        Collectors.summingInt(Employee::salary)));
        System.out.println("Salary sum by department (descending): \n" + salarySumByDeptDesc);

        // 2-2. 부서명이 짧은 것부터 긴 순으로 정렬
        // 이 경우 TreeMap의 키 중복 판단 기준 때문에, 내부적으로 Comparator.compare(a, b)의 결과가 0이면
        // 두 키를 동일(key collision)하다고 간주하여 기존 값을 덮어씌운다.
        // 즉, Comparator.comparingInt(String::length)만 사용하면 "IT"와 "HR"은 길이 2로 같기 때문에
        // TreeMap은 하나만 저장하고 값을 모두 더한다. (즉 {HR=7500, IT=13000, Finance=5000} 이 아닌 {HR=20500, Finance=5000}
        // 로 결과가 나온다.
        Map<String, Integer> salarySumByDeptLength = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        () -> new TreeMap<>(Comparator.comparingInt(String::length)),
                        Collectors.summingInt(Employee::salary)));
        System.out.println("Salary sum by department (sorted by dept name length): \n" + salarySumByDeptLength);

        // 2-3. 길이가 같으면 알파벳순으로 정렬
        Map<String, Integer> salarySumByCustomOrder = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        () -> new TreeMap<>(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder())),
                        Collectors.summingInt(Employee::salary)));
        System.out.println("Salary sum by department (length, then alphabetical): \n" + salarySumByCustomOrder);

        // 3. Partition by pass/fail marks
        Map<Boolean, List<Employee>> passFail = employees.stream()
                .collect(Collectors.partitioningBy(e -> e.marks() >= 33));
        System.out.println("Pass/Fail Partition: \n" + passFail);

        // 4. Group by state and then by city
        Map<String, Map<String, List<Employee>>> byStateAndCity = employees.stream()
                .collect(Collectors.groupingBy(Employee::state,
                        Collectors.groupingBy(Employee::city)));
        System.out.println("Employees grouped by state and city: \n" + byStateAndCity);
    }
}


record Employee(String name, String dept, int salary, int marks, String state, String city) {

    @Override
    public String toString() {
        return name + " (" + dept + ", " + salary + ", " + marks + ", " + state + "/" + city + ")";
    }
}
