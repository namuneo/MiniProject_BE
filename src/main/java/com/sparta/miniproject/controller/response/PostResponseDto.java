package com.sparta.miniproject.controller.response;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String imgUrl;



//    public PostResponseDto(Post post){   //매개변수 이름 확인필요
//        this.postId =post.getId();
//        this.title=post.getTitle();
//        this.content=post.getContent();
//    }

}
