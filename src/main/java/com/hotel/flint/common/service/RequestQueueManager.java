package com.hotel.flint.common.service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RequestQueueManager {

    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    public String addRequest() {
        String requestId = UUID.randomUUID().toString();
        queue.add(requestId);
        return requestId;
    }

    public int getPositionInQueue(String requestId) {
        int position = 1;
        for (String id : queue) {
            if (id.equals(requestId)) {
                return position;
            }
            position++;
        }
        return -1; // 요청 ID가 큐에 없는 경우
    }

    public void removeRequest(String requestId) {
        queue.remove(requestId);
    }
}