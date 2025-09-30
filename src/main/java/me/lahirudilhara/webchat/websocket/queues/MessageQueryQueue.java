package me.lahirudilhara.webchat.websocket.queues;

import me.lahirudilhara.webchat.websocket.queues.events.MessageQueryEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageQueryQueue {
    private final BlockingQueue<MessageQueryEvent> queue =  new LinkedBlockingQueue<>();

    public void push(MessageQueryEvent remainMessageDTO) {
        queue.offer(remainMessageDTO);
    }

    public MessageQueryEvent poll() throws InterruptedException {
        return queue.take();
    }
}
