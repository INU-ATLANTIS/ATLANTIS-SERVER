package com.atl.map.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atl.map.common.CertificationNumber;
import com.atl.map.dto.request.auth.CheckCertificationRequestDto;
import com.atl.map.dto.request.auth.EmailCertificationRequestDto;
import com.atl.map.dto.request.auth.EmailCheckRequestDto;
import com.atl.map.dto.request.auth.SignInRequestDto;
import com.atl.map.dto.request.auth.SignUpRequestDto;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.auth.CheckCertificationResponseDto;
import com.atl.map.dto.response.auth.EmailCertificationResponseDto;
import com.atl.map.dto.response.auth.EmailCheckResponseDto;
import com.atl.map.dto.response.auth.SignInResponseDto;
import com.atl.map.dto.response.auth.SignUpResponseDto;
import com.atl.map.entity.CertificationEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.provider.EmailProvider;
import com.atl.map.provider.JwtProvider;
import com.atl.map.repository.CertificationRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    //외부에서 제어역전 통해서 의존성 주입
    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;
    private final JwtProvider jwtProvider;

    //의존성 주입 아님
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto) {

        try {

            String userEmail = dto.getEmail();
            boolean isExistEmail = userRepository.existsByEmail(userEmail);
            if (isExistEmail) return EmailCheckResponseDto.duplicateEmail();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertificaion(EmailCertificationRequestDto dto) {
        
        try{
            
            String email = dto.getEmail();
            
            boolean isExistEmail = userRepository.existsByEmail(email);
            if (isExistEmail) return EmailCertificationResponseDto.duplicateEmail();

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccessd = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccessd) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(email, certificationNumber);
            certificationRepository.save(certificationEntity);

        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try{
            
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();
            
            CertificationEntity certificationEntity = certificationRepository.findByEmail(email);
            
            //인증 메일을 보낸적 없을 때
            if(certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatched = certificationEntity.getEmail().equals(email)
            && certificationEntity.getCertificationNumber().equals(certificationNumber);

            if(!isMatched) return CheckCertificationResponseDto.certificationFail();

        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        
        try{

            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();
            boolean isExistEmail = userRepository.existsByEmail(email);
            if(isExistEmail) return SignUpResponseDto.duplicateEmail();
            
            CertificationEntity certificationEntity = certificationRepository.findByEmail(email);
            // 인증 번호를 찾을 수 없는 경우의 예외 처리
            if(certificationEntity == null) return SignUpResponseDto.certificationFail();
            boolean isMatched = certificationEntity.getCertificationNumber().equals(certificationNumber);
            
            if(!isMatched) return SignUpResponseDto.certificationFail();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            certificationRepository.delete(certificationEntity);
            //certificationRepository.deleteByEmail(email);

        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        
        String token = null;

        try{

            String userEmail = dto.getEmail();
            UserEntity userEntity = userRepository.findByEmail(userEmail);
            if(userEntity == null) return SignInResponseDto.signInFail();

            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if(!isMatched) return SignInResponseDto.signInFail();

            token = jwtProvider.create(userEmail);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignInResponseDto.success(token);
    }

}
