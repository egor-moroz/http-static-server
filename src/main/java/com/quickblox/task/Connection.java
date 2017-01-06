package com.quickblox.task;


public interface Connection {

    String readRequest() throws Exception;

    Response handle(Request request) throws Exception;

    void writeResponse(Response response) throws Exception;

}
