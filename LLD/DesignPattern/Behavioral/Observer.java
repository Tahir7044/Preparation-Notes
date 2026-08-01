package DesignPattern.Behavioral;

import java.util.ArrayList;

interface IChannel {
    void subscriber(ISubscriber obs);
    void unSubscriber(ISubscriber obs);
    void notifySubscribers();
}

class Channel implements IChannel {

    private ArrayList<ISubscriber> subscribers;
    private String name;
    private String latestVideo;

    public Channel(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void subscriber(ISubscriber ob) {
        this.subscribers.add(ob);
    }

    public void unSubscriber(ISubscriber ob) {
        this.subscribers.remove(ob);
    }

    public void notifySubscribers() {
        subscribers.stream().forEach(subscriber -> subscriber.notified());
    }

    public void upload(String video) {
        this.latestVideo = video;
        notifySubscribers();
    }

    public String getVideo() {
        return this.latestVideo;
    }


}


interface ISubscriber {
    void notified();
}

class Subscriber implements ISubscriber {
    private Channel channel;
    private String name;
    public Subscriber(String name, Channel channel) {
        this.channel = channel;
        this.name = name;
    }
    public void notified() {
        System.out.println(channel.getVideo());
    }

    public String getName() {
        return name;
    }
}


public class Observer {
    public static void main(String[] args) {

        Channel channel = new Channel("WWE");

        ISubscriber sb1 = new Subscriber("Tahir", channel);
        ISubscriber sb2 = new Subscriber("Mudassir", channel);
        ISubscriber sb3 = new Subscriber("Nafis", channel);
        channel.subscriber(sb1);
        channel.upload("Wrestlemania 25");

        channel.subscriber(sb2);
        channel.upload("Wrestlemania 26");

        channel.subscriber(sb3);
        channel.upload("Wrestlemania 27");

        channel.unSubscriber(sb3);
        channel.upload("Wrestlemania 28");
    }
}
