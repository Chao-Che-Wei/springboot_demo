package com.vicent.demo.entity;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class SendMailRequest {
    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    @NotEmpty
    private List<String> receivers;

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }
}