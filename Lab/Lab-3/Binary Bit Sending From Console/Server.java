import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static String modifyString(String str) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            sb.append(ch);
            if (ch == '1') {
                cnt++;
                if (cnt == 5) {
                    i++;
                    cnt = 0;
                }
            } else {
                cnt = 0;
            }
        }
        return sb.toString();

    }

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server is connected at port no: " + ss.getLocalPort());
        System.out.println("Waiting for the client\n");

        Socket s = ss.accept();
        System.out.println("Client request is accepted at port no: " + s.getPort());
        System.out.println("Server’s Communication Port: " + s.getLocalPort());

        DataInputStream input = new DataInputStream(s.getInputStream());
        DataOutputStream output = new DataOutputStream(s.getOutputStream());

        while (true) {
            String receivedMessage = input.readUTF();
            System.out.println("Received from client: " + receivedMessage);

            if ("stop".equalsIgnoreCase(receivedMessage)) {
                System.out.println("Stopping communication as requested by client.");
                break;
            }

            String modifiedMessage = modifyString(receivedMessage);
            output.writeUTF(modifiedMessage);
            System.out.println("Sent to client: " + modifiedMessage);
        }
    }
}
