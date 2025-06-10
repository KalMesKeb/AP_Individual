import java.io.*;
import java.net.*;

public class SumServer {
    public static void main(String[] args) {
        final int PORT = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for a connection...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            int num1 = input.readInt();
            int num2 = input.readInt();
            int sum = num1 + num2;

            System.out.println("Received numbers: " + num1 + " and " + num2);
            System.out.println("Sending back the sum: " + sum);

            output.writeInt(sum);

            socket.close();
            System.out.println("Connection closed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
