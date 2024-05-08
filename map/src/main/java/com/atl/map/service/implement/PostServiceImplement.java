package com.atl.map.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.atl.map.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImplement implements PostService {
    
    
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final PostListViewRepository postListViewRepository;
    

    @Transactional
    @Override
    public ResponseEntity<? super CreatePostResponseDto> createPost(CreatePostRequestDto dto, String email) {
        
        PostEntity postEntity = null;

        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail) return CreatePostResponseDto.notExistUser();

            UserEntity userEntity = userRepository.findByEmail(email);
            postEntity = new PostEntity(dto, userEntity.getUserId());
            postRepository.save(postEntity);

            int postId = postEntity.getPostId();
            List<String> postImageList = dto.getImageList();
            List<ImageEntity> imageEntities = new ArrayList<>();
            if(postImageList!=null){
                for(String image: postImageList)
                {
                    ImageEntity imageEntity = new ImageEntity(postId, image);
                    imageEntities.add(imageEntity);
                }
                imageRepository.saveAll(imageEntities);
            }

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreatePostResponseDto.success(postEntity.getPostId());

    }

    @Override
    public ResponseEntity<? super GetPostResponseDto> getPost(Integer postId) {
     
        GetPostResultSet resultSet = null;
        List<ImageEntity> imageEntities = new ArrayList<>();
        try{
 
            resultSet = postRepository.getPost(postId);
            if(resultSet == null) return GetPostResponseDto.notExistPost();
            imageEntities = imageRepository.findByPostId(postId);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetPostResponseDto.success(resultSet, imageEntities);
    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer postId, String email) {
    
        try{

            boolean existedEmail =userRepository.existsByEmail(email);
            if(!existedEmail) return PutFavoriteResponseDto.notExistUser();

            PostEntity postEntity = postRepository.findByPostId(postId);
            if(postEntity == null) return PutFavoriteResponseDto.noExistPost();

            Integer userId = userRepository.findByEmail(email).getUserId();
            FavoriteEntity favoriteEntity = favoriteRepository.findByPostIdAndUserId(postId, userId);
            if(favoriteEntity==null){
                favoriteEntity = new FavoriteEntity(userId, postId);
                favoriteRepository.save(favoriteEntity);
                postEntity.increaseLikeCount();
            }else{
                favoriteRepository.delete(favoriteEntity);
                postEntity.decreaseLikeCount();
            }

            postRepository.save(postEntity);


        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PutFavoriteResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer postId, String email) {
    
        try{

            PostEntity postEntity = postRepository.findByPostId(postId);
            if(postEntity == null) return PostCommentResponseDto.notExistPost();

            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail) return PostCommentResponseDto.notExistUser();

            CommentEntity commentEntity = new CommentEntity(dto, postId, userRepository.findByEmail(email).getUserId());
            commentRepository.save(commentEntity);
            postEntity.increaseCommentCount();
            postRepository.save(postEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchPostResponseDto> patchPost(PatchPostRequestDto dto, Integer postId, String email) 
    {
        try{

        PostEntity postEntity = postRepository.findByPostId(postId);
        if(postEntity == null) return PatchPostResponseDto.notExistPost();

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) return PatchPostResponseDto.notExistUser();

        Integer writerInteger = postEntity.getUserId();
        if(writerInteger != userEntity.getUserId()) return PatchPostResponseDto.noPermisson();

        postEntity.patchPost(dto);
        postRepository.save(postEntity);

        if(dto.getImageList()!= null){
            imageRepository.deleteById(postId);
            List<String> imageList = dto.getImageList();
            List<ImageEntity> imageEntities = new ArrayList<>();
    
            for(String image: imageList){
                ImageEntity imageEntity = new ImageEntity(postId, image);
                imageEntities.add(imageEntity);
            }
            imageRepository.saveAll(imageEntities);
        }
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchPostResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeletePostResponseDto> deletePost(Integer postId, String email) {
        
        try
        {
            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null ) return DeletePostResponseDto.notExistUser();

            PostEntity postEntity = postRepository.findByPostId(postId);
            if(postEntity == null) return DeletePostResponseDto.notExistPost();

            if(postEntity.getUserId() != userEntity.getUserId()) return DeletePostResponseDto.noPermisson();

            imageRepository.deleteByPostId(postId);
            commentRepository.deleteByPostId(postId);
            favoriteRepository.deleteByPostId(postId);
            postRepository.delete(postEntity);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeletePostResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetLatestPostResponseDto> getLatestPostList() {
    
        List<PostListViewEntity> postListViewEntities = new ArrayList<>();
        try{
            postListViewEntities = postListViewRepository.findByOrderByWriteDatetimeDesc();

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetLatestPostResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetTopPostListResponseDto> getTopPostList() {
    
        List<PostListViewEntity> postListViewEntities = new ArrayList<>();

        try{

            LocalDateTime beforeWeek = LocalDateTime.now().minusWeeks(1);
            postListViewEntities = postListViewRepository.findTop10ByWriteDatetimeGreaterThanOrderByLikeCountDescCommentCountDesc(beforeWeek);
        
        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetTopPostListResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetSearchPostListResponseDto> getSearchPostList(String searchWord) {
    
        List<PostListViewEntity> postListViewEntities = new ArrayList<>();
        try{
            postListViewEntities = postListViewRepository.findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(searchWord, searchWord);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        
        return GetSearchPostListResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer postId) {
    
        List<GetCommentListResultSet> resultSets = new ArrayList<>();
        try{

            boolean existedPost = postRepository.existsById(postId);
            if(!existedPost) return GetCommentListResponseDto.notExistPost();

            resultSets = commentRepository.getCommentList(postId);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super GetBuildingPostListResponseDto> getBuildingPostList(Integer buildingId) {
        List<PostListViewEntity> postListViewEntities = new ArrayList<>();
        try {
            postListViewEntities = postListViewRepository.findByBuildingIdOrderByWriteDatetimeDesc(buildingId);
    
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    
        return GetBuildingPostListResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetChildCommentListResponseDto> getChildCommentList(Integer parentId) {
        
        List<GetCommentListResultSet> resultSets = new ArrayList<>();
        try{

            boolean existedComment = commentRepository.existsById(parentId);
            if(!existedComment) return GetChildCommentListResponseDto.notExistComment();

            resultSets = commentRepository.getChildCommentList(parentId);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetChildCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super PostChildCommentResponseDto> postChildComment(PostChildCommentRequestDto dto, String email, Integer postId, Integer commentId) {
        
        try{

            PostEntity postEntity = postRepository.findByPostId(postId);
            if(postEntity == null) return PostChildCommentResponseDto.notExistPost();

            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail) return PostChildCommentResponseDto.notExistUser();

            CommentEntity commentEntity = new CommentEntity(dto, postId, commentId, userRepository.findByEmail(email).getUserId());
            commentRepository.save(commentEntity);
            postEntity.increaseCommentCount();
            postRepository.save(postEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostChildCommentResponseDto.success();

    }

    @Override
    public ResponseEntity<? super GetMyPostResponseDto> getUserPostList(String email) {
        List<PostListViewEntity> postListViewEntities = new ArrayList<>();
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null) return GetMyPostResponseDto.notExistUser();
            postListViewEntities = postListViewRepository.findByUserIdOrderByWriteDatetimeDesc(userEntity.getUserId());
    
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    
        return GetMyPostResponseDto.success(postListViewEntities);
    }

    @Override
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComment(String email, Integer commentId) {
        
        try
        {
            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null ) return DeletePostResponseDto.notExistUser();

            CommentEntity commentEntity = commentRepository.findByCommentId(commentId);
            if(commentEntity == null) return DeleteCommentResponseDto.notExistComment();

            if(commentEntity.getUserId() != userEntity.getUserId()) return DeleteCommentResponseDto.noPermisson();

            commentRepository.delete(commentEntity);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteCommentResponseDto.success();
    }
    
}
