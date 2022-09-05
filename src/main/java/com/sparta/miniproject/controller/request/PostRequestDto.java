package com.sparta.miniproject.controller.request;

import lombok.Getter;

@Getter
public class PostRequestDto {   //client한테서 json형식으로 받아옴
    private String title;
    private String content;
    private String imgUrl;
}
