package com.atl.map.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atl.map.common.CertificationNumber;
import com.atl.map.dto.request.auth.*;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.auth.*;
import com.atl.map.entity.CertificationEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.provider.EmailProvider;
import com.atl.map.provider.JwtProvider;
import com.atl.map.repository.CertificationRepository;
import com.atl.map.repository.CommentRepository;
import com.atl.map.repository.FavoriteRepository;
import com.atl.map.repository.MarkerRepository;
import com.atl.map.repository.NotificationRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;
    private final JwtProvider jwtProvider;
    private final FavoriteRepository favoriteRepository;
    private final NotificationRepository notificationRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto) {

        try {
            String userEmail = dto.getEmail();
            boolean isExistEmail = userRepository.existsByEmail(userEmail);
            if (isExistEmail) return EmailCheckResponseDto.duplicateEmail();

        } catch (Exception exception) {
            log.error("이메일 중복 확인 실패 - email: {}", dto.getEmail(), exception);
            return ResponseDto.databaseError();
        }

        return EmailCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertificaion(EmailCertificationRequestDto dto) {

        try {
            String email = dto.getEmail();
            String certificationNumber = CertificationNumber.getCertificationNumber();
            boolean isSuccessd = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessd) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = certificationRepository.findByEmail(email);

            if (certificationEntity == null) {
                certificationEntity = new CertificationEntity(email, certificationNumber);
            } else {
                certificationEntity.setCertificationNumber(certificationNumber);
            }

            certificationRepository.save(certificationEntity);

        } catch (Exception exception) {
            log.error("이메일 인증번호 발송 실패 - email: {}", dto.getEmail(), exception);
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        String email = dto.getEmail();
        String certificationNumber = dto.getCertificationNumber();

        log.debug("인증번호 확인 요청 - email: {}", email);
        try {
            CertificationEntity certificationEntity = certificationRepository.findByEmail(email);

            if (certificationEntity == null) {
                log.warn("인증 내역 없음 - email: {}", email);
                return CheckCertificationResponseDto.certificationFail();
            }

            boolean isMatched = certificationEntity.getEmail().equals(email)
                    && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) {
                log.info("인증번호 불일치 - email: {}", email);
                return CheckCertificationResponseDto.certificationFail();
            }
        } catch (Exception exception) {
            log.error("인증번호 확인 중 오류 - email: {}", email, exception);
            return ResponseDto.databaseError();
        }

        log.info("인증번호 확인 성공 - email: {}", email);
        return CheckCertificationResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {

        try {
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();
            boolean isExistEmail = userRepository.existsByEmail(email);
            if (isExistEmail) return SignUpResponseDto.duplicateEmail();

            CertificationEntity certificationEntity = certificationRepository.findByEmail(email);
            if (certificationEntity == null) return SignUpResponseDto.certificationFail();
            boolean isMatched = certificationEntity.getCertificationNumber().equals(certificationNumber);

            if (!isMatched) return SignUpResponseDto.certificationFail();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            certificationRepository.delete(certificationEntity);

        } catch (Exception exception) {
            log.error("회원가입 실패 - email: {}", dto.getEmail(), exception);
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        String token = null;

        try {
            String userEmail = dto.getEmail();
            UserEntity userEntity = userRepository.findByEmail(userEmail);
            if (userEntity == null) return SignInResponseDto.signInFail();

            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return SignInResponseDto.signInFail();

            token = jwtProvider.create(userEmail);

        } catch (Exception exception) {
            log.error("로그인 실패 - email: {}", dto.getEmail(), exception);
            return ResponseDto.databaseError();
        }

        return SignInResponseDto.success(token);
    }

    @Transactional
    @Override
    public ResponseEntity<? super DeleteAccountResponseDto> deleteAccount(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        try {
            if (userEntity == null) {
                return DeleteAccountResponseDto.notExistUser();
            }
            notificationRepository.deleteByUserId(userEntity.getUserId());
            favoriteRepository.deleteByUserId(userEntity.getUserId());
            userEntity.deletedUser();
            userRepository.save(userEntity);

        } catch (Exception exception) {
            log.error("회원 탈퇴 실패 - email: {}", email, exception);
            return ResponseDto.databaseError();
        }
        return DeleteAccountResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super ChangePasswordResponseDto> changePassword(ChangePasswordRequestDto dto) {
        try {
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return ChangePasswordResponseDto.noExistUser();

            CertificationEntity certificationEntity = certificationRepository.findByEmail(email);
            if (certificationEntity == null) return ChangePasswordResponseDto.certificationFail();

            boolean isMatched = certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return ChangePasswordResponseDto.certificationFail();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            userEntity.setPassword(encodedPassword);
            userRepository.save(userEntity);

            certificationRepository.delete(certificationEntity);

        } catch (Exception exception) {
            log.error("비밀번호 변경 실패 - email: {}", dto.getEmail(), exception);
            return ResponseDto.databaseError();
        }

        return ChangePasswordResponseDto.success();
    }
}
