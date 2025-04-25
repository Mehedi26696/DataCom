import java.io.*;
import java.net.Socket;

public class Client {

    // Bit stuffing: insert a '0' after five consecutive '1's
    public static String ModifyString(String str) {
        StringBuilder modifiedString = new StringBuilder();
        int cnt = 0;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            modifiedString.append(ch);
            if (ch == '1') {
                cnt++;
                if (cnt == 5) {
                    modifiedString.append('0'); // Stuff '0'
                    cnt = 0;
                }
            } else {
                cnt = 0;
            }
        }
        return modifiedString.toString();
    }

    // Convert plain text to binary (8 bits per character)
    public static String textToBinary(String text) {
        StringBuilder binaryString = new StringBuilder();
        for (char c : text.toCharArray()) {
            String binaryChar = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            binaryString.append(binaryChar);
        }
        return binaryString.toString();
    }

    public static void main(String[] args) {
        try (
                Socket s = new Socket("localhost", 14465);
                DataOutputStream output = new DataOutputStream(s.getOutputStream());
                DataInputStream input = new DataInputStream(s.getInputStream());
                BufferedReader read = new BufferedReader(new FileReader("Input.txt"))) {
            System.out.println("Connected to server at port " + s.getPort());

            String str;
            while ((str = read.readLine()) != null) {
                System.out.println("Client: " + str);

                // Convert to binary and apply bit stuffing
                String binaryString = textToBinary(str);
                String modifiedString = ModifyString(binaryString);

                // Send stuffed binary to server
                output.writeUTF(modifiedString);

                // Read response from server
                String response = input.readUTF();
                System.out.println("Server: " + response);
            }

            // Notify server that transmission is done
            output.writeUTF("stop");
            System.out.println("All lines sent. Connection closed.");

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}
