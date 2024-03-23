package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.post.CreatePostResponseDto;
import com.atl.map.dto.request.post.CreatePostRequestDto;

public interface PostService {

    ResponseEntity<? super CreatePostResponseDto> createPost(CreatePostRequestDto dto, String email);
}
