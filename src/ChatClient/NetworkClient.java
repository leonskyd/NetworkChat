package ChatClient;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class NetworkClient extends Application {

    public static final List<String> USERS_TEST_DATA = List.of("Vasya", "Masha", "Boris");
    private Stage primaryStage;
    private Stage authStage;
    private Network network;
    private ChatController chatController;



    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        network = new Network();
        if(!network.connect()) {
        showErrorMessage("Problems with connection", " ", "Error of connection to server");
        return; }

        openAuthWindow();  // открыть окно авторизации
        createMainChatWindow();

    }

    private void openAuthWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NetworkClient.class.getResource("authView.fxml")); // здесь свое видео окно на аутентификацию
        Parent root = loader.load();
        authStage = new Stage();

        authStage.setTitle("Авторизация");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        Scene scene = new Scene(root);
        authStage.setScene(scene);
        authStage.show();

        authController authController = loader.getController();
        authController.setNetwork(network);
        authController.setNetworkClient(this);
    }

    public void createMainChatWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NetworkClient.class.getResource("sample.fxml")); // из лоадера достаем вьюху

        Parent root = loader.load();

        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root,600,400));
        //primaryStage.show(); окно не должно создаваться по умолчанию

        chatController = loader.getController();
        chatController.setNetwork(network);

        primaryStage.setOnCloseRequest(windowEvent -> network.close());
    }

    public static void showErrorMessage(String title, String message, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openMainChatWindow() {
        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());
        chatController.setUsernameTitle(network.getUsername());
        network.waitMessage(chatController);
    }
}
