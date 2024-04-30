package com.atl.map.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import com.atl.map.dto.request.Noti.CreateNotificationRequestDto;
import com.atl.map.dto.request.Noti.PatchNotificationRequestDto;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notiId;
    private int userId;
    private Integer markerId;
    private int type;
    private String message;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime dateTime;
    private Integer radius;

    public NotificationEntity(CreateNotificationRequestDto dto, int userId){
        this.message = dto.getMessage();
        this.createDate = LocalDateTime.now();
        this.dateTime = dto.getDateTime();
        this.radius = dto.getRadius();
        this.markerId = dto.getMarkerId();
        this.userId = userId;
        if(dto.getMarkerId() == null){ // 지오펜싱 알람이 아님.
            this.type = 2;
        }
        else{
            this.type = 1;
        }
    }

    public void patchNotification(PatchNotificationRequestDto dto){
        this.updateDate = LocalDateTime.now();
        this.message = dto.getMessage();
        this.dateTime = dto.getDateTime();
        this.radius = dto.getRadius();
        this.markerId = dto.getMarkerId();
        if(dto.getMarkerId() == null){ // 지오펜싱 알람이 아님.
            this.type = 2;
        }
        else{
            this.type = 1;
        }
    }

}
