package com.dsmmm.battleships.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSocket {
    void clientSocket() {
        Scanner into = new Scanner(System.in);
        System.out.println("Podaj imię: ");
        String imie = into.next();
        try (
                Socket echoSocket = new Socket("vps624409.ovh.net", 8189);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {
            String userInput;
            System.out.println(in.readLine());
            Thread t = new Thread(()-> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            while ((userInput = stdIn.readLine()) != null) {
                out.println(imie + " " + userInput);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
