import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Клас складу товарів
class Inventory {
    private int items;

    public Inventory(int initialItems) {
        this.items = initialItems;
    }

    // Синхронізований метод для зняття товару
    public synchronized boolean takeItem(int counterId, String customerName) {
        if (items > 0) {
            System.out.println("Каса " + counterId + ": Клієнт " + customerName + " отримує товар. Залишок: " + (items - 1));
            items--;
            return true;
        } else {
            System.out.println("Каса " + counterId + ": Товар для клієнта " + customerName + " закінчився!");
            return false;
        }
    }

    public int getItems() {
        return items;
    }
}

// Завдання для виконання у потоці
class CheckoutCounter implements Runnable {
    private final int counterId;
    private final String customerName;
    private final Inventory inventory;

    public CheckoutCounter(int counterId, String customerName, Inventory inventory) {
        this.counterId = counterId;
        this.customerName = customerName;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        System.out.println("Каса " + counterId + ": Початок обслуговування клієнта " + customerName);
        try {
            // Симуляція обслуговування клієнта
            Thread.sleep((long) (Math.random() * 3000 + 1000));

            // Спроба взяти товар зі складу
            synchronized (inventory) {
                boolean success = inventory.takeItem(counterId, customerName);
                if (!success) {
                    System.out.println("Каса " + counterId + ": Клієнт " + customerName + " йде без товару.");
                }
            }

        } catch (InterruptedException e) {
            System.out.println("Каса " + counterId + ": Перервано обслуговування клієнта " + customerName);
        }
        System.out.println("Каса " + counterId + ": Завершено обслуговування клієнта " + customerName);
    }
}

public class SupermarketSimulation {
    public static void main(String[] args) {
        // Ініціалізація складу
        Inventory inventory = new Inventory(5); // 5 одиниць товару на складі

        // Імітація клієнтів
        String[] customers = {"Олександр", "Марія", "Іван", "Олена", "Дмитро", "Катерина"};

        // Створення пулу потоків
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 каси

        System.out.println("Супермаркет відкрито! Починаємо обслуговування клієнтів.");

        // Додання завдань до пулу потоків
        for (int i = 0; i < customers.length; i++) {
            executor.submit(new CheckoutCounter(i + 1, customers[i], inventory));
        }

        // Завершення роботи пулу
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Очікування завершення всіх завдань
        }

        System.out.println("Супермаркет закрито! Усі клієнти обслуговані.");
    }
}
