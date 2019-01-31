package com.dsmmm.battleships.client;

import com.dsmmm.battleships.client.io.Prefix;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;

class ServerListener implements Runnable {
    private final TextArea chatId;
    private final Controller controller;
    private BufferedReader bufferedReader;

    ServerListener(TextArea chatId, Controller controller, BufferedReader bufferedReader) {
        this.chatId = chatId;
        this.controller = controller;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    Prefix type = Prefix.getType(line);
                    String decipheredLine = Prefix.decipher(line);
                    switch (type) {
                        case CHAT:
                            chatId.appendText(decipheredLine + "\n");
                            break;
                        case HIT:
                            Printer.print(line);
                            break;
                        case SHIPS:
                            controller.showFleet(decipheredLine);
                            break;
                        default:
                            Printer.print(line);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
