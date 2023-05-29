import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class App {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5173;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("Enter a number (or 'quit' to exit): ");
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("quit")) {
                        break;
                    }

                    sendRequest(input, socket, serverAddress);

                    String response = receiveResponse(socket);

                    displayResponse(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(String input, DatagramSocket socket, InetAddress serverAddress) throws Exception {
        byte[] requestData = input.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, SERVER_PORT);
        socket.send(requestPacket);
    }

    private static String receiveResponse(DatagramSocket socket) throws Exception {
        byte[] buffer = new byte[1024];
        DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(responsePacket);
        return new String(responsePacket.getData(), 0, responsePacket.getLength());
    }

    private static void displayResponse(String response) {
        System.out.println("\u001B[36mServer response: " + response + "\u001B[0m");
    }
}
