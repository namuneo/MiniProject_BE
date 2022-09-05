package com.sparta.miniproject.controller;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.controller.response.PostResponseDto;
import com.sparta.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/api/auth/post")
    public String createPost(@RequestBody PostRequestDto requestDto){
        postService.createPost(requestDto);
        return "게시글 작성 성공";
    }

    //전체게시글조회
    @GetMapping("/api/posts")
    public List<PostResponseDto> getList(){
        return postService.postList();
    }


    //상세게시글 조회
    @GetMapping("/api/post/{postId}")
    public PostResponseDto getOnePost(@PathVariable Long postId){
        return postService.getOnePost(postId);
    }


    //상세게시글 수정
    @PutMapping("/api/auth/post/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto){
        return postService.updatePost(postId, requestDto);

    }

    //상세게시글 삭제
    @DeleteMapping("/api/auth/post/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return "게시글 삭제 성공";
    }

}
