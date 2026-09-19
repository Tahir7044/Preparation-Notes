package DesignPattern.Creational;

import java.util.ArrayList;
import java.util.List;

/*
 * It helps to simplify the creation of complex object.
 * A complex Object -> an object having lots of properties.
 * the object is immutable
 */

 class EmailBuilder {
    private String to;
    private String subject;
    private String body;
    private String cc;
    private String bcc;
    private ArrayList<String> attachment;

    public EmailBuilder setTo(String to) {
        this.to = to;
        return this;
    }

    public EmailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public EmailBuilder setCc(String cc) {
        this.cc = cc;
        return this;
    }

    public EmailBuilder setBcc(String bcc) {
        this.bcc = bcc;
        return this;
    }

    public EmailBuilder setAttachment(ArrayList<String> attachment) {
        this.attachment = attachment;
        return this;
    }

    public Email build() {
        return new Email(this);
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public ArrayList<String> getAttachment() {
        return attachment;
    }
 }

 class Email {
    private String to;
    private String subject;
    private String body;
    private String cc;
    private String bcc;
    private ArrayList<String> attachment;

    public Email(EmailBuilder builder) {
        this.to = builder.getTo();
        this.subject = builder.getSubject();
        this.body = builder.getBody();
        this.cc = builder.getCc();
        this.bcc = builder.getBcc();
        this.attachment = builder.getAttachment();
    }
    public String toString() {
        return "Email [to=" + to + ", subject=" + subject + ", body=" + body + ", cc=" + cc + ", bcc=" + bcc + ", attachment=" + attachment + "]";
    }
 }

public class Builder {
    public static void main(String[] args) {
        Email email = new EmailBuilder()
                .setTo("tahir@gmail.com")
                .setSubject("Hello")
                .setBody("Hello")
                .setCc("tahir@gmail.com")
                .setBcc("tahir@gmail.com")
                .setAttachment(new ArrayList<>(List.of("file1.txt", "file2.txt")))
                .build();

        System.out.println(email.toString());
    }
}
