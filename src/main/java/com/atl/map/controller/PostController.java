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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.atl.map.dto.response.post.*;
import com.atl.map.dto.request.post.*;
import com.atl.map.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 및 댓글 관련 API")
public class PostController {
    
    private final PostService postService;

    @GetMapping("/{postid}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보와 이미지 목록을 조회합니다.")
    public ResponseEntity<? super GetPostResponseDto> getPost(
        @PathVariable("postid") Integer postId
    ){
        ResponseEntity<? super GetPostResponseDto> response = postService.getPost(postId);
        return response;
    }

    @PostMapping("")
    @Operation(summary = "게시글 작성", description = "새 게시글을 등록합니다.")
    public ResponseEntity<? super CreatePostResponseDto> createPost(
        @RequestBody @Valid CreatePostRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super CreatePostResponseDto> response = postService.createPost(requestBody, email);
        return response;
    }

    @GetMapping("/my")
    @Operation(summary = "내 게시글 목록 조회", description = "현재 로그인 사용자가 작성한 게시글 목록을 조회합니다.")
    public ResponseEntity<? super GetMyPostResponseDto> getMyPosts(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetMyPostResponseDto> response = postService.getUserPostList(email);
        return response;
    }

    @GetMapping("/my/favorite")
    @Operation(summary = "내 좋아요 게시글 조회", description = "현재 로그인 사용자가 좋아요한 게시글 목록을 조회합니다.")
    public ResponseEntity<? super GetMyLikePostResponseDto> getMyLikePosts(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetMyLikePostResponseDto> response = postService.getUserLikePostList(email);
        return response;
    }

    @PutMapping("/{postid}/like")
    @Operation(summary = "게시글 좋아요 토글", description = "게시글 좋아요를 추가하거나 취소합니다.")
    public ResponseEntity<? super PutFavoriteResponseDto> putLike(
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PutFavoriteResponseDto> response = postService.putFavorite(postId, email);
        return response;
    }

    @PatchMapping("/{postid}")
    @Operation(summary = "게시글 수정", description = "게시글 내용과 이미지 구성을 수정합니다.")
    public ResponseEntity<? super PatchPostResponseDto> patchPost(
        @RequestBody @Valid PatchPostRequestDto dto,
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PatchPostResponseDto> response = postService.patchPost(dto, postId, email);
        return response;
    }

    @DeleteMapping("/{postid}")
    @Operation(summary = "게시글 삭제", description = "게시글과 연관된 이미지/좋아요 정보를 함께 삭제합니다.")
    public ResponseEntity<? super DeletePostResponseDto> deletePost( 
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super DeletePostResponseDto> response = postService.deletePost(postId, email);
        return response;
    }

    @PostMapping("/{postid}/comment")
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public ResponseEntity<? super PostCommentResponseDto> postComment(
        @RequestBody @Valid PostCommentRequestDto requestBody, 
        @PathVariable("postid") Integer postId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PostCommentResponseDto> response = postService.postComment(requestBody, postId, email);
        return response;
    }

    @GetMapping("/latest-list")
    @Operation(summary = "최신 게시글 목록 조회", description = "페이지네이션 기반 최신 게시글 목록을 조회합니다.")
    public ResponseEntity<? super GetLatestPostResponseDto> getLatestPostList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ){
        ResponseEntity<? super GetLatestPostResponseDto> response = postService.getLatestPostList(page, size);
        return response;
    }

    @GetMapping("/top")
    @Operation(summary = "인기 게시글 조회", description = "최근 기간 기준 인기 게시글 목록을 조회합니다.")
    public ResponseEntity<? super GetTopPostListResponseDto> getTopPostList(){
        ResponseEntity<? super GetTopPostListResponseDto> response = postService.getTopPostList();
        return response;
    }

    @GetMapping("/search-list/{searchword}")
    @Operation(summary = "게시글 검색", description = "키워드로 게시글 제목 및 내용을 검색합니다.")
    public ResponseEntity<? super GetSearchPostListResponseDto> getSearchPost(
        @PathVariable("searchword") String searchWord,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ){
        ResponseEntity<? super GetSearchPostListResponseDto> response = postService.getSearchPostList(searchWord, page, size);
        return response;
    }


    @GetMapping("/{postid}/comment-list")
    @Operation(summary = "댓글 목록 조회", description = "게시글의 댓글 목록을 조회합니다.")
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(
        @PathVariable("postid") Integer postId
    ){
        ResponseEntity<? super GetCommentListResponseDto> response = postService.getCommentList(postId);
        return response;
    }

    @GetMapping("/building/{buildingid}")
    @Operation(summary = "건물별 게시글 조회", description = "특정 건물에 속한 게시글 목록을 조회합니다.")
    public ResponseEntity<? super GetBuildingPostListResponseDto> GetbuildingPostList(
        @PathVariable("buildingid") Integer buildingId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ){
        ResponseEntity<? super GetBuildingPostListResponseDto> response = postService.getBuildingPostList(buildingId, page, size);
        return response;
    }

    @GetMapping("/{commentId}/child-comments")
    @Operation(summary = "대댓글 목록 조회", description = "특정 댓글의 대댓글 목록을 조회합니다.")
    public ResponseEntity<? super GetChildCommentListResponseDto> getChildComments(
        @PathVariable("commentId") Integer commentId
    ){
        ResponseEntity<? super GetChildCommentListResponseDto> response = postService.getChildCommentList(commentId);
        return response;
    }
    
    @PostMapping("/{postid}/{commentId}/comment")
    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대한 대댓글을 작성합니다.")
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
    @Operation(summary = "댓글 삭제", description = "댓글과 그 하위 대댓글을 함께 삭제합니다.")
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComments(
        @PathVariable("commentId") Integer commentId,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super DeleteCommentResponseDto> response = postService.deleteComment(email, commentId);
        return response;
    }

    
    @GetMapping("/comment/my")
    @Operation(summary = "내 댓글 목록 조회", description = "현재 로그인 사용자가 작성한 댓글 목록을 조회합니다.")
    public ResponseEntity<? super GetMyCommentListResponseDto> getMyComments(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetMyCommentListResponseDto> response = postService.getMyComment(email);
        return response;
    }
}
