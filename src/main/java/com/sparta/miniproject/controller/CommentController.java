package com.sparta.miniproject.controller;

import com.sparta.miniproject.controller.request.CommentRequestDto;
import com.sparta.miniproject.controller.response.ResponseDto;
import com.sparta.miniproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/auth/comment", method = RequestMethod.POST)       //
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    @RequestMapping(value = "/comment/{postId}", method = RequestMethod.GET)
    public ResponseDto<?> getAllComment(@PathVariable Long postId) {
        return commentService.getAllCommentsByPost(postId);
    }

    @RequestMapping(value = "/auth/comment/{commentId}", method = RequestMethod.PUT)
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(commentId, requestDto, request);
    }

    @RequestMapping(value = "/auth/comment/{commentId}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@PathVariable Long commentId,
                                        HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }
}