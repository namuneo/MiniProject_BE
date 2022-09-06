package com.sparta.miniproject.domain;


import com.sparta.miniproject.controller.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String imgUrl;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //댓글 연관관계
    @OneToMany(mappedBy="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imgUrl = requestDto.getImgUrl();
    }

   //comment에 필요해서 추가
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

}
