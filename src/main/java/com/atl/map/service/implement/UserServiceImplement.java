package com.atl.map.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.atl.map.dto.request.user.PatchNicknameRequestDto;
import com.atl.map.dto.request.user.PatchProfileImageRequestDto;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.user.GetSignInUserResponseDto;
import com.atl.map.dto.response.user.GetUserResponseDto;
import com.atl.map.dto.response.user.PatchNicknameResponseDto;
import com.atl.map.dto.response.user.PatchProfileImageResponseDto;
import com.atl.map.dto.response.user.PostReportResponseDto;
import com.atl.map.entity.ReportEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.repository.ReportRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
   
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Override
    public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email) {

        UserEntity userEntity = null;

        try{

            userEntity = userRepository.findByEmail(email);
            if(userEntity == null) return GetSignInUserResponseDto.noExistUser();

        }catch(Exception exception){
            log.error("로그인 사용자 조회 실패 - email: {}", email, exception);
            return ResponseDto.databaseError();
        }

        return GetSignInUserResponseDto.success(userEntity);

    }

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(String email) {
       
        UserEntity userEntity = null;

        try{
           userEntity = userRepository.findByEmail(email);
           if(userEntity == null) return GetUserResponseDto.noExistUser();

        }catch(Exception exception){
            log.error("사용자 조회 실패 - email: {}", email, exception);
            return ResponseDto.databaseError();
        }

        return GetUserResponseDto.success(userEntity);
    }

    @Override
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto, String email) {
       
        try{

            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null) return PatchNicknameResponseDto.noExistUser();
            
            String nickname = dto.getNickname();
            boolean existedNickname = nickname != null && userRepository.existsByNickname(nickname);
            if(existedNickname) return PatchNicknameResponseDto.duplicateNickname();

            userEntity.setNickname(nickname);
            userRepository.save(userEntity);
 
         }catch(Exception exception){
             log.error("닉네임 변경 실패 - email: {}", email, exception);
             return ResponseDto.databaseError();
        }

        return PatchNicknameResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto dto, String email) {
        
        try{

            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null) return PatchProfileImageResponseDto.noExistUser();

            String profileImage = dto.getProfileImage();
            userEntity.setProfileImage(profileImage);
            userRepository.save(userEntity);
 
        }catch(Exception exception){
            log.error("프로필 이미지 변경 실패 - email: {}", email, exception);
            return ResponseDto.databaseError();
        }

        return PatchProfileImageResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostReportResponseDto> report(String email, int userid) {
        
        try{

            UserEntity reportUser = userRepository.findByEmail(email);
            if(reportUser == null) return PostReportResponseDto.noExistUser();            
            UserEntity reportedUser = userRepository.findByUserId(userid);
            if(reportedUser == null) return PostReportResponseDto.noExistUser();

            if(!reportRepository.existsByReportUserIdAndReportedUserId(reportUser.getUserId(), userid))
            {
               reportedUser.report();
               userRepository.save(reportedUser);
               ReportEntity reportEntity = new ReportEntity(reportUser.getUserId(), userid);
               reportRepository.save(reportEntity);
            }
 
        }catch(Exception exception){
            log.error("사용자 신고 실패 - reporter: {}, targetUserId: {}", email, userid, exception);
            return ResponseDto.databaseError();
        }

        return PostReportResponseDto.success();
    }
    
}
