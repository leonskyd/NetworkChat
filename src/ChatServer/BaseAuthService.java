package ChatServer;

import java.util.List;

public class BaseAuthService implements AuthService{

    private static final List<User> clients = List.of(
            new User("user1", "1111", "Vasya"),
            new User("user2", "2222", "Masha"),
            new User("user3", "3333", "Boris"));



    @Override
    public void start() {
        System.out.println("Server authentification is started");
    }

    @Override
    public String GetUserNameByLoginAndPassword(String login, String password) {
        for (User client : clients) {
            if(client.getLogin().equals(login) && client.getPassword().equals(password)) {
                return client.getUsername();
            }
    } return null;
}

    @Override
    public void close() {
        System.out.println("Authentification has been finished");
    }
}
