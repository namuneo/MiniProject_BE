package com.sparta.miniproject.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {   //client한테서 json형식으로 받아옴
    private String title;
    private String content;
    private String imgUrl;
}
