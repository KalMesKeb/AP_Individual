import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SumClient {
    public static void main(String[] args) {
        final String SERVER = "localhost";
        final int PORT = 5000;

        try (Socket socket = new Socket(SERVER, PORT)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter first number: ");
            int num1 = scanner.nextInt();

            System.out.print("Enter second number: ");
            int num2 = scanner.nextInt();

            output.writeInt(num1);
            output.writeInt(num2);

            int sum = input.readInt();
            System.out.println("Sum received from server: " + sum);

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
