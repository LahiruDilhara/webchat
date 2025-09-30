package me.lahirudilhara.webchat.websocket.queues;

import me.lahirudilhara.webchat.websocket.queues.events.TextMessageEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TextMessageQueue {
    private final BlockingQueue<TextMessageEvent> queue =  new LinkedBlockingQueue<>();

    public void push(TextMessageEvent message) {
        queue.offer(message);
    }

    public TextMessageEvent poll() throws InterruptedException {
        return queue.take();
    }
}
