package com.sparta.miniproject.controller;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.controller.response.PostResponseDto;
import com.sparta.miniproject.controller.response.ResponseDto;
import com.sparta.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/auth/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.createPost(requestDto, request);

    }

    //전체게시글조회

    @GetMapping("/post")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    //상세게시글 조회
    @GetMapping("/post/{postId}")
    public PostResponseDto getOnePost(@PathVariable Long postId){
        return postService.getOnePost(postId);
    }


    //상세게시글 수정
    @PutMapping("/auth/post/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto){
        return postService.updatePost(postId, requestDto);

    }

    //상세게시글 삭제
    @DeleteMapping("/auth/post/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return "게시글 삭제 성공";
    }

}
