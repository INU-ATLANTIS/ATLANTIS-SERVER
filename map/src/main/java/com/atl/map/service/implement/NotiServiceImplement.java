package com.atl.map.service.implement;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
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
import com.atl.map.repository.NotificationRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.repository.MarkerRepository;
import com.atl.map.service.NotiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotiServiceImplement implements NotiService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;

    @Override
    public ResponseEntity<? super CreateNotificationResponseDto> createNotification(String email, CreateNotificationRequestDto dto) {
        try {
            // Check if user exists
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) {
                return CreateNotificationResponseDto.notExistUser();
            }
            // Check if marker exists if markerId is provided
            if (dto.getMarkerId() != null && !markerRepository.existsById(dto.getMarkerId())) {
                return CreateNotificationResponseDto.notExistMarker();
            }
            // Assuming user entity and other necessary data are retrieved and valid
            NotificationEntity notificationEntity = new NotificationEntity(dto, userEntity.getUserId());
            notificationRepository.save(notificationEntity);

            return CreateNotificationResponseDto.success(notificationEntity.getNotiId());

        } catch (Exception exception) {
            exception.printStackTrace();
            return CreateNotificationResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(String email, int notiId) {
        try {

            NotificationEntity notificationEntity = notificationRepository.findByNotiId(notiId);
            UserEntity userEntity = userRepository.findByEmail(email);

            if(notificationEntity == null){
                return DeleteNotificationResponseDto.notExistNotification();
            }
            if(userEntity == null){
                return DeleteNotificationResponseDto.notExistUser();
            }
            if(notificationEntity.getUserId() != userEntity.getUserId()){
                return DeleteNotificationResponseDto.noPermisson();
            }

            notificationRepository.delete(notificationEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return DeleteNotificationResponseDto.databaseError();
        }

        return DeleteNotificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchNotificationResponseDto> patchNotification(PatchNotificationRequestDto dto, int notiId, String email) {
        
        try {

            NotificationEntity notificationEntity = notificationRepository.findByNotiId(notiId);
            UserEntity userEntity = userRepository.findByEmail(email);

            if(notificationEntity == null){
                return DeleteNotificationResponseDto.notExistNotification();
            }
            if(userEntity == null){
                return DeleteNotificationResponseDto.notExistUser();
            }
            if(notificationEntity.getUserId() != userEntity.getUserId()){
                return DeleteNotificationResponseDto.noPermisson();
            }
            
            notificationEntity.patchNotification(dto);
            notificationRepository.save(notificationEntity);

            return PatchNotificationResponseDto.success();
        } catch (Exception exception) {
            exception.printStackTrace();
            return PatchNotificationResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetNotificationResponseDto> getNotification(int notiId) {
        try {
            NotificationEntity notificationEntity = notificationRepository.findByNotiId(notiId);
            if (notificationEntity == null) {
                return GetNotificationResponseDto.notExistNotification();
            }
            return GetNotificationResponseDto.success(notificationEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return GetNotificationResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetUserNotificationResponseDto> getUserNotification(String email) {
        
        List<NotificationEntity> list;
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null){
                return DeleteNotificationResponseDto.notExistUser();
            }
            list = notificationRepository.findAllByUserId(userEntity.getUserId());
            return GetUserNotificationResponseDto.success(list);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return GetUserNotificationResponseDto.databaseError();
        }
    }
}
