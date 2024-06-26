package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.post.*;
import com.atl.map.dto.request.post.*;

public interface PostService {
    ResponseEntity<? super GetPostResponseDto> getPost(Integer postId);
    ResponseEntity<? super CreatePostResponseDto> createPost(CreatePostRequestDto dto, String email);
    ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer postId, String email);
    ResponseEntity<? super PatchPostResponseDto> patchPost(PatchPostRequestDto dto, Integer postId, String email);
    ResponseEntity<? super DeletePostResponseDto> deletePost(Integer postId, String email);

    ResponseEntity<? super GetLatestPostResponseDto> getLatestPostList();
    ResponseEntity<? super GetTopPostListResponseDto> getTopPostList();
    ResponseEntity<? super GetSearchPostListResponseDto> getSearchPostList(String searchWord);
    ResponseEntity<? super GetBuildingPostListResponseDto> getBuildingPostList(Integer buildingId);
    ResponseEntity<? super GetMyPostResponseDto> getUserPostList(String email);
    ResponseEntity<? super GetMyLikePostResponseDto> getUserLikePostList(String email);

    ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer postId, String email);
    ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer postId);
    ResponseEntity<? super DeleteCommentResponseDto> deleteComment(String email, Integer commentId);

    ResponseEntity<? super GetMyCommentListResponseDto> getMyComment(String email);
    ResponseEntity<? super GetChildCommentListResponseDto> getChildCommentList(Integer parentId);
    ResponseEntity<? super PostChildCommentResponseDto> postChildComment(PostChildCommentRequestDto dto, String email, Integer postId, Integer commentId);

}
