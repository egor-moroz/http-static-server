package com.quickblox.task.http;

import com.quickblox.task.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest implements Request {

    private String request;
    private static Pattern httpGetPattern = Pattern.compile("(?s)GET /?(\\S*).*");
    private String method;
    private String target;

    public HttpRequest() {
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Pattern getHttpGetPattern() {
        return httpGetPattern;
    }

    public void setHttpGetPattern(Pattern httpGetPattern) {
        this.httpGetPattern = httpGetPattern;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public static Request parseRequest(String request) {
        if (request == null) {
            throw new IllegalArgumentException();
        }
        Request req = new HttpRequest();
        Matcher get = httpGetPattern.matcher(request);
        if (get.matches()) {
            req.setMethod("GET");
            String target = get.group(1);
            if ("".equals(target)) {
                target = target + "index.html";
                req.setTarget(target);
            }
            if (target.endsWith(".html")) {
                req.setTarget(target);
            }
        }
        return req;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest request1 = (HttpRequest) o;

        if (request != null ? !request.equals(request1.request) : request1.request != null) return false;
        if (method != null ? !method.equals(request1.method) : request1.method != null) return false;
        return target != null ? target.equals(request1.target) : request1.target == null;

    }

    @Override
    public int hashCode() {
        int result = request != null ? request.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }
}
