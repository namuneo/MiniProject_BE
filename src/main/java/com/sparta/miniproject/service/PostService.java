package com.sparta.miniproject.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostService {

    private final PostRepository postRepository;           //의존성 주입
    private final TokenProvider tokenProvider;
    private final S3UploaderService s3UploaderService;

    //게시글 작성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request){      //controller에서 매개변수 받아올 때 같은 타입으로
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        String FileName = null;
        if (!multipartFile.isEmpty()) {
            try {
                FileName = s3UploaderService.uploadFiles(multipartFile, "image");
                System.out.println(FileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Member member = tokenProvider.getMemberFromAuthentication();
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imgUrl(FileName)
                .member(member)
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getMember().getNickname())
                        .imgUrl(FileName)
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
    }


    //상세게시글 조회
    public PostResponseDto getOnePost(Long postId){                //controller의 메서드 반환타입 동일하게,이름은 상관x
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")       //예외처리
        );
        //생성자 대신 builder로 사용가능

        return PostResponseDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .imgUrl(post.getImgUrl())
                .author(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();

    }

    //게시글 수정
    @Transactional
    public ResponseDto<Post> updatePost(Long postId, PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        String FileName = null;
        if (!multipartFile.isEmpty()) {
            try {
                FileName = s3UploaderService.uploadFiles(multipartFile, "image");
                System.out.println(FileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Post post = existingPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }
        requestDto.setImgUrl(FileName);
        post.update(requestDto);
        return ResponseDto.success(post);

    }

    //게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest request){
//        Post post = postRepository.findById(postId).orElseThrow(       //필요한 정보를 찾고  -> 그 정보를 업데이트
//                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
//        );

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = existingPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
        return ResponseDto.success("삭제가 완료 되었습니다");
    }


    @Transactional(readOnly = true)
    public Post existingPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
