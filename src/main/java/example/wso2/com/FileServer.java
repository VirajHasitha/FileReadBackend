package example.wso2.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private static final int PORT = 8080;
    private static final String FILE_PATH = "/Users/virajwarnakulasinghe/test.pdf";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("File server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                sendFile(clientSocket);

                clientSocket.close();
                System.out.println("Client disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(Socket clientSocket) {
        try {
            File file = new File(FILE_PATH);
            FileInputStream fileInputStream = new FileInputStream(file);

            OutputStream outputStream = clientSocket.getOutputStream();

            //Write the HTTP response headers
            outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
            outputStream.write(("Content-Disposition: attachment; filename=\"" + file.getName() + "\"\r\n").getBytes());
            outputStream.write(("Content-Type: application/pdf\r\n").getBytes());
            outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes());
            outputStream.write("\r\n".getBytes());

            //Write the file content
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
