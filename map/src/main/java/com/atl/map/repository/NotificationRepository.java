package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.atl.map.entity.NotificationEntity;
import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    
    NotificationEntity findByNotiId(int notiId);

    List<NotificationEntity> findAllByUserId(int userId);
}
