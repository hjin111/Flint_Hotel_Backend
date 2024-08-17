package com.hotel.flint.common.controller;

import com.hotel.flint.common.configs.RabbitMqConfig;
import com.hotel.flint.common.service.RequestQueueManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqRequestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RequestQueueManager queueManager;

    @PostMapping("/submit")
    public ResponseEntity<String> submitRequest() {
        String requestId = queueManager.addRequest();
        rabbitTemplate.convertAndSend(RabbitMqConfig.WAITING_LIST_QUEUE, requestId);

        int position = queueManager.getPositionInQueue(requestId);
        return ResponseEntity.ok("귀하의 대기 순서는 " + position + "번째 입니다. 요청 ID: " + requestId);
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<String> getStatus(@PathVariable String requestId) {
        int position = queueManager.getPositionInQueue(requestId);
        if (position == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청 ID를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok("귀하의 대기 순서는 " + position + "번째 입니다.");
    }
}
