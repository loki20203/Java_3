import java.util.concurrent.*;
import java.util.*;

// Клас для перевірки чисел на простоту
class PrimeCheckerTask implements Callable<Boolean> {
    private final int number;

    public PrimeCheckerTask(int number) {
        this.number = number;
    }

    @Override
    public Boolean call() {
        return isPrime(number);
    }

    private boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}

public class PrimeCheckerApp {
    public static void main(String[] args) {
        // Масив чисел для перевірки
        int[] numbers = {2, 3, 4, 5, 6, 7, 11, 13, 16, 17, 18, 19, 20, 23, 29, 31, 37, 41, 43, 47, 50};

        // Конкурентна мапа для зберігання результатів
        ConcurrentHashMap<Integer, Boolean> results = new ConcurrentHashMap<>();

        // Створення пулу потоків
        ExecutorService executor = Executors.newFixedThreadPool(4); // 4 потоки

        try {
            // Список задач для виконання
            List<Future<Boolean>> futures = new ArrayList<>();

            for (int number : numbers) {
                Future<Boolean> future = executor.submit(new PrimeCheckerTask(number));
                futures.add(future);
            }

            // Обробка результатів
            for (int i = 0; i < numbers.length; i++) {
                int number = numbers[i];
                try {
                    boolean isPrime = futures.get(i).get(); // Очікуємо результат виконання
                    results.put(number, isPrime);
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Помилка під час обробки числа " + number + ": " + e.getMessage());
                }
            }

        } finally {
            executor.shutdown(); // Завершення роботи пулу
        }

        // Вивід результатів
        System.out.println("Результати перевірки чисел на простоту:");
        results.forEach((number, isPrime) ->
                System.out.println("Число " + number + " є простим: " + isPrime));
    }
}
