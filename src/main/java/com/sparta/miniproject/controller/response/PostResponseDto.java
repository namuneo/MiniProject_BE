package com.sparta.miniproject.controller.response;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private String author;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;



//    public PostResponseDto(Post post){   //매개변수 이름 확인필요
//        this.postId =post.getId();
//        this.title=post.getTitle();
//        this.content=post.getContent();
//    }

}
