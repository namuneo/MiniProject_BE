package com.sparta.miniproject.service;

import com.sparta.miniproject.controller.request.PostRequestDto;
import com.sparta.miniproject.controller.response.PostResponseDto;
import com.sparta.miniproject.domain.Post;
import com.sparta.miniproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;           //의존성 주입

    //게시글 작성
    public void createPost(PostRequestDto requestDto){   //controller에서 매개변수 받아올 때 같은 타입으로
        Post post =new Post(requestDto);                 //postman이 아니라 controller에서 준거라서 @body 가 필요없음
        postRepository.save(post);



    //이미지 업로드 추가 필요
    }

    //전체게시글 조회
    public List<PostResponseDto> postList() {
        List<Post> post = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();            //빈 리스트 만들어주고

        for (int i=0; i< post.size(); i++){
            PostResponseDto postResponseDto = new PostResponseDto(post.get(i));     //하나하나 생성자 만들어주고 넣어주기
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }


    //상세게시글 조회
    public PostResponseDto getOnePost(Long postId){                //controller의 메서드 반환타입 동일하게,이름은 상관x
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")       //예외처리
        );

        PostResponseDto responseDto = new PostResponseDto(post);       //생성자 대신 builder로 사용가능
        return responseDto;

    }

    //게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto){
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        post.update(requestDto);
        postRepository.save(post);
        PostResponseDto responseDto = new PostResponseDto(post);
        return responseDto;
    }

    //게시글 삭제
    @Transactional
    public void deletePost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).orElseThrow(       //필요한 정보를 찾고  -> 그 정보를 업데이트
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        postRepository.deleteById(postId);
    }

}
