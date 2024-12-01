import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Завдання для виконання у потоці
class CheckoutCounter implements Runnable {
    private final int counterId;
    private final String customerName;

    public CheckoutCounter(int counterId, String customerName) {
        this.counterId = counterId;
        this.customerName = customerName;
    }

    @Override
    public void run() {
        System.out.println("Каса " + counterId + ": Початок обслуговування клієнта " + customerName);
        try {
            // Симуляція часу обслуговування
            Thread.sleep((long) (Math.random() * 5000 + 1000));
        } catch (InterruptedException e) {
            System.out.println("Каса " + counterId + ": Перервано обслуговування клієнта " + customerName);
        }
        System.out.println("Каса " + counterId + ": Завершено обслуговування клієнта " + customerName);
    }
}

public class SupermarketSimulation {
    public static void main(String[] args) {
        // Імітація клієнтів
        String[] customers = {"Олександр", "Марія", "Іван", "Олена", "Дмитро", "Катерина"};

        // Створення пулу потоків
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 каси

        System.out.println("Супермаркет відкрито! Починаємо обслуговування клієнтів.");

        // Додання завдань до пулу потоків
        for (int i = 0; i < customers.length; i++) {
            executor.submit(new CheckoutCounter(i + 1, customers[i]));
        }

        // Завершення роботи пулу
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Очікування завершення всіх завдань
        }

        System.out.println("Супермаркет закрито! Усі клієнти обслуговані.");
    }
}
