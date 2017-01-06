package com.quickblox.task;

import com.quickblox.task.Request;
import com.quickblox.task.http.HttpRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestTest {


    String req = "\nConnection: keep-alive\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
            "Referer: http://127.0.0.1:5555/\n" +
            "Accept-Encoding: gzip, deflate, sdch\n" +
            "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseRequestIllegalArgument() {
        HttpRequest.parseRequest(null);
    }

    @Test
    public void testParseRequestGetNotHtml() {

        Request request = HttpRequest.parseRequest("GET /notHtmlPage.txt HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getTarget(), null);
    }

    @Test
    public void testParseRequestGetIndex() {
        Request request = HttpRequest.parseRequest("GET / HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getTarget(), "index.html");
    }

    @Test
    public void testParseRequestGetHtml() {
        Request request = HttpRequest.parseRequest("GET /htmlPage.html HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getTarget(), "htmlPage.html");
    }

    @Test
    public void testParseRequestPostNotHtml() {
        Request request = HttpRequest.parseRequest("POST /notHtmlPage.txt HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), null);
        Assert.assertEquals(request.getTarget(), null);
    }

    @Test
    public void testParseRequestPostIndex() {
        Request request = HttpRequest.parseRequest("POST / HTTP/1.1");
        Assert.assertEquals(request.getMethod(), null);
        Assert.assertEquals(request.getTarget(), null);
    }

    @Test
    public void testParseRequestPostHtml() {
        Request request = HttpRequest.parseRequest("POST /htmlPage.html HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), null);
        Assert.assertEquals(request.getTarget(), null);
    }


    @Test
    public void testParseRequestHeadNotHtml() {
        Request request = HttpRequest.parseRequest("HEAD /notHtmlPage.txt HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), null);
        Assert.assertEquals(request.getTarget(), null);
    }

    @Test
    public void testParseRequestHeadIndex() {
        Request request = HttpRequest.parseRequest("HEAD / HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), null);
        Assert.assertEquals(request.getTarget(), null);
    }

    @Test
    public void testParseRequestHeadHtml() {
        Request request = HttpRequest.parseRequest("HEAD /htmlPage.html HTTP/1.1"+req);
        Assert.assertEquals(request.getMethod(), null);
        Assert.assertEquals(request.getTarget(), null);
    }

}
