package com.atl.map.service.implement;

import java.time.Duration;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.atl.map.common.CertificationNumber;
import com.atl.map.dto.request.auth.*;
import com.atl.map.dto.response.auth.*;
import com.atl.map.entity.UserEntity;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
import com.atl.map.provider.EmailProvider;
import com.atl.map.provider.JwtProvider;
import com.atl.map.repository.FavoriteRepository;
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

    private static final Duration CERTIFICATION_TTL = Duration.ofMinutes(5);
    private static final String CERTIFICATION_KEY_PREFIX = "certification:";

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final JwtProvider jwtProvider;
    private final FavoriteRepository favoriteRepository;
    private final NotificationRepository notificationRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        return EmailCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertificaion(EmailCertificationRequestDto dto) {
        String email = dto.getEmail();
        String certificationNumber = CertificationNumber.getCertificationNumber();

        if (!emailProvider.sendCertificationMail(email, certificationNumber)) {
            throw new BusinessException(ErrorCode.MAIL_FAIL);
        }

        stringRedisTemplate.opsForValue()
                .set(getCertificationKey(email), certificationNumber, CERTIFICATION_TTL);

        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        String email = dto.getEmail();
        log.debug("인증번호 확인 요청 - email: {}", email);

        String savedCertificationNumber = stringRedisTemplate.opsForValue().get(getCertificationKey(email));
        if (savedCertificationNumber == null) {
            log.warn("인증 내역 없음 - email: {}", email);
            throw new BusinessException(ErrorCode.CERTIFICATION_FAIL);
        }

        boolean isMatched = savedCertificationNumber.equals(dto.getCertificationNumber());
        if (!isMatched) {
            log.info("인증번호 불일치 - email: {}", email);
            throw new BusinessException(ErrorCode.CERTIFICATION_FAIL);
        }

        log.info("인증번호 확인 성공 - email: {}", email);
        return CheckCertificationResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        String email = dto.getEmail();

        if (userRepository.existsByEmail(email)) throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);

        String savedCertificationNumber = stringRedisTemplate.opsForValue().get(getCertificationKey(email));
        if (savedCertificationNumber == null) throw new BusinessException(ErrorCode.CERTIFICATION_FAIL);
        if (!savedCertificationNumber.equals(dto.getCertificationNumber())) {
            throw new BusinessException(ErrorCode.CERTIFICATION_FAIL);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(new UserEntity(dto));
        stringRedisTemplate.delete(getCertificationKey(email));

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        UserEntity userEntity = userRepository.findByEmail(dto.getEmail());
        if (userEntity == null) throw new BusinessException(ErrorCode.SIGN_IN_FAIL);
        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
            throw new BusinessException(ErrorCode.SIGN_IN_FAIL);
        }

        return SignInResponseDto.success(jwtProvider.create(dto.getEmail()));
    }

    @Transactional
    @Override
    public ResponseEntity<? super DeleteAccountResponseDto> deleteAccount(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        notificationRepository.deleteByUserId(userEntity.getUserId());
        favoriteRepository.deleteByUserId(userEntity.getUserId());
        userEntity.deletedUser();
        userRepository.save(userEntity);

        return DeleteAccountResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super ChangePasswordResponseDto> changePassword(ChangePasswordRequestDto dto) {
        String email = dto.getEmail();

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        String savedCertificationNumber = stringRedisTemplate.opsForValue().get(getCertificationKey(email));
        if (savedCertificationNumber == null) throw new BusinessException(ErrorCode.CERTIFICATION_FAIL);
        if (!savedCertificationNumber.equals(dto.getCertificationNumber())) {
            throw new BusinessException(ErrorCode.CERTIFICATION_FAIL);
        }

        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(userEntity);
        stringRedisTemplate.delete(getCertificationKey(email));

        return ChangePasswordResponseDto.success();
    }

    private String getCertificationKey(String email) {
        return CERTIFICATION_KEY_PREFIX + email;
    }
}
