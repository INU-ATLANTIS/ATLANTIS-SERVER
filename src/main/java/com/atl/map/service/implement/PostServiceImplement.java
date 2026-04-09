package com.atl.map.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atl.map.dto.request.post.CreatePostRequestDto;
import com.atl.map.dto.request.post.PatchPostRequestDto;
import com.atl.map.dto.request.post.PostChildCommentRequestDto;
import com.atl.map.dto.request.post.PostCommentRequestDto;
import com.atl.map.dto.response.post.CreatePostResponseDto;
import com.atl.map.dto.response.post.DeleteCommentResponseDto;
import com.atl.map.dto.response.post.DeletePostResponseDto;
import com.atl.map.dto.response.post.GetBuildingPostListResponseDto;
import com.atl.map.dto.response.post.GetChildCommentListResponseDto;
import com.atl.map.dto.response.post.GetCommentListResponseDto;
import com.atl.map.dto.response.post.GetLatestPostResponseDto;
import com.atl.map.dto.response.post.GetMyCommentListResponseDto;
import com.atl.map.dto.response.post.GetMyLikePostResponseDto;
import com.atl.map.dto.response.post.GetMyPostResponseDto;
import com.atl.map.dto.response.post.GetPostResponseDto;
import com.atl.map.dto.response.post.GetSearchPostListResponseDto;
import com.atl.map.dto.response.post.GetTopPostListResponseDto;
import com.atl.map.dto.response.post.PatchPostResponseDto;
import com.atl.map.dto.response.post.PostChildCommentResponseDto;
import com.atl.map.dto.response.post.PostCommentResponseDto;
import com.atl.map.dto.response.post.PutFavoriteResponseDto;
import com.atl.map.entity.CommentEntity;
import com.atl.map.entity.FavoriteEntity;
import com.atl.map.entity.ImageEntity;
import com.atl.map.entity.PostEntity;
import com.atl.map.entity.PostListViewEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
import com.atl.map.repository.CommentRepository;
import com.atl.map.repository.FavoriteRepository;
import com.atl.map.repository.ImageRepository;
import com.atl.map.repository.PostListViewRepository;
import com.atl.map.repository.PostRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.repository.resultSet.GetCommentListResultSet;
import com.atl.map.repository.resultSet.GetPostResultSet;
import com.atl.map.service.MarkerService;
import com.atl.map.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImplement implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final PostListViewRepository postListViewRepository;
    private final MarkerService markerService;

    @Transactional
    @Override
    public ResponseEntity<? super CreatePostResponseDto> createPost(CreatePostRequestDto dto, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (userEntity.getReportedCount() > 10) throw new BusinessException(ErrorCode.REPORTED_USER);

        PostEntity postEntity = new PostEntity(dto, userEntity.getUserId());
        postRepository.save(postEntity);

        List<String> postImageList = dto.getImageList();
        if (postImageList != null) {
            List<ImageEntity> imageEntities = new ArrayList<>();
            for (String image : postImageList) {
                imageEntities.add(new ImageEntity(postEntity.getPostId(), image));
            }
            imageRepository.saveAll(imageEntities);
        }

        return CreatePostResponseDto.success(postEntity.getPostId());
    }

    @Override
    public ResponseEntity<? super GetPostResponseDto> getPost(Integer postId) {
        GetPostResultSet resultSet = postRepository.getPost(postId);
        if (resultSet == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        List<ImageEntity> imageEntities = imageRepository.findByPostId(postId);
        return GetPostResponseDto.success(resultSet, imageEntities);
    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer postId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        FavoriteEntity favoriteEntity = favoriteRepository.findByPostIdAndUserId(postId, userEntity.getUserId());
        if (favoriteEntity == null) {
            favoriteRepository.save(new FavoriteEntity(userEntity.getUserId(), postId));
            postEntity.increaseLikeCount();
        } else {
            favoriteRepository.delete(favoriteEntity);
            postEntity.decreaseLikeCount();
        }
        postRepository.save(postEntity);

        return PutFavoriteResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer postId, String email) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (userEntity.getReportedCount() > 10) throw new BusinessException(ErrorCode.REPORTED_USER);

        CommentEntity commentEntity = new CommentEntity(dto, postId, userEntity.getUserId());
        commentRepository.save(commentEntity);
        postEntity.increaseCommentCount();
        postRepository.save(postEntity);

        return PostCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchPostResponseDto> patchPost(PatchPostRequestDto dto, Integer postId, String email) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (postEntity.getUserId() != userEntity.getUserId()) throw new BusinessException(ErrorCode.NO_PERMISSION);

        postEntity.patchPost(dto);
        postRepository.save(postEntity);

        if (dto.getImageList() != null) {
            imageRepository.deleteById(postId);
            List<ImageEntity> imageEntities = new ArrayList<>();
            for (String image : dto.getImageList()) {
                imageEntities.add(new ImageEntity(postId, image));
            }
            imageRepository.saveAll(imageEntities);
        }

        return PatchPostResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super DeletePostResponseDto> deletePost(Integer postId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        if (postEntity.getUserId() != userEntity.getUserId()) throw new BusinessException(ErrorCode.NO_PERMISSION);

        imageRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        favoriteRepository.deleteByPostId(postId);
        markerService.deleteMarkersAndNotificationsByPostId(postId);
        postRepository.delete(postEntity);

        return DeletePostResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetLatestPostResponseDto> getLatestPostList(int page, int size) {
        Page<PostListViewEntity> postPage =
                postListViewRepository.findByOrderByWriteDatetimeDescPostIdDesc(createPageable(page, size));
        return GetLatestPostResponseDto.success(postPage);
    }

    @Override
    public ResponseEntity<? super GetTopPostListResponseDto> getTopPostList() {
        LocalDateTime beforeWeek = LocalDateTime.now().minusWeeks(1);
        List<PostListViewEntity> postListViewEntities =
                postListViewRepository.findTop10ByWriteDatetimeGreaterThanOrderByLikeCountDescCommentCountDesc(beforeWeek);
        return GetTopPostListResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetSearchPostListResponseDto> getSearchPostList(String searchWord, int page, int size) {
        Page<PostListViewEntity> postPage = postListViewRepository
                .findByTitleContainsOrContentContainsOrderByWriteDatetimeDescPostIdDesc(
                        searchWord, searchWord, createPageable(page, size));
        return GetSearchPostListResponseDto.success(postPage);
    }

    @Override
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer postId) {
        if (!postRepository.existsById(postId)) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        List<GetCommentListResultSet> resultSets = commentRepository.getCommentList(postId);
        return GetCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super GetBuildingPostListResponseDto> getBuildingPostList(Integer buildingId, int page, int size) {
        Page<PostListViewEntity> postPage =
                postListViewRepository.findByBuildingIdOrderByWriteDatetimeDescPostIdDesc(buildingId, createPageable(page, size));
        return GetBuildingPostListResponseDto.success(postPage);
    }

    @Override
    public ResponseEntity<? super GetChildCommentListResponseDto> getChildCommentList(Integer parentId) {
        if (!commentRepository.existsById(parentId)) throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);

        List<GetCommentListResultSet> resultSets = commentRepository.getChildCommentList(parentId);
        return GetChildCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super PostChildCommentResponseDto> postChildComment(PostChildCommentRequestDto dto, String email, Integer postId, Integer commentId) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (userEntity.getReportedCount() > 10) throw new BusinessException(ErrorCode.REPORTED_USER);

        CommentEntity commentEntity = new CommentEntity(dto, postId, commentId, userEntity.getUserId());
        commentRepository.save(commentEntity);
        postEntity.increaseCommentCount();
        postRepository.save(postEntity);

        return PostChildCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetMyPostResponseDto> getUserPostList(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        List<PostListViewEntity> postListViewEntities =
                postListViewRepository.findByUserIdOrderByWriteDatetimeDesc(userEntity.getUserId());
        return GetMyPostResponseDto.success(postListViewEntities);
    }

    @Transactional
    @Override
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComment(String email, Integer commentId) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        CommentEntity commentEntity = commentRepository.findByCommentId(commentId);
        if (commentEntity == null) throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        if (commentEntity.getUserId() != userEntity.getUserId()) throw new BusinessException(ErrorCode.NO_PERMISSION);

        PostEntity postEntity = postRepository.findByPostId(commentEntity.getPostId());
        List<CommentEntity> postComments = commentRepository.findByPostId(commentEntity.getPostId());
        List<CommentEntity> commentsToDelete = collectCommentsToDelete(commentId, postComments);

        commentRepository.deleteAllInBatch(commentsToDelete);
        postEntity.decreaseCommentCount(commentsToDelete.size());
        postRepository.save(postEntity);

        return DeleteCommentResponseDto.success();
    }

    private List<CommentEntity> collectCommentsToDelete(Integer rootCommentId, List<CommentEntity> postComments) {
        Map<Integer, List<CommentEntity>> childrenByParentId = new HashMap<>();
        CommentEntity rootComment = null;

        for (CommentEntity postComment : postComments) {
            if (postComment.getCommentId() == rootCommentId) {
                rootComment = postComment;
            }

            Integer parentId = postComment.getParentId();
            if (parentId != null) {
                childrenByParentId
                        .computeIfAbsent(parentId, key -> new ArrayList<>())
                        .add(postComment);
            }
        }

        List<CommentEntity> commentsToDelete = new ArrayList<>();
        if (rootComment != null) {
            collectDescendants(rootComment, childrenByParentId, commentsToDelete);
        }

        return commentsToDelete;
    }

    private void collectDescendants(
            CommentEntity currentComment,
            Map<Integer, List<CommentEntity>> childrenByParentId,
            List<CommentEntity> commentsToDelete
    ) {
        commentsToDelete.add(currentComment);

        List<CommentEntity> childComments =
                childrenByParentId.getOrDefault(currentComment.getCommentId(), List.of());
        for (CommentEntity childComment : childComments) {
            collectDescendants(childComment, childrenByParentId, commentsToDelete);
        }
    }

    @Override
    public ResponseEntity<? super GetMyLikePostResponseDto> getUserLikePostList(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        List<PostListViewEntity> postListViewEntities =
                postListViewRepository.findLikedPostsByUserId(userEntity.getUserId());

        return GetMyLikePostResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetMyCommentListResponseDto> getMyComment(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        List<CommentEntity> list = commentRepository.findByUserId(userEntity.getUserId());
        return GetMyCommentListResponseDto.success(list);
    }

    private Pageable createPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(safePage, safeSize);
    }
}
