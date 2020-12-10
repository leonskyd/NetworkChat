package ChatServer;

public interface AuthService {

    void start();

    String GetUserNameByLoginAndPassword(String login, String password);

    void close();
}
