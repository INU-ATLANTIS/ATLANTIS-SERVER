package com.atl.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.atl.map.dto.response.post.*;
import com.atl.map.dto.request.post.*;
import com.atl.map.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;

    @GetMapping("/{postid}")
    public ResponseEntity<? super GetPostResponseDto> getPost(
        @PathVariable("postid") Integer postId
    ){
        ResponseEntity<? super GetPostResponseDto> response = postService.getPost(postId);
        return response;
    }

    @PostMapping("")
    public ResponseEntity<? super CreatePostResponseDto> createPost(
        @RequestBody @Valid CreatePostRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super CreatePostResponseDto> response = postService.createPost(requestBody, email);
        return response;
    }

    @GetMapping("/my")
    public ResponseEntity<? super GetMyPostResponseDto> getMyPosts(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetMyPostResponseDto> response = postService.getUserPostList(email);
        return response;
    }

    @GetMapping("/my/favorite")
    public ResponseEntity<? super GetMyLikePostResponseDto> getMyLikePosts(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetMyLikePostResponseDto> response = postService.getUserLikePostList(email);
        return response;
    }

    @PutMapping("/{postid}/like")
    public ResponseEntity<? super PutFavoriteResponseDto> putLike(
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PutFavoriteResponseDto> response = postService.putFavorite(postId, email);
        return response;
    }

    @PatchMapping("/{postid}")
    public ResponseEntity<? super PatchPostResponseDto> patchPost(
        @RequestBody @Valid PatchPostRequestDto dto,
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PatchPostResponseDto> response = postService.patchPost(dto, postId, email);
        return response;
    }

    @DeleteMapping("/{postid}")
    public ResponseEntity<? super DeletePostResponseDto> deletePost( 
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super DeletePostResponseDto> response = postService.deletePost(postId, email);
        return response;
    }

    @PostMapping("/{postid}/comment")
    public ResponseEntity<? super PostCommentResponseDto> postComment(
        @RequestBody @Valid PostCommentRequestDto requestBody, 
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PostCommentResponseDto> response = postService.postComment(requestBody, postId, email);
        return response;
    }

    @GetMapping("/latest-list")
    public ResponseEntity<? super GetLatestPostResponseDto> getLatestPostList(){
        ResponseEntity<? super GetLatestPostResponseDto> response = postService.getLatestPostList();
        return response;
    }

    @GetMapping("/top")
    public ResponseEntity<? super GetTopPostListResponseDto> getTopPostList(){
        ResponseEntity<? super GetTopPostListResponseDto> response = postService.getTopPostList();
        return response;
    }

    @GetMapping("/search-list/{searchword}")
    public ResponseEntity<? super GetSearchPostListResponseDto> getSearchPost(
        @PathVariable("searchword") String searchWord
    ){
        ResponseEntity<? super GetSearchPostListResponseDto> response = postService.getSearchPostList(searchWord);
        return response;
    }


    @GetMapping("/{postid}/comment-list")
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(
        @PathVariable("postid") Integer postId
    ){
        ResponseEntity<? super GetCommentListResponseDto> response = postService.getCommentList(postId);
        return response;
    }

    @GetMapping("/building/{buildingid}")
    public ResponseEntity<? super GetBuildingPostListResponseDto> GetbuildingPostList(
        @PathVariable("buildingid") Integer buildingId
    ){
        ResponseEntity<? super GetBuildingPostListResponseDto> response = postService.getBuildingPostList(buildingId);
        return response;
    }

    @GetMapping("/{commentId}/child-comments")
    public ResponseEntity<? super GetChildCommentListResponseDto> getChildComments(
        @PathVariable("commentId") Integer commentId
    ){
        ResponseEntity<? super GetChildCommentListResponseDto> response = postService.getChildCommentList(commentId);
        return response;
    }
    
    @PostMapping("/{postid}/{commentId}/comment")
    public ResponseEntity<? super PostChildCommentResponseDto> postChildComments(
        @RequestBody @Valid PostChildCommentRequestDto requestBody,
        @PathVariable("postid") Integer postId,
        @PathVariable("commentId") Integer commentId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PostChildCommentResponseDto> response = postService.postChildComment(requestBody, email, postId, commentId);
        return response;
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComments(
        @PathVariable("commentId") Integer commentId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super DeleteCommentResponseDto> response = postService.deleteComment(email, commentId);
        return response;
    }

    
    @GetMapping("/comment/my")
    public ResponseEntity<? super GetMyCommentListResponseDto> getMyComments(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetMyCommentListResponseDto> response = postService.getMyComment(email);
        return response;
    }
}
