package com.atl.map.service.implement;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atl.map.dto.request.post.CreatePostRequestDto;
import com.atl.map.dto.request.post.PatchPostRequestDto;
import com.atl.map.dto.request.post.PostCommentRequestDto;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.post.CreatePostResponseDto;
import com.atl.map.dto.response.post.DeletePostResponseDto;
import com.atl.map.dto.response.post.GetCommentListResponseDto;
import com.atl.map.dto.response.post.GetLatestPostResponseDto;
import com.atl.map.dto.response.post.GetPostResponseDto;
import com.atl.map.dto.response.post.GetSearchPostListResponseDto;
import com.atl.map.dto.response.post.GetTopPostListResponseDto;
import com.atl.map.dto.response.post.PatchPostResponseDto;
import com.atl.map.dto.response.post.PostCommentResponseDto;
import com.atl.map.dto.response.post.PutFavoriteResponseDto;
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
        

        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail) return CreatePostResponseDto.notExistUser();

            UserEntity userEntity = userRepository.findByEmail(email);
            PostEntity postEntity = new PostEntity(dto, userEntity.getUserId());
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

        return CreatePostResponseDto.success();

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
    
}
