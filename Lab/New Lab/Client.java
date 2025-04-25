import java.io.*;
import java.net.*;

public class  Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Enter numbers to check for primality (type 'exit' to quit):");
            String number;

            while (!(number = input.readLine()).equalsIgnoreCase("exit")) {
                output.println(number);
                String response = serverResponse.readLine();
                System.out.println("Server says: " + response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
