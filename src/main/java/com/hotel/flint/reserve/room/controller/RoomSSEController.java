package com.hotel.flint.reserve.room.controller;

import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.user.employee.domain.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class RoomSSEController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // 여러번 구독 방지
    private Set<String> subscribeList = ConcurrentHashMap.newKeySet();

    @Qualifier("6")
    private final RedisTemplate<String, Object> sseRedisTemplate;

    public RoomSSEController(@Qualifier("6") RedisTemplate<String, Object> sseRedisTemplate) {
        this.sseRedisTemplate = sseRedisTemplate;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        emitters.put(email, emitter);

        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결되었습니다."));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emitter;
    }

    // publish => redis에 서버가 메시지 publish(?)
    public void publishMessage(RoomReservedListDto dto, List<Employee> roomEmployeeList) {

        for (Employee employee: roomEmployeeList) {
            SseEmitter emitter = emitters.get(employee.getEmail());

            //redis 활용
            sseRedisTemplate.convertAndSend(employee.getEmail(), dto);
        }
    }

}
