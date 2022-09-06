package com.sparta.miniproject.controller;

import com.sparta.miniproject.controller.response.ImageResponseDto;
import com.sparta.miniproject.controller.response.ResponseDto;
import com.sparta.miniproject.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3UploaderService s3UploaderService;

//    List<MultipartFile> files;

    @PostMapping("/api/auth/image")
    public ResponseDto<?> imageUpload(@RequestPart(required = false) MultipartFile multipartFile){

        if(multipartFile.isEmpty()){
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }
        try{
            return ResponseDto.success(new ImageResponseDto(s3UploaderService.uploadFiles(multipartFile,"static")) );
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }

    }

}