package ChatServer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final ServerSocket serverSocket; // точка подключения
    private final AuthService authService; // сервис для проверки аутентификации
    private final List<ClientHandler> clients = new ArrayList<>();


    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.authService = new BaseAuthService();
    }
    public void start() throws IOException {
        System.out.println("Server is started");
        authService.start();

        try {
            while (true) {
                waitAndProcessClientConnection(); // метод ожидания и подключения клиента
            }
        } catch (IOException e) {
            System.out.println("Error of new connection opening");
            e.printStackTrace();
        } finally {
                serverSocket.close();
        }
    }

    private void waitAndProcessClientConnection() throws IOException {
        System.out.println("Client is awaited..."); // ожидается подключение
        Socket clientSocket = serverSocket.accept(); // Сервер принимает сокет клиента
        System.out.println("Client has connected");
        processClientConnection(clientSocket); // здесь будет создание КлиентХендлера и работа с ним
    }

    public AuthService getAuthService() {
        return authService;
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle(); // здесь будет создание входящего и выходящего потоков, аутентификация, ожидание и чтение сообщений.


    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler); // добавлять клиента
    }
    public void unSubscribe(ClientHandler clientHandler) { clients.remove(clientHandler); // пользователь отключился и его убирают из списка подключений
    }

    public boolean isUserBusy(String username) {
        for (ClientHandler client : clients) {
            if(client.getUsername().equals(username)) {
                return true;
            }
        } return false;
    }

    public void broadcastMessage(String message, ClientHandler sender, boolean isServerInfoMessage) throws IOException {
        for (ClientHandler client : clients) {
             if(client == sender) {
                 continue;
             }
             client.sendMessage(isServerInfoMessage ? null : sender.getUsername(),message);
        }
    }
}
