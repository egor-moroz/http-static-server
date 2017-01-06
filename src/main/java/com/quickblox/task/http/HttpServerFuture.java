package com.quickblox.task.http;

import com.quickblox.task.Connection;
import com.quickblox.task.Request;
import com.quickblox.task.Response;
import com.quickblox.task.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

public class HttpServerFuture implements Server {

    final private int DEFAULT_PORT = 5555;
    final private String IP = "127.0.0.1";
    private int port;

    public HttpServerFuture() {
    }

    public HttpServerFuture(int port) {
        this.port = port;
    }

    public void run() {
        ExecutorService taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                if (port > 0 && port < 65536) {
                    asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, port));
                    System.out.println("Server starting on " + port + " port");
                } else {
                    asynchronousServerSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
                    System.out.println("Incorrect port server starting on default " + DEFAULT_PORT + " port");
                }

                System.out.println("Waiting for connections...");

                while (true) {
                    Future<AsynchronousSocketChannel> asynchronousServerSocketChannelFuture = asynchronousServerSocketChannel.accept();
                    try {

                        final AsynchronousSocketChannel asynchronousSocketChannel = asynchronousServerSocketChannelFuture.get();

                        Callable<String> worker = new Callable<String>() {
                            @Override
                            public String call() {
                                String host = null;
                                try {
                                    host = asynchronousSocketChannel.getRemoteAddress().toString();
                                    System.out.println("Incoming connection from: " + host);
                                    Connection clientConnection = new HttpConnection(asynchronousSocketChannel);
                                    Request request = HttpRequest.parseRequest(clientConnection.readRequest());
                                    Response response = clientConnection.handle(request);
                                    clientConnection.writeResponse(response);
                                    asynchronousSocketChannel.close();
                                    System.out.println(host + " was successfully served");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    try {
                                        asynchronousSocketChannel.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return host;
                            }
                        };

                        taskExecutor.submit(worker);
                    } catch (InterruptedException | ExecutionException ex) {
                        System.err.println(ex);
                        System.err.println("\n Server is shutting down...");
                        taskExecutor.shutdown();
                        while (!taskExecutor.isTerminated()) {
                        }
                        break;
                    }
                }
            } else {
                System.out.println("The asynchronous server-socked channel cannot be opened!");
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public String getIP() {
        return IP;
    }

    public int getDEFAULT_PORT() {
        return DEFAULT_PORT;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
