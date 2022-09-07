package com.sparta.miniproject.controller.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String author;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
