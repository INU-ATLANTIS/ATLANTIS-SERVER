package com.atl.map.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.post.DeleteCommentResponseDto;
import com.atl.map.entity.CommentEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.repository.CommentRepository;
import com.atl.map.repository.FavoriteRepository;
import com.atl.map.repository.ImageRepository;
import com.atl.map.repository.PostListViewRepository;
import com.atl.map.repository.PostRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.CachedLookupService;
import com.atl.map.service.MarkerService;

@ExtendWith(MockitoExtension.class)
class PostServiceImplementTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostListViewRepository postListViewRepository;
    @Mock
    private MarkerService markerService;
    @Mock
    private CachedLookupService cachedLookupService;

    private PostServiceImplement postService;

    @BeforeEach
    void setUp() {
        postService = new PostServiceImplement(
                userRepository,
                postRepository,
                imageRepository,
                favoriteRepository,
                commentRepository,
                postListViewRepository,
                markerService,
                cachedLookupService
        );
    }

    @Test
    @DisplayName("댓글 삭제 시 자식 댓글까지 함께 삭제하고 댓글 수를 정확히 감소시킨다")
    void deleteCommentDeletesDescendantsAndDecreasesCommentCount() {
        UserEntity userEntity = new UserEntity(
                1,
                "test@example.com",
                "encoded-password",
                null,
                null,
                "tester",
                "ROLE_USER",
                null,
                0
        );
        CommentEntity rootComment = new CommentEntity(1, 1, 10, "root", 0, null, null, null);
        CommentEntity childComment = new CommentEntity(2, 2, 10, "child", 0, null, null, 1);
        CommentEntity unrelatedComment = new CommentEntity(3, 3, 10, "other", 0, null, null, null);

        when(userRepository.findByEmail("test@example.com")).thenReturn(userEntity);
        when(commentRepository.findByCommentId(1)).thenReturn(rootComment);
        when(commentRepository.findByPostId(10)).thenReturn(List.of(rootComment, childComment, unrelatedComment));
        when(postRepository.decreaseCommentCount(10, 2)).thenReturn(1);

        ResponseEntity<? super DeleteCommentResponseDto> response =
                postService.deleteComment("test@example.com", 1);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ArgumentCaptor<List<CommentEntity>> deletedCommentsCaptor = ArgumentCaptor.forClass(List.class);
        verify(commentRepository).deleteAllInBatch(deletedCommentsCaptor.capture());
        verify(postRepository).decreaseCommentCount(eq(10), eq(2));

        List<CommentEntity> deletedComments = deletedCommentsCaptor.getValue();
        assertThat(deletedComments)
                .extracting(CommentEntity::getCommentId)
                .containsExactlyInAnyOrder(1, 2);
    }
}
