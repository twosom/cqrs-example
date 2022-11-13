package com.icloud.controller;

import com.icloud.domain.PostDocument;
import com.icloud.domain.PostRepository;
import lombok.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/query/post")
@RequiredArgsConstructor
public class PostQueryController {

    private final PostRepository postRepository;

    @RabbitListener(queues = "post.create")
    public void listen(PostMessage message) {
        var postDocument = PostDocument.builder()
                .referenceId(message.referenceId)
                .content(message.content)
                .username(message.getUsername())
                .build();

        postRepository.save(postDocument);
    }

    @GetMapping
    public List<PostDocument> findAll() {
        return StreamSupport.stream(postRepository.findAll().spliterator(), false)
                .toList();
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

}
