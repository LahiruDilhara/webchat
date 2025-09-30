package me.lahirudilhara.webchat.websocket.queues;

import me.lahirudilhara.webchat.models.message.Message;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageQueue {
    private final BlockingQueue<Message> queue =  new LinkedBlockingQueue<>();

    public void push(Message message) {
        queue.offer(message);
    }

    public Message poll() throws InterruptedException {
        return queue.take();
    }
}
