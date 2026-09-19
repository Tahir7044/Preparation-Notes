package DesignPattern.Behavioral;
/*
 * it is a behavioral design pattern 
 * it allows an object to pass a request along a chain of handler.
 * it can be use when we have 1 to many receiver/handler
 * 
 */

abstract class InterviewHandler {
    protected InterviewHandler next;

    protected InterviewHandler setNext(InterviewHandler next) {
        this.next = next;
        return next;
    }

    protected void callNext(String candidateName) {
        if (next != null) {
            next.hire(candidateName);
        } else {
            System.out.println(candidateName + " " + "you got hired");
        }
    }

    protected abstract void hire(String candidate);
}

class TechnicalHandler extends InterviewHandler {
    protected InterviewHandler next;

    public void hire(String candidate) {
        System.out.println(candidate + " " + " technical round invitation");
        int score = 70;
        if (score > 65) {
            System.out.println(candidate + " " + "passed technical round");
            callNext(candidate);
        }
    }
}

class BarRaiserHandler extends InterviewHandler {
    protected InterviewHandler next;

    public void hire(String candidate) {
        System.out.println(candidate + " " + " bar raiser round invitation");
        int score = 70;
        if (score > 65) {
            System.out.println(candidate + " " + " passed bar raiser round");
            callNext(candidate);
        }
    }
}

class HRHandler extends InterviewHandler {
    protected InterviewHandler next;

    public void hire(String candidate) {
        System.out.println(candidate + " " + " HR round invitation");
        int score = 70;
        if (score > 65) {
            System.out.println(candidate + " " + " passed HR technical round");
            callNext(candidate);
        }
    }
}

class InterviewProcess {
    private final InterviewHandler chain;

    public InterviewProcess() {
        this.chain = new TechnicalHandler();
        this.chain.setNext(new BarRaiserHandler()).setNext(new HRHandler());
    }

    public void start(String candidate) {
        chain.hire(candidate);
    }
}

public class ChainOfResponsibility {
    public static void main(String[] args) {
        InterviewProcess interview = new InterviewProcess();

        interview.start("tahir");
    }
}
