package ChatClient;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.io.IOException;

public class authController {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;

    private Network network;
    private NetworkClient networkClient;

    @FXML
    public void checkAuth() throws IOException {
        String login = loginField.getText(); //ввод логина из окошка
        String password = passwordField.getText();
        if (login.isBlank() || password.isBlank()) { // если ничего не ввели
            NetworkClient.showErrorMessage("Error of autorization", "Error of input", "Fields shall not be empty");
            return;
        }
        String authErrorMessage = network.sendAuthCommand(login, password);
        if (authErrorMessage != null) {
            networkClient.showErrorMessage("Error of authorization", "Smth is going wrong", authErrorMessage);
        } else {
            networkClient.openMainChatWindow(); // try catch is not needed
        }


    }
    public void setNetwork (Network network){
        this.network = network;
    }

    public void setNetworkClient (NetworkClient networkClient){
        this.networkClient = networkClient;
    }
}
