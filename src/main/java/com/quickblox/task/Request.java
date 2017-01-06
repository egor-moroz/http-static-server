package com.quickblox.task;

public interface Request {

    String getMethod();

    String getTarget();

    void setMethod(String method);

    void setTarget(String target);

}
