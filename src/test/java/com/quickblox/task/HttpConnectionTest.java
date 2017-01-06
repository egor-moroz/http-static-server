package com.quickblox.task;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class HttpConnectionTest {

   private HttpConnection connection = new HttpConnection();
   private Request request = new Request();
   private String okHeader = "HTTP/1.1 200 OK";
   private String errorHeader = "HTTP/1.1 404 Not Found";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void handleException() throws IOException, ExecutionException, InterruptedException {
        connection.handle(null);
    }

    @Test
    public void testHandleOk() throws InterruptedException, ExecutionException, IOException {
        request.setMethod("GET");
        request.setTarget("index.html");
        Response response = connection.handle(request);
        Assert.assertEquals(okHeader, response.getStatus());
    }

    @Test
    public void testHandleMethodNull() throws InterruptedException, ExecutionException, IOException {
        request.setMethod(null);
        request.setTarget("index.html");
        Response response = connection.handle(request);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test
    public void testHandleMethodPost() throws InterruptedException, ExecutionException, IOException {
        request.setMethod("POST");
        request.setTarget("index.html");
        Response response = connection.handle(request);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test
    public void testHandleTargetNull() throws InterruptedException, ExecutionException, IOException {
        request.setMethod("GET");
        request.setTarget(null);
        Response response = connection.handle(request);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test
    public void testHandleTargetAndMethodNull() throws InterruptedException, ExecutionException, IOException {
        request.setMethod(null);
        request.setTarget(null);
        Response response = connection.handle(request);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test
    public void testHandleTargetNotFile() throws InterruptedException, ExecutionException, IOException {
        Path directory = Files.createTempDirectory("testHandle");
        request.setMethod("GET");
        request.setTarget(directory.toString());
        Response response = connection.handle(request);
        Files.deleteIfExists(directory);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test
    public void testHandleTargetFileHtml() throws InterruptedException, ExecutionException, IOException {
        Path file = Files.createTempFile("testHandle", ".html");
        request.setMethod("GET");
        request.setTarget(file.toString());
        Response response = connection.handle(request);
        Files.deleteIfExists(file);
        Assert.assertEquals(okHeader, response.getStatus());
    }

    @Test
    public void testHandleTargetDirectoryHtml() throws InterruptedException, ExecutionException, IOException {
        Path directory = Files.createTempDirectory("home.html");
        request.setMethod("GET");
        request.setTarget(directory.toString());
        Response response = connection.handle(request);
        Files.deleteIfExists(directory);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test
    public void testCreateOkResponse() throws IOException, ExecutionException, InterruptedException {
        Path file = Files.createTempFile("testHandle", ".html");
        Response response = connection.createOkResponse(file);
        Files.deleteIfExists(file);
        Assert.assertEquals(okHeader, response.getStatus());

    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateOkResponseException() throws IOException, ExecutionException, InterruptedException {
        Response response = connection.createOkResponse(null);
        Assert.assertEquals(okHeader, response.getStatus());

    }

    @Test
    public void testCreateErrorResponse() throws IOException, ExecutionException, InterruptedException {
        Path file = Files.createTempFile("testCreateErrorResponse", ".html");
        Response response = connection.createErrorResponse(file);
        Files.deleteIfExists(file);
        Assert.assertEquals(errorHeader, response.getStatus());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testReadContentException() throws IOException {
         connection.readContent(null);
    }

    @Test
    public void testReadContent() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path file = Paths.get(classLoader.getResource("testReadContent.html").toURI());
        long size = Files.size(file);
        String content = connection.readContent(file);
        Assert.assertEquals(size, content.length());
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWriteResponse() throws ExecutionException, InterruptedException {
        connection.writeResponse(null);
    }
}
