import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    public static String ModifyString(String str) {
        StringBuilder modifiedString = new StringBuilder();

        int cnt = 0;

        for (int i = 0; i < str.length(); i++) {

            char ch = str.charAt(i);
            if (ch == '1') {
                cnt++;
                modifiedString.append(ch);
                if (cnt == 5) {
                    modifiedString.append('0');
                    cnt = 0;
                }
            } else {
                modifiedString.append(ch);

            }
        }

        return modifiedString.toString();
    }

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 5000);
        // Socket s = new Socket("10.33.18.6", 14465);
        System.out.println("Client Connected at server Handshaking port " + s.getPort());
        System.out.println("Client’s communication port " + s.getLocalPort());

        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        DataInputStream input = new DataInputStream(s.getInputStream());

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        String str = "";
        String str2 = "";
        while (true) {

            // Send message to the server

            System.out.print("Client: ");
            str = read.readLine();

            String modifiedString = ModifyString(str);
            output.writeUTF(modifiedString);
            System.out.println("Sent to server: " + modifiedString);
            // Exit if "stop" is entered
            if (str.equals("stop")) {
                break;
            }

            // Receive server's response
            str2 = input.readUTF();
            System.out.println("Server : " + str2);

            // Exit if server sends "stop"
            if (str2.equals("stop")) {
                break;
            }
        }
        output.close();
        input.close();
        read.close();
        s.close();
    }
}
