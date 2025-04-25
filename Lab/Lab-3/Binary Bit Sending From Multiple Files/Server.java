import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static String removeBitStuffing(String str) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            sb.append(ch);
            if (ch == '1') {
                cnt++;
                if (cnt == 5) {
                    i++; // Skip the next '0' (bit stuffing)
                    cnt = 0;
                }
            } else {
                cnt = 0;
            }
        }
        return sb.toString();
    }

    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i + 7 < binary.length(); i += 8) {
            String byteStr = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteStr, 2);
            text.append((char) charCode);
        }
        return text.toString();
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(14465);
             Socket client = ss.accept();
             DataInputStream input = new DataInputStream(client.getInputStream());
             DataOutputStream output = new DataOutputStream(client.getOutputStream())) {

            System.out.println("Server listening on port: " + ss.getLocalPort());
            System.out.println("Client connected from: " + client.getInetAddress());

            // Read the number of files sent by the client
            int numFiles = input.readInt();
            System.out.println("Number of files to process: " + numFiles);

            for (int i = 0; i < numFiles; i++) {
                try {
                    String received = input.readUTF();
                    System.out.println("Received (file " + (i+1) + "): " + received);

                    // Remove bit stuffing and convert to text
                    String str = removeBitStuffing(received);
                    String decoded = binaryToText(str);
                    System.out.println("Decoded Text (file " + (i+1) + "): " + decoded);

                    // Send back the decoded text to the client
                    output.writeUTF("Decoded: " + decoded);
                } catch (EOFException eof) {
                    System.out.println("Client closed the connection unexpectedly.");
                    break;
                }
            }

            // Listen for the "stop" message to close the connection
            String stopMessage = input.readUTF();
            if (stopMessage.equalsIgnoreCase("stop")) {
                System.out.println("Client disconnected.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
