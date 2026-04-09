package com.atl.map.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.atl.map.entity.NotificationEntity;
import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    
    NotificationEntity findByNotiId(int notiId);
    void deleteByMarkerId(Integer markerId);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from NotificationEntity n where n.markerId in :markerIds")
    void deleteAllByMarkerIds(List<Integer> markerIds);
    List<NotificationEntity> findAllByUserId(int userId);
    void deleteByUserId(int userId);
}
