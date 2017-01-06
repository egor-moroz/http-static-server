package com.quickblox.task.http;

import com.quickblox.task.Connection;
import com.quickblox.task.Request;
import com.quickblox.task.Response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class HttpConnection implements Connection {

    private String pathToStaticContent = "./";
    private String pathToNotFound = "404.html";
    private Charset charset = Charset.defaultCharset();
    private CharsetDecoder decoder = charset.newDecoder();
    private AsynchronousSocketChannel asynchronousSocketChannel;

    public HttpConnection() {
    }

    public HttpConnection(AsynchronousSocketChannel asynchronousSocketChannel) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
    }


    public String readRequest() throws ExecutionException, InterruptedException, CharacterCodingException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        asynchronousSocketChannel.read(buffer).get();
        buffer.flip();
        CharBuffer charBuffer = decoder.decode(buffer);
        buffer.clear();
        return charBuffer.toString();
    }

    public String readContent(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        FileChannel file = FileChannel.open(path);
        MappedByteBuffer buffer = file.map(FileChannel.MapMode.READ_ONLY, 0, file.size());
        buffer.load();
        buffer.clear();
        file.close();
        CharBuffer charBuffer = decoder.decode(buffer);
        return charBuffer.toString();
    }

    public void writeResponse(Response response) throws ExecutionException, InterruptedException {
        if (asynchronousSocketChannel == null || response == null) {
            throw new IllegalArgumentException();
        }
        final ByteBuffer responseBuffer = ByteBuffer.wrap(response.getResponse().getBytes());
        asynchronousSocketChannel.write(responseBuffer).get();
        responseBuffer.clear();
    }

    public Response handle(Request request) throws InterruptedException, ExecutionException, IOException {
        if (request == null) {
            throw new IllegalArgumentException();
        }
        if ((request.getMethod() != null) && (request.getMethod().equals("GET")) && (request.getTarget() != null) && (request.getTarget().endsWith(".html"))) {
            Path filePath = Paths.get(request.getTarget());
            if (Files.isRegularFile(filePath)) {
                return createOkResponse(filePath);
            } else {
                Path errorPath = Paths.get(pathToStaticContent + pathToNotFound);
                return createErrorResponse(errorPath);
            }
        } else {
            Path errorPath = Paths.get(pathToStaticContent + pathToNotFound);
            return createErrorResponse(errorPath);
        }
    }

    public Response createErrorResponse(Path errorPath) throws IOException, ExecutionException, InterruptedException {
        Response response = new HttpResponse();
        String content = readContent(errorPath);
        response.setStatus("HTTP/1.1 404 Not Found");
        response.setContent(content);
        return response;
    }

    public Response createOkResponse(Path okPath) throws IOException, ExecutionException, InterruptedException {
        if (okPath == null) {
            throw new IllegalArgumentException();
        }
        Response response = new HttpResponse();
        String content = readContent(okPath);
        response.setStatus("HTTP/1.1 200 OK");
        response.setContent(content);
        response.setContentLength(content.length());
        response.setContentType("Content-Type: text/html");
        return response;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public CharsetDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(CharsetDecoder decoder) {
        this.decoder = decoder;
    }

    public AsynchronousSocketChannel getAsynchronousSocketChannel() {
        return asynchronousSocketChannel;
    }

    public void setAsynchronousSocketChannel(AsynchronousSocketChannel asynchronousSocketChannel) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
    }

    public String getPathToStaticContent() {
        return pathToStaticContent;
    }

    public void setPathToStaticContent(String pathToStaticContent) {
        this.pathToStaticContent = pathToStaticContent;
    }

    public String getPathToNotFound() {
        return pathToNotFound;
    }

    public void setPathToNotFound(String pathToNotFound) {
        this.pathToNotFound = pathToNotFound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpConnection that = (HttpConnection) o;

        if (pathToStaticContent != null ? !pathToStaticContent.equals(that.pathToStaticContent) : that.pathToStaticContent != null)
            return false;
        if (pathToNotFound != null ? !pathToNotFound.equals(that.pathToNotFound) : that.pathToNotFound != null)
            return false;
        if (charset != null ? !charset.equals(that.charset) : that.charset != null) return false;
        if (decoder != null ? !decoder.equals(that.decoder) : that.decoder != null) return false;
        return asynchronousSocketChannel != null ? asynchronousSocketChannel.equals(that.asynchronousSocketChannel) : that.asynchronousSocketChannel == null;

    }

    @Override
    public int hashCode() {
        int result = pathToStaticContent != null ? pathToStaticContent.hashCode() : 0;
        result = 31 * result + (pathToNotFound != null ? pathToNotFound.hashCode() : 0);
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + (decoder != null ? decoder.hashCode() : 0);
        result = 31 * result + (asynchronousSocketChannel != null ? asynchronousSocketChannel.hashCode() : 0);
        return result;
    }
}
