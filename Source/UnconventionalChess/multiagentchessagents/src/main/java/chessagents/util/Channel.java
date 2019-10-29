package chessagents.util;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Channel<T> {

    private Queue<T> messageQueue;

    public Channel(int size) {
        messageQueue = new LinkedBlockingQueue<>(size);
    }

    public void send(T value) {
        messageQueue.add(value);
    }

    public T receive() {
        return messageQueue.poll();
    }

    public boolean containsMessages() {
        return !messageQueue.isEmpty();
    }

    public void clear() {
        messageQueue.clear();
    }
}
