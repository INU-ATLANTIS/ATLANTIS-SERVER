package com.atl.map.entity;

import java.time.LocalDateTime;

import com.atl.map.dto.request.auth.SignUpRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter 
@NoArgsConstructor  
@AllArgsConstructor 
@Entity(name="user")
@Table(name="user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY 전략 사용
    @Column(name = "userId")
    private int userId;
    @Column(name = "email") // 데이터베이스의 실제 컬럼명 지정
    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String nickname;
    private String role;
    private String profileImage;

    public UserEntity(SignUpRequestDto dto) {

        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.role = "ROLE_USER";
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createDate = now; // 엔티티 생성 시 현재 시간으로 초기화
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
        LocalDateTime now = LocalDateTime.now();
        this.updateDate = now; 
    }

    public void setProfileImage(String profileimage){
        this.profileImage = profileimage;
        LocalDateTime now = LocalDateTime.now();
        this.updateDate = now; 
    }
}
