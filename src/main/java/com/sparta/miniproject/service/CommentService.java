package com.sparta.miniproject.service;

import com.sparta.miniproject.controller.request.CommentRequestDto;
import com.sparta.miniproject.controller.response.CommentResponseDto;
import com.sparta.miniproject.controller.response.ResponseDto;
import com.sparta.miniproject.domain.Comment;
import com.sparta.miniproject.domain.Member;
import com.sparta.miniproject.domain.Post;
import com.sparta.miniproject.jwt.TokenProvider;
import com.sparta.miniproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final TokenProvider tokenProvider;
    private final PostService postService;

    //댓글작성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "사용자 Token이 유효하지 않습니다.");
        }

        Post post = postService.existingPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .nickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    //댓글조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByPost(Long postId) {
        Post post = postService.existingPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();


        for (Comment comment : commentList) {

            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getCommentId())
                            .nickname(comment.getMember().getNickname())
                            .content(comment.getContent())

                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
}

    //댓글수정
    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
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
            return ResponseDto.fail("INVALID_TOKEN", "사용자 Token이 유효하지 않습니다.");
        }

        Post post = postService.existingPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        Comment comment = existingComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .nickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }


    //댓글삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest request) {
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

        Comment comment = existingComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        commentRepository.delete(comment);
        return ResponseDto.success("success");
    }


    //---------------------------------CommentService 메서드
    @Transactional(readOnly = true)
    public Comment existingComment(Long commentId) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
