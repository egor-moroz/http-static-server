package com.quickblox.task.http;

import com.quickblox.task.Response;

public class HttpResponse implements Response {

    private String status = "";
    private String contentLength = "";
    private String contentType = "";
    private String content;

    public HttpResponse() {
    }

    public String getResponse() {
        return status + "\n\r" + contentLength + "\n\r" + contentType + "\n\r" + "\n\r" + content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        if (contentLength < 0) {
            throw new IllegalArgumentException();
        }
        this.contentLength = "Content-Length: " + contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException();
        }

        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException();
        }
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpResponse response = (HttpResponse) o;

        if (status != null ? !status.equals(response.status) : response.status != null) return false;
        if (contentLength != null ? !contentLength.equals(response.contentLength) : response.contentLength != null)
            return false;
        if (contentType != null ? !contentType.equals(response.contentType) : response.contentType != null)
            return false;
        return content != null ? content.equals(response.content) : response.content == null;

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (contentLength != null ? contentLength.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
