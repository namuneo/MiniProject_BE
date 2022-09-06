package com.sparta.miniproject.repository;

import com.sparta.miniproject.domain.Comment;
import com.sparta.miniproject.domain.Member;
import com.sparta.miniproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByMember(Member member);
}
