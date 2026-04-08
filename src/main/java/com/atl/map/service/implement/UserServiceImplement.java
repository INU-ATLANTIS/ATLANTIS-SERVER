package com.atl.map.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.atl.map.dto.request.user.PatchNicknameRequestDto;
import com.atl.map.dto.request.user.PatchProfileImageRequestDto;
import com.atl.map.dto.response.user.GetSignInUserResponseDto;
import com.atl.map.dto.response.user.GetUserResponseDto;
import com.atl.map.dto.response.user.PatchNicknameResponseDto;
import com.atl.map.dto.response.user.PatchProfileImageResponseDto;
import com.atl.map.dto.response.user.PostReportResponseDto;
import com.atl.map.entity.ReportEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
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
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        return GetSignInUserResponseDto.success(userEntity);
    }

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        return GetUserResponseDto.success(userEntity);
    }

    @Override
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        String nickname = dto.getNickname();
        if (nickname != null && userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        userEntity.setNickname(nickname);
        userRepository.save(userEntity);
        return PatchNicknameResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto dto, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        userEntity.setProfileImage(dto.getProfileImage());
        userRepository.save(userEntity);
        return PatchProfileImageResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostReportResponseDto> report(String email, int userid) {
        UserEntity reportUser = userRepository.findByEmail(email);
        if (reportUser == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        UserEntity reportedUser = userRepository.findByUserId(userid);
        if (reportedUser == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        if (!reportRepository.existsByReportUserIdAndReportedUserId(reportUser.getUserId(), userid)) {
            reportedUser.report();
            userRepository.save(reportedUser);
            reportRepository.save(new ReportEntity(reportUser.getUserId(), userid));
        }

        return PostReportResponseDto.success();
    }
}
