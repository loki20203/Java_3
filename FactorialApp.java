import java.util.concurrent.*;
import java.util.*;

// Завдання для обчислення факторіалу
class FactorialTask implements Callable<Long> {
    private final int number;

    public FactorialTask(int number) {
        this.number = number;
    }

    @Override
    public Long call() {
        return calculateFactorial(number);
    }

    private Long calculateFactorial(int n) {
        if (n == 0 || n == 1) return 1L;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}

public class FactorialApp {
    public static void main(String[] args) {
        // Список чисел для обчислення факторіалів
        List<Integer> numbers = Arrays.asList(5, 10, 15, 20, 25);

        // Список для збереження Future-об'єктів
        List<Future<Long>> futures = new ArrayList<>();

        // Створення пулу потоків
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 потоки

        try {
            // Додавання завдань до пулу
            for (int number : numbers) {
                Callable<Long> task = new FactorialTask(number);
                Future<Long> future = executor.submit(task);
                futures.add(future);
            }

            // Отримання та виведення результатів
            for (int i = 0; i < numbers.size(); i++) {
                int number = numbers.get(i);
                try {
                    Long result = futures.get(i).get(); // Очікування результату обчислення
                    System.out.println("Факторіал числа " + number + " дорівнює: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Помилка під час обчислення факторіалу для числа " + number + ": " + e.getMessage());
                }
            }

        } finally {
            executor.shutdown(); // Завершення роботи пулу
        }
    }
}
