package ChatServer;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler { // содержит подключение клиента, входящий и выходящий потоки

    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String OK_CMD_PREFIX = "/enterOk";
    private static final String ERR_CMD_PREFIX = "/enterErr";
    private static final String PRIVATE_MSG_PREFIX = "/w";
    private static final String END_CMD = "/end";
    private static final String CLIENT_MSG_PREFIX = "/clientMsg";
    private static final String SERVER_MSG_PREFIX = "/serverMsg";


    private final MyServer myServer;
    private final Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public String getUsername() {
        return username;
    }

    public void handle() throws IOException {
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
        new Thread(() -> {
            try {
                authentification();
                readMessage();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            // reading of messages and waiting
        }).start();
    }

    private void readMessage() throws IOException {
        while(true) {
            String message = in.readUTF();
            System.out.println("message | " + username + " : " + message);
            if(message.startsWith(END_CMD)) {
                return;
            }
            else if (message.startsWith(PRIVATE_MSG_PREFIX)) {
                //TODO
            }
            else {
                myServer.broadcastMessage(message,this,false);
            }
        }
    }

    private void authentification() throws IOException {
        String message = in.readUTF();

        while (true) {
            if (message.startsWith(AUTH_CMD_PREFIX)) {
                String[] parts = message.split("\\s+", 3);
                String login = parts[1];
                String password = parts[2];
                AuthService authService = myServer.getAuthService();
                username = authService.GetUserNameByLoginAndPassword(login, password);
                if (username != null) {

                    if (myServer.isUserBusy(username)) { // проверить свободен ли никнейм
                        out.writeUTF(String.format("%s %s", ERR_CMD_PREFIX, "login is already in use !"));
                    }
                    out.writeUTF(String.format("%s %s", OK_CMD_PREFIX, username));
                    // send nickname to client
                    // inform all the users about newcomer connection
                    myServer.broadcastMessage(String.format(">>> &s connected to chat", username), this, true); // последнее это флаг серверного сообщениф

                    myServer.subscribe(this); // зарегистрировали клиента
                    break;
                } else {
                    out.writeUTF(String.format("%s %s", ERR_CMD_PREFIX, "login and password do not match!"));
                }
            } else {
                out.writeUTF(String.format("%s %s", ERR_CMD_PREFIX, "Error of authorization !"));
            }
        }
    }
        public void sendMessage (String sender, String message) throws IOException {
            if (sender == null) {
                out.writeUTF(String.format("%s %s", SERVER_MSG_PREFIX, message));
            } else {
                out.writeUTF(String.format("%s %s %s", CLIENT_MSG_PREFIX, sender, message));

            }
        }
    }
