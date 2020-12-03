package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controllers.AuthStageController;
import sample.controllers.Controller;
import sample.models.Network;

import java.util.List;

public class NetClient extends Application {

    public static final List<String> USERS_DATA = List.of("Vasya", "JhonY", "Mishka");
    public Stage primaryStage;
    private Stage authStage;
    private Network network;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(NetClient.class.getResource("views/authStage.fxml"));
        Parent page = authLoader.load();
        authStage = new Stage();
        authStage.setTitle("Authentication");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        authStage.setScene(scene);
        authStage.show();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NetClient.class.getResource("views/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 600, 400));

        network = new Network();
        AuthStageController authStageController = authLoader.getController();
        authStageController.setNetwork(network);
        authStageController.setNetClient(this);

        if (!network.connection()){
            showErrorMessage("", "Server connection error");
        }
        controller = loader.getController();
        controller.setNetwork(network);

    }

    public static void showErrorMessage(String message, String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection problem");
        alert.setHeaderText(error);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openChat(){
        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUserName());
        network.waitMessage(controller);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
