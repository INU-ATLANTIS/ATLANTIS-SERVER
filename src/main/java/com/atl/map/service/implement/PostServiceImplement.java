package com.atl.map.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atl.map.dto.request.post.*;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.post.*;
import com.atl.map.entity.CommentEntity;
import com.atl.map.entity.FavoriteEntity;
import com.atl.map.entity.ImageEntity;
import com.atl.map.entity.PostEntity;
import com.atl.map.entity.PostListViewEntity;
import com.atl.map.entity.UserEntity;
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

        PostEntity postEntity = null;

        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return CreatePostResponseDto.notExistUser();

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity.getReportedCount() > 10) return CreatePostResponseDto.reportedUser();

            postEntity = new PostEntity(dto, userEntity.getUserId());
            postRepository.save(postEntity);

            int postId = postEntity.getPostId();
            List<String> postImageList = dto.getImageList();
            List<ImageEntity> imageEntities = new ArrayList<>();
            if (postImageList != null) {
                for (String image : postImageList) {
                    ImageEntity imageEntity = new ImageEntity(postId, image);
                    imageEntities.add(imageEntity);
                }
                imageRepository.saveAll(imageEntities);
            }

        } catch (Exception exception) {
            log.error("게시물 작성 실패 - user: {}", email, exception);
            return ResponseDto.databaseError();
        }

        return CreatePostResponseDto.success(postEntity.getPostId());
    }

    @Override
    public ResponseEntity<? super GetPostResponseDto> getPost(Integer postId) {

        GetPostResultSet resultSet = null;
        List<ImageEntity> imageEntities = new ArrayList<>();
        try {
            resultSet = postRepository.getPost(postId);
            if (resultSet == null) return GetPostResponseDto.notExistPost();
            imageEntities = imageRepository.findByPostId(postId);

        } catch (Exception exception) {
            log.error("게시물 조회 실패 - postId: {}", postId, exception);
            return ResponseDto.databaseError();
        }

        return GetPostResponseDto.success(resultSet, imageEntities);
    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer postId, String email) {

        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PutFavoriteResponseDto.notExistUser();

            PostEntity postEntity = postRepository.findByPostId(postId);
            if (postEntity == null) return PutFavoriteResponseDto.noExistPost();

            Integer userId = userRepository.findByEmail(email).getUserId();
            FavoriteEntity favoriteEntity = favoriteRepository.findByPostIdAndUserId(postId, userId);
            if (favoriteEntity == null) {
                favoriteEntity = new FavoriteEntity(userId, postId);
                favoriteRepository.save(favoriteEntity);
                postEntity.increaseLikeCount();
            } else {
                favoriteRepository.delete(favoriteEntity);
                postEntity.decreaseLikeCount();
            }

            postRepository.save(postEntity);

        } catch (Exception exception) {
            log.error("게시물 좋아요 처리 실패 - postId: {}, user: {}", postId, email, exception);
            return ResponseDto.databaseError();
        }

        return PutFavoriteResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer postId, String email) {

        try {
            PostEntity postEntity = postRepository.findByPostId(postId);
            if (postEntity == null) return PostCommentResponseDto.notExistPost();

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return PostCommentResponseDto.notExistUser();
            if (userEntity.getReportedCount() > 10) return PostCommentResponseDto.reportedUser();

            CommentEntity commentEntity = new CommentEntity(dto, postId, userEntity.getUserId());
            commentRepository.save(commentEntity);
            postEntity.increaseCommentCount();
            postRepository.save(postEntity);

        } catch (Exception exception) {
            log.error("댓글 작성 실패 - postId: {}, user: {}", postId, email, exception);
            return ResponseDto.databaseError();
        }

        return PostCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchPostResponseDto> patchPost(PatchPostRequestDto dto, Integer postId, String email) {
        try {
            PostEntity postEntity = postRepository.findByPostId(postId);
            if (postEntity == null) return PatchPostResponseDto.notExistPost();

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return PatchPostResponseDto.notExistUser();

            Integer writerInteger = postEntity.getUserId();
            if (writerInteger != userEntity.getUserId()) return PatchPostResponseDto.noPermisson();

            postEntity.patchPost(dto);
            postRepository.save(postEntity);

            if (dto.getImageList() != null) {
                imageRepository.deleteById(postId);
                List<String> imageList = dto.getImageList();
                List<ImageEntity> imageEntities = new ArrayList<>();

                for (String image : imageList) {
                    ImageEntity imageEntity = new ImageEntity(postId, image);
                    imageEntities.add(imageEntity);
                }
                imageRepository.saveAll(imageEntities);
            }
        } catch (Exception exception) {
            log.error("게시물 수정 실패 - postId: {}, user: {}", postId, email, exception);
            return ResponseDto.databaseError();
        }

        return PatchPostResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super DeletePostResponseDto> deletePost(Integer postId, String email) {

        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return DeletePostResponseDto.notExistUser();

            PostEntity postEntity = postRepository.findByPostId(postId);
            if (postEntity == null) return DeletePostResponseDto.notExistPost();

            if (postEntity.getUserId() != userEntity.getUserId()) return DeletePostResponseDto.noPermisson();

            imageRepository.deleteByPostId(postId);
            commentRepository.deleteByPostId(postId);
            favoriteRepository.deleteByPostId(postId);
            markerService.deleteMarkersAndNotificationsByPostId(postId);
            postRepository.delete(postEntity);

        } catch (Exception exception) {
            log.error("게시물 삭제 실패 - postId: {}, user: {}", postId, email, exception);
            return ResponseDto.databaseError();
        }
        return DeletePostResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetLatestPostResponseDto> getLatestPostList(int page, int size) {

        try {
            Page<PostListViewEntity> postPage =
                    postListViewRepository.findByOrderByWriteDatetimeDesc(createPageable(page, size));
            return GetLatestPostResponseDto.success(postPage);

        } catch (Exception exception) {
            log.error("최신 게시물 목록 조회 실패 - page: {}, size: {}", page, size, exception);
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetTopPostListResponseDto> getTopPostList() {

        List<PostListViewEntity> postListViewEntities = new ArrayList<>();

        try {
            LocalDateTime beforeWeek = LocalDateTime.now().minusWeeks(1);
            postListViewEntities = postListViewRepository.findTop10ByWriteDatetimeGreaterThanOrderByLikeCountDescCommentCountDesc(beforeWeek);

        } catch (Exception exception) {
            log.error("인기 게시물 목록 조회 실패", exception);
            return ResponseDto.databaseError();
        }

        return GetTopPostListResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetSearchPostListResponseDto> getSearchPostList(String searchWord, int page, int size) {

        try {
            Page<PostListViewEntity> postPage = postListViewRepository
                    .findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(
                            searchWord, searchWord, createPageable(page, size));
            return GetSearchPostListResponseDto.success(postPage);

        } catch (Exception exception) {
            log.error("게시물 검색 실패 - keyword: {}, page: {}, size: {}", searchWord, page, size, exception);
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer postId) {

        List<GetCommentListResultSet> resultSets = new ArrayList<>();
        try {
            boolean existedPost = postRepository.existsById(postId);
            if (!existedPost) return GetCommentListResponseDto.notExistPost();

            resultSets = commentRepository.getCommentList(postId);

        } catch (Exception exception) {
            log.error("댓글 목록 조회 실패 - postId: {}", postId, exception);
            return ResponseDto.databaseError();
        }

        return GetCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super GetBuildingPostListResponseDto> getBuildingPostList(Integer buildingId, int page, int size) {
        try {
            Page<PostListViewEntity> postPage =
                    postListViewRepository.findByBuildingIdOrderByWriteDatetimeDesc(buildingId, createPageable(page, size));
            return GetBuildingPostListResponseDto.success(postPage);

        } catch (Exception exception) {
            log.error("건물별 게시물 목록 조회 실패 - buildingId: {}, page: {}, size: {}", buildingId, page, size, exception);
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetChildCommentListResponseDto> getChildCommentList(Integer parentId) {

        List<GetCommentListResultSet> resultSets = new ArrayList<>();
        try {
            boolean existedComment = commentRepository.existsById(parentId);
            if (!existedComment) return GetChildCommentListResponseDto.notExistComment();

            resultSets = commentRepository.getChildCommentList(parentId);

        } catch (Exception exception) {
            log.error("대댓글 목록 조회 실패 - parentId: {}", parentId, exception);
            return ResponseDto.databaseError();
        }

        return GetChildCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super PostChildCommentResponseDto> postChildComment(PostChildCommentRequestDto dto, String email, Integer postId, Integer commentId) {

        try {
            PostEntity postEntity = postRepository.findByPostId(postId);
            if (postEntity == null) return PostChildCommentResponseDto.notExistPost();

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return PostChildCommentResponseDto.notExistUser();
            if (userEntity.getReportedCount() > 10) return PostChildCommentResponseDto.reportedUser();

            CommentEntity commentEntity = new CommentEntity(dto, postId, commentId, userEntity.getUserId());
            commentRepository.save(commentEntity);
            postEntity.increaseCommentCount();
            postRepository.save(postEntity);

        } catch (Exception exception) {
            log.error("대댓글 작성 실패 - postId: {}, user: {}", postId, email, exception);
            return ResponseDto.databaseError();
        }

        return PostChildCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetMyPostResponseDto> getUserPostList(String email) {
        List<PostListViewEntity> postListViewEntities = new ArrayList<>();
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return GetMyPostResponseDto.notExistUser();
            postListViewEntities = postListViewRepository.findByUserIdOrderByWriteDatetimeDesc(userEntity.getUserId());

        } catch (Exception exception) {
            log.error("내 게시물 목록 조회 실패 - user: {}", email, exception);
            return ResponseDto.databaseError();
        }

        return GetMyPostResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComment(String email, Integer commentId) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return DeletePostResponseDto.notExistUser();

            CommentEntity commentEntity = commentRepository.findByCommentId(commentId);
            if (commentEntity == null) return DeleteCommentResponseDto.notExistComment();

            if (commentEntity.getUserId() != userEntity.getUserId()) return DeleteCommentResponseDto.noPermisson();

            PostEntity postEntity = postRepository.findByPostId(commentEntity.getPostId());

            int deletedCommentsCount = deleteChildComments(commentId);

            commentRepository.delete(commentEntity);

            postEntity.decreaseCommentCount(deletedCommentsCount + 1);

            postRepository.save(postEntity);
        } catch (Exception exception) {
            log.error("댓글 삭제 실패 - commentId: {}, user: {}", commentId, email, exception);
            return ResponseDto.databaseError();
        }
        return DeleteCommentResponseDto.success();
    }

    private int deleteChildComments(Integer parentId) {
        List<CommentEntity> childComments = commentRepository.findByParentId(parentId);
        int deleteCount = 0;
        for (CommentEntity childComment : childComments) {
            deleteCount += deleteChildComments(childComment.getCommentId());
            commentRepository.delete(childComment);
            deleteCount++;
        }
        return deleteCount;
    }

    @Override
    public ResponseEntity<? super GetMyLikePostResponseDto> getUserLikePostList(String email) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return GetMyLikePostResponseDto.notExistUser();

            List<FavoriteEntity> favorites = favoriteRepository.findByUserId(userEntity.getUserId());
            List<Integer> postIds = favorites.stream()
                    .map(FavoriteEntity::getPostId)
                    .collect(Collectors.toList());

            List<PostListViewEntity> postListViewEntities = postListViewRepository.findAllById(postIds);

            return GetMyLikePostResponseDto.success(postListViewEntities);
        } catch (Exception exception) {
            log.error("좋아요한 게시물 목록 조회 실패 - user: {}", email, exception);
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetMyCommentListResponseDto> getMyComment(String email) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return GetMyCommentListResponseDto.notExistUser();

            List<CommentEntity> list = commentRepository.findByUserId(userEntity.getUserId());

            return GetMyCommentListResponseDto.success(list);

        } catch (Exception exception) {
            log.error("내 댓글 목록 조회 실패 - user: {}", email, exception);
            return ResponseDto.databaseError();
        }
    }

    private Pageable createPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(safePage, safeSize);
    }
}