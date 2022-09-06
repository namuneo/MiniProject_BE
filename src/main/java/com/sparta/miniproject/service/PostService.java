package com.sparta.miniproject.service;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.controller.response.PostResponseDto;
import com.sparta.miniproject.controller.response.ResponseDto;
import com.sparta.miniproject.domain.Member;
import com.sparta.miniproject.domain.Post;
import com.sparta.miniproject.jwt.TokenProvider;
import com.sparta.miniproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;           //의존성 주입
    private final TokenProvider tokenProvider;

    //게시글 작성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request){      //controller에서 매개변수 받아올 때 같은 타입으로
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .member(member)
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .nickname(member.getNickname())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );

        //이미지 업로드 추가 필요
    }

    //전체게시글 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {

        return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
//        return ResponseDto.success(listResponse.getPostListResponse(postList));
    }


    //상세게시글 조회
    public PostResponseDto getOnePost(Long postId){                //controller의 메서드 반환타입 동일하게,이름은 상관x
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")       //예외처리
        );
        //생성자 대신 builder로 사용가능

        Member member = tokenProvider.getMemberFromAuthentication();
        return PostResponseDto.builder()
                .postId(post.getId())
                .nickname(member.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();

    }

    //게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        post.update(requestDto);
        postRepository.save(post);

        Member member = tokenProvider.getMemberFromAuthentication();
        return PostResponseDto.builder()
                .postId(post.getId())
                .nickname(member.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    //게시글 삭제
    @Transactional
    public void deletePost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).orElseThrow(       //필요한 정보를 찾고  -> 그 정보를 업데이트
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        postRepository.delete(post);
    }


    @Transactional(readOnly = true)
    public Post existingPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

}
