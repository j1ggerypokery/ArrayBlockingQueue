import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static Thread texting;

    public static void main(String[] args) throws InterruptedException {
        texting = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });

        texting.start();

        Thread a = textGet(queueA, 'a');
        Thread b = textGet(queueB, 'b');
        Thread c = textGet(queueC, 'c');
        a.start();
        b.start();
        c.start();
        a.join();
        b.join();
        c.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread textGet(BlockingQueue<String> queue, char letter) {

        return new Thread(() -> {

            try {
                int max = max(queue, letter);
                System.out.println("Наибольшее число " + letter + " - " + max);
            } catch (InterruptedException e) {
          }

        });

    }

    public static int max(BlockingQueue<String> queue, char letter) throws InterruptedException {
        int max = 0;
        int count = 0;
     String texts;
            while (texting.isAlive()) {
                texts = queue.take();
                for (char abc : texts.toCharArray()) {
                    if (abc == letter) count++;
                }
                if (count > max) {
                    max = count;
                    count = 0;
                }
            }
        return max;
    }

}