package com.quickblox.task;

import com.quickblox.task.Response;
import com.quickblox.task.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ResponseTest {

    HttpResponse response = new HttpResponse();

    @Test
    public void testGetResponse() {
        response.setContent("<html><body>Hello world</body></html>");
        response.setContentLength(11);
        response.setStatus("HTTP/1.1 200 OK");
        response.setContentType("Content-Type: text/html");
        Assert.assertEquals("HTTP/1.1 200 OK\n\r" + "Content-Length: 11\n\r" + "Content-Type: text/html\n\r" + "\n\r" +
                "<html><body>Hello world</body></html>", response.getResponse());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetContent() {
        response.setContent(null);
    }

    @Test
    public void testSetContentLength() {
        response.setContentLength(11);
        Assert.assertEquals("Content-Length: 11", response.getContentLength());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetContentLengthException() {
        response.setContentLength(-1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetStatusException() {
        response.setStatus(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetContentTypeException() {
        response.setContentType(null);
    }
}
