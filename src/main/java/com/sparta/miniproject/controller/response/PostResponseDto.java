package com.sparta.miniproject.controller.response;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.domain.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private final Long postId;
    private final String title;
    private final String content;



    public PostResponseDto(Post post){   //매개변수 이름 확인필요
        this.postId =post.getId();
        this.title=post.getTitle();
        this.content=post.getContent();
    }

}
