package com.quickblox.task;

public interface Response {

    String getResponse();

    void setStatus(String status);

    void setContent(String content);

    void setContentType(String contentType);

    void setContentLength(int contentLength);

}
