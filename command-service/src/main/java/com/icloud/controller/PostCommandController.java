package com.icloud.controller;

import com.icloud.domain.PostEntity;
import com.icloud.domain.PostEntityRepository;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 예제 복잡도를 최소화 하기 위해 Service 클래스는 생략
@RestController
@RequestMapping("/api/command/post")
@RequiredArgsConstructor
public class PostCommandController {

    private final PostEntityRepository repository;
    private final RabbitTemplate rabbitTemplate;


    @PostMapping
    public void post(@RequestBody PostRequest request) {
        // 게시글을 RDB 에 저장
        var entity = PostEntity.builder()
                .username(request.getUsername())
                .content(request.getContent())
                .build();
        repository.save(entity);
        var message = PostMessage.builder()
                .referenceId(entity.getId())
                .content(entity.getContent())
                .username(entity.getUsername())
                .build();
        rabbitTemplate.convertAndSend("post.create", message);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class PostMessage {
        private Long referenceId;
        private String content;
        private String username;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class PostRequest {
        private String content;
        private String username;
    }


}
