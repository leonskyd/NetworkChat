package ChatClient;

import ChatClient.Network;
import ChatClient.NetworkClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class ChatController {

    @FXML
    public ListView<String> userList;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea chatHistory;
    @FXML
    private TextField textField;
    @FXML
    private Label usernameTitle;

    private Network network;

    public void setUsernameTitle(String usernameTitle) {
        this.usernameTitle.setText(usernameTitle);
    }

    public void setNetwork(Network network) {this.network = network;}

    @FXML
    public void initialize() {
        userList.setItems(FXCollections.observableArrayList(NetworkClient.USERS_TEST_DATA));
        sendButton.setOnAction(event -> ChatController.this.sendMessage());
        textField.setOnAction(event -> ChatController.this.sendMessage());
    }

    public void sendMessage() {
        String message = textField.getText();
        appendMessage("Me: " + message);
        textField.clear();

        try {
            network.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            NetworkClient.showErrorMessage("Connection error", "Error when sending a message", e.getMessage());
        }
    }

    public void appendMessage(String message) {
        String timestamp = DateFormat.getInstance().format(new Date());
        chatHistory.appendText(timestamp);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(message);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());
    }
}
