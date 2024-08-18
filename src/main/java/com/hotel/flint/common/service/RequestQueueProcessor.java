package com.hotel.flint.common.service;

import com.hotel.flint.common.configs.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RequestQueueProcessor {

    @Autowired
    private RequestQueueManager queueManager;

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.WAITING_LIST_QUEUE)
    public void processRequest(String requestId) throws InterruptedException {
        try {
            // 처리 완료된 후 큐에서 요청 제거
            queueManager.removeRequest(requestId);
        } catch (Exception e) {
            // 에러 발생 시 예외 처리
            System.err.println("Error processing request ID: " + requestId);
            e.printStackTrace();
            // 예외를 던져 Spring의 재시도 메커니즘을 트리거
            throw e;
        }
        // 시연을 위해 0.1초마다 메시지 소모
        Thread.sleep(100);
    }
}
