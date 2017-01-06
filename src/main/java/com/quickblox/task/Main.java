package com.quickblox.task;

import com.quickblox.task.http.HttpServerFuture;

public class Main {

    public static void main(String[] args) {
        Server serverFuture = new HttpServerFuture();
        if (args.length == 2 && args[0].equals("-p")) {
            try {
                serverFuture.setPort(Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                System.out.println("Incorrect param please try again..");
            }
        } else {
            System.out.println("Incorrect param please try again..");
        }

        serverFuture.run();

    }
}