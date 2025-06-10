import java.util.Scanner;

public class SumCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        int num1 = scanner.nextInt();

        System.out.print("Enter second number: ");
        int num2 = scanner.nextInt();

        SumThread sumThread = new SumThread(num1, num2);
        sumThread.start();

        try {
            sumThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int result = sumThread.getResult();
        System.out.println("The sum is: " + result);

        scanner.close();
    }
}

class SumThread extends Thread {
    private int a, b, result;

    public SumThread(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public void run() {
        result = a + b;
    }

    public int getResult() {
        return result;
    }
}
