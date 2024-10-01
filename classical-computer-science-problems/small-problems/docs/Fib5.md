`IntStream.generate()`는 `IntStream`을 생성할 때 사용되는 메서드로, 주어진 람다 표현식에 따라 무한 스트림을 생성합니다. 이 코드를 통해, `IntStream`은 매번 값이 요청될 때마다 람다 표현식의 결과를 반환하게 됩니다.

이해를 돕기 위해 코드를 자세히 설명하겠습니다.

```java
return IntStream.generate(() -> {
    int oldLast = last;
    last = next;
    next = oldLast + next;
    return oldLast;
});
```

### 1. `IntStream.generate()`
- `generate()`는 무한 스트림을 생성합니다. 무한 스트림이라는 것은 정해진 범위가 없고, 사용자가 명시적으로 제한하지 않으면 계속해서 값을 생성할 수 있는 스트림입니다.
- 이 메서드는 매번 호출될 때마다 스트림에 값을 공급할 `Supplier`를 받아서 값을 생성합니다. 이 경우, `Supplier`는 람다 표현식 `() -> { ... }`입니다.

### 2. 람다 표현식
람다 표현식 내부의 동작을 세부적으로 살펴보겠습니다.

```java
int oldLast = last;  // 현재의 last 값을 oldLast에 저장
last = next;         // last 값을 현재의 next 값으로 업데이트
next = oldLast + next;  // next 값을 이전 last와 현재 next의 합으로 업데이트
return oldLast;  // 원래 last 값을 반환
```

- **`int oldLast = last;`**: 현재의 `last` 값을 `oldLast`라는 변수에 저장합니다. 이 값은 스트림에서 반환될 값이 됩니다.
- **`last = next;`**: `last` 값을 다음 피보나치 수인 `next`로 업데이트합니다. 즉, 피보나치 수열에서 현재 값이 `next`로 변경됩니다.
- **`next = oldLast + next;`**: 피보나치 수열의 다음 값을 계산하기 위해, 원래의 `last` 값(즉, `oldLast`)과 현재의 `next` 값을 더해서 `next` 값을 업데이트합니다. 이렇게 해서 수열의 다음 값을 준비합니다.
- **`return oldLast;`**: 피보나치 수열의 현재 값을 반환합니다. 이 값이 스트림에서 소비됩니다.

### 3. 스트림의 동작
- `IntStream.generate()`에 의해 무한 스트림이 생성되며, 각 요소는 피보나치 수열의 다음 값으로 계산됩니다.
- `fib5.stream().limit(41)`에서 `limit(41)`은 스트림에서 처음 41개의 요소만 가져오도록 제한합니다. 피보나치 수열의 처음 41개 값을 생성하고 출력하는 코드입니다.

### 전체 흐름
1. `last`는 처음에 0, `next`는 처음에 1로 초기화됩니다.
2. `IntStream.generate()`는 무한히 피보나치 수를 생성하지만, `limit(41)`에 의해 첫 41개의 값만 스트림에 포함됩니다.
3. 피보나치 수는 `last`와 `next` 값을 순차적으로 업데이트하여 계산됩니다.
4. 각 피보나치 수는 `oldLast`를 통해 반환되고 출력됩니다.

결과적으로, 이 코드는 피보나치 수열의 처음 41개 숫자를 생성하여 출력하는 것입니다.

그리고 `forEachOrdered(System.out::println)`는 스트림의 각 요소를 순차적으로 처리하면서, 그 값을 출력하는 역할을 합니다.   
여기서 `forEachOrdered`와 `System.out::println` 각각의 역할을 설명하겠습니다.

### 1. `forEachOrdered()`
- **`forEachOrdered()`**는 스트림의 각 요소를 처리하는 터미널 연산입니다. 스트림이 처리하는 데이터를 순차적으로 소비하면서, 지정된 작업을 각 요소에 대해 수행합니다.
- `forEachOrdered()`는 특히 병렬 스트림에서 **순서를 보장**하면서 각 요소를 처리합니다. 즉, 스트림이 병렬로 처리되는 경우에도 원래 스트림의 순서를 유지하면서 데이터를 처리할 수 있도록 합니다.
- 이 코드에서는 스트림이 병렬로 동작하지 않으므로, 기본적으로 순차적 처리와 동일하게 동작하지만, `forEachOrdered()`는 이 순서를 확실히 보장합니다.

### 2. `System.out::println`
- **`System.out::println`**은 자바에서 표준 출력 스트림(`System.out`)에 텍스트를 출력하는 메서드입니다. `System.out::println`이라는 형태는 **메서드 참조(method reference)**입니다.
- `System.out::println`은 각 스트림 요소를 전달받아 출력하는 역할을 합니다. 즉, 스트림에서 생성된 피보나치 수열의 값들을 하나씩 출력합니다.

### 결합된 동작: `forEachOrdered(System.out::println)`
- **`forEachOrdered(System.out::println)`**는 스트림의 각 요소를 순차적으로 출력합니다. 스트림에서 생성된 피보나치 수의 값들이 차례대로 `System.out::println`에 의해 콘솔에 출력됩니다.

### 병렬 스트림의 예시
`forEachOrdered()`와 `forEach()`의 차이를 잘 이해하기 위해 병렬 스트림을 예로 들어보겠습니다.

```java
IntStream.range(0, 10).parallel().forEach(System.out::println);
```

- 이 코드는 병렬로 실행되기 때문에 출력 순서가 무작위일 수 있습니다.
- 하지만 `forEachOrdered()`를 사용하면, 병렬로 실행되더라도 원래 스트림 순서대로 처리됩니다.

```java
IntStream.range(0, 10).parallel().forEachOrdered(System.out::println);
```

- 이렇게 하면 출력되는 값이 항상 0부터 9까지 순서대로 출력됩니다.

### 요약
`forEachOrdered(System.out::println)`는 스트림에서 생성된 피보나치 수들을 순차적으로(원래 순서를 유지하며) 콘솔에 출력하는 코드입니다. 이 경우 순차 스트림이므로 `forEach()`와 동일하게 동작하지만, 병렬 스트림에서도 순서가 보장된다는 점이 `forEachOrdered()`의 특징입니다.