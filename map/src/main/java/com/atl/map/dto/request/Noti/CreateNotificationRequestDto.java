package com.atl.map.dto.request.Noti;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateNotificationRequestDto {
    private String message;
    private LocalDateTime dateTime;
    private Integer radius;
    private Integer markerId;
}
