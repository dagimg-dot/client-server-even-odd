import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class App {
    private static final int SERVER_PORT = 5173;

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = runServer();

        while (true) {
            DatagramPacket packet = receivePacket(socket);

            String data = extractData(packet);

            System.out.println("Received request from client: " + data);

            try {
                int number = parseInt(data);
                String response = evenOrOdd(number);
                sendResponse(response, socket, packet);
            } catch (NumberFormatException e) {
                sendError("Invalid input", socket, packet);
            }
        }
    }

    public static DatagramSocket runServer() throws SocketException {
        DatagramSocket socket = new DatagramSocket(SERVER_PORT);
        System.out.println("Server listening on port " + SERVER_PORT);
        return socket;
    }

    public static DatagramPacket receivePacket(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }

    public static String extractData(DatagramPacket packet) {
        return new String(packet.getData(), 0, packet.getLength());
    }

    public static int parseInt(String data) throws NumberFormatException {
        return Integer.parseInt(data);
    }

    public static String evenOrOdd(int number) {
        if (number % 2 == 0) {
            return "Even";
        } else {
            return "Odd";
        }
    }

    public static void sendResponse(String response, DatagramSocket socket, DatagramPacket packet) throws IOException {
        byte[] responseData = response.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                packet.getAddress(), packet.getPort());
        socket.send(responsePacket);
    }

    public static void sendError(String errorMessage, DatagramSocket socket, DatagramPacket packet) throws IOException {
        System.out.println("Error: " + errorMessage);
        sendResponse(errorMessage, socket, packet);
    }
}
