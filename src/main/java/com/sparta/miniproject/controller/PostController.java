package com.sparta.miniproject.controller;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.controller.response.PostResponseDto;
import com.sparta.miniproject.controller.response.ResponseDto;
import com.sparta.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/auth/post")
    public ResponseDto<?> createPost(@RequestPart PostRequestDto requestDto, @RequestPart(required = false) MultipartFile multipartFile ,HttpServletRequest request){
        return postService.createPost(requestDto, multipartFile, request);

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
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestPart PostRequestDto requestDto, @RequestPart(required = false) MultipartFile multipartFile, HttpServletRequest request){
        return postService.updatePost(postId, requestDto, multipartFile, request);

    }

    //상세게시글 삭제
    @DeleteMapping("/auth/post/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest request){
        return postService.deletePost(postId, request);
//        return "게시글 삭제 성공";
    }

}
