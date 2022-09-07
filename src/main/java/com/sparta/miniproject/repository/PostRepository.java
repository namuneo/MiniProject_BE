package com.sparta.miniproject.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.sparta.miniproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();

}
