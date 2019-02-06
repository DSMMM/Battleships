package com.dsmmm.battleships.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;


@SuppressWarnings("WeakerAccess")
public class Controller implements Initializable, Gameable {

    private static final double SIZE = 30.0d;

    private ClientInitializer client;

    @FXML
    private TextField nameId;

    @FXML
    private Button joinId;

    @FXML
    private TextArea chatId;

    @FXML
    private TextField inputChat;

    @FXML
    private Pane paneEnemy;

    @FXML
    private Button generateFleet;

    @FXML
    private Pane paneFleet;

    @FXML
    void inputName() {
        joinId.setDisable(nameId.getText().isEmpty());
    }

    @FXML
    void join() {
        client = new ClientInitializer(nameId.getText());
        if (client.connectWithServer()) {
            proceedConnection();
        } else {
            chatId.appendText("Nie udało się nawiązać połączenia z serwerem.\n");
        }
    }

    public void proceedConnection() {
        ServerListener listenerThread = client.makeListenerThread(new GuiChat(chatId), this);
        listenerThread.start();
        ChatFX.setServerListener(listenerThread);
        joinId.setDisable(true);
        nameId.setDisable(true);
        enableBoard(paneEnemy);
        generateFleet.setDisable(false);
    }

    @FXML
    void enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            client.sendMessage(inputChat.getText());
            inputChat.clear();
        }
    }

    @FXML
    private void fillPane(Pane pane) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button();
                button.setLayoutX(i * SIZE);
                button.setLayoutY(j * SIZE);

                button.setPrefHeight(SIZE);
                button.setPrefWidth(SIZE);
                final int high = i + 1;
                final int width = j + 1;
                button.setOnAction(onFieldClickEvent(high, width));
                button.setId(high + "-" + width);
                button.setDisable(true);
                pane.getChildren().add(button);
            }
        }
    }

    private void enableBoard(Pane pane) {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Button button = (Button) pane.lookup("#" + i + "-" + j);
                button.setDisable(false);
            }
        }
    }

    private void disableBoard(Pane pane) {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Button button = (Button) pane.lookup("#" + i + "-" + j);
                button.setDisable(true);
            }
        }
    }

    private void resetFleet() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Button button = (Button) paneFleet.lookup("#" + i + "-" + j);
                button.setStyle("-fx-background-color: #3BB9FF; -fx-opacity: 1.0 !important;");
            }
        }
    }

    public void showFleet(String toDecode) {
        Platform.runLater(this::resetFleet);
        Platform.runLater(() -> reloadFleet(toDecode));
    }

    @Override
    public void showEnemyMiss(String toDecode) {
        Button button = (Button) paneFleet.lookup("#" + toDecode);
        colorButton(button, Colors.BLUE);
    }

    @Override
    public void showEnemyHit(String toDecode) {
        Button button = (Button) paneFleet.lookup("#" + toDecode);
        colorButton(button, Colors.YELLOW);
    }

    @Override
    public void showHit(String toDecode) {
        Button button = (Button) paneEnemy.lookup("#" + toDecode);
        colorButton(button, Colors.YELLOW);
    }

    @Override
    public void showMiss(String toDecode) {
        Button button = (Button) paneEnemy.lookup("#" + toDecode);
        colorButton(button, Colors.BLUE);
    }

    private void colorButton(Button button, Colors color) {
        button.setStyle("-fx-background-color: " + color.getColor() + "; -fx-opacity: 1.0 !important;");
    }

    private void reloadFleet(String toDecode) {
        String[] lines = toDecode.split(",");
        for (String s : lines) {
            Button button = (Button) paneFleet.lookup(s);
            button.setStyle("-fx-background-color: brown; -fx-opacity: 1.0 !important;");
        }
    }

    @FXML
    void generateFleet() {
        client.requestGenerateFleet();
    }

    private EventHandler<ActionEvent> onFieldClickEvent(int high, int width) {
        return (ActionEvent event) -> {
            client.sendCoordinatesToEnemy(high, width);
            disableBoard(paneEnemy);
        };
    }

    @Override
    public void nowaMetoda(String decipheredLine) {
        String[] coordinatesTable = decipheredLine.split("-");
        int column = Integer.parseInt(coordinatesTable[0]);
        int row = Integer.parseInt(coordinatesTable[1]);
        client.sendCoordinates(column, row);
        enableBoard(paneEnemy);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillPane(paneEnemy);
        fillPane(paneFleet);
    }
}