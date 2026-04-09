package com.atl.map.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

import com.atl.map.dto.request.Noti.CreateNotificationRequestDto;
import com.atl.map.dto.request.Noti.PatchNotificationRequestDto;
import com.atl.map.dto.response.Noti.CreateNotificationResponseDto;
import com.atl.map.dto.response.Noti.DeleteNotificationResponseDto;
import com.atl.map.dto.response.Noti.GetNotificationResponseDto;
import com.atl.map.dto.response.Noti.GetUserNotificationResponseDto;
import com.atl.map.dto.response.Noti.PatchNotificationResponseDto;
import com.atl.map.entity.NotificationEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
import com.atl.map.repository.MarkerRepository;
import com.atl.map.repository.NotificationRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.NotiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotiServiceImplement implements NotiService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;

    @Override
    public ResponseEntity<? super CreateNotificationResponseDto> createNotification(String email, CreateNotificationRequestDto dto) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        if (dto.getMarkerId() != null && !markerRepository.existsById(dto.getMarkerId())) {
            throw new BusinessException(ErrorCode.MARKER_NOT_FOUND);
        }

        NotificationEntity notificationEntity = new NotificationEntity(dto, userEntity.getUserId());
        notificationRepository.save(notificationEntity);
        return CreateNotificationResponseDto.success(notificationEntity.getNotiId());
    }

    @Override
    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(String email, int notiId) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        NotificationEntity notificationEntity = notificationRepository.findByNotiId(notiId);
        if (notificationEntity == null) throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        if (notificationEntity.getUserId() != userEntity.getUserId()) throw new BusinessException(ErrorCode.NO_PERMISSION);

        notificationRepository.delete(notificationEntity);
        return DeleteNotificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchNotificationResponseDto> patchNotification(PatchNotificationRequestDto dto, int notiId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        NotificationEntity notificationEntity = notificationRepository.findByNotiId(notiId);
        if (notificationEntity == null) throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        if (notificationEntity.getUserId() != userEntity.getUserId()) throw new BusinessException(ErrorCode.NO_PERMISSION);

        notificationEntity.patchNotification(dto);
        notificationRepository.save(notificationEntity);
        return PatchNotificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetNotificationResponseDto> getNotification(int notiId) {
        NotificationEntity notificationEntity = notificationRepository.findByNotiId(notiId);
        if (notificationEntity == null) throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        return GetNotificationResponseDto.success(notificationEntity);
    }

    @Override
    public ResponseEntity<? super GetUserNotificationResponseDto> getUserNotification(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        List<NotificationEntity> list = notificationRepository.findAllByUserId(userEntity.getUserId());
        return GetUserNotificationResponseDto.success(list);
    }
}
