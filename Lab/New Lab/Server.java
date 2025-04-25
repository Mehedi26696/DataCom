import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = input.readLine()) != null) {
                try {
                    int number = Integer.parseInt(line);
                    if (isPrime(number)) {
                        output.println(number + " is a prime number.");
                    } else {
                        output.println(number + " is not a prime number.");
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input.");
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPrime(int n) {
        if (n <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }
}

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started... Waiting for clients...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");
            new ClientHandler(clientSocket).start(); // Thread starts
        }
    }
}
