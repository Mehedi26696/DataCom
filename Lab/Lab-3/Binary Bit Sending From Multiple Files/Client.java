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
                DataInputStream input = new DataInputStream(s.getInputStream())) {

            System.out.println("Connected to server at port " + s.getPort());

            // List of files to send
           // String[] fileNames = {"InInput1.txt", "Input2.txt"}; 
            
            String[] fileNames = {"InputFiles/input1.txt","InputFiles/input2.txt"};// Add more files as needed

            // Notify the server of the number of files
            output.writeInt(fileNames.length);

            // Loop through each file, read its content, modify and send
            for (String fileName : fileNames) {
                try (BufferedReader read = new BufferedReader(new FileReader(fileName))) {
                    String str;
                    StringBuilder fileContent = new StringBuilder();

                    while ((str = read.readLine()) != null) {
                        fileContent.append(str).append("\n");
                    }

                    System.out.println("Sending file: " + fileName);
                    
                    // Convert to binary and apply bit stuffing
                    String binaryString = textToBinary(fileContent.toString());
                    String modifiedString = ModifyString(binaryString);

                    // Send the file's binary data
                    output.writeUTF(modifiedString);

                    // Receive the response from the server
                    String response = input.readUTF();
                    System.out.println("Server: " + response);
                } catch (IOException e) {
                    System.err.println("Error reading file " + fileName + ": " + e.getMessage());
                }
            }

            // Notify the server that transmission is done
            output.writeUTF("stop");
            System.out.println("All files sent. Connection closed.");

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}
