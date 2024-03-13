package com.atl.map.provider;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailProvider {

    private final JavaMailSender javaMailSender;

    private final String SUBJECT = "메일 인증 번호 입니다.";

    public boolean sendCertificationMail(String email, String CertificationNumber) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getCertificationMessage(CertificationNumber);

            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        return true;

    }

    private String getCertificationMessage(String certificationNumber) {

        String certificationMessage = "";
        certificationMessage += "<div style='font-family: Arial, sans-serif; text-align: center; margin: 40px; padding: 20px; border: 1px solid #004a9e; border-radius: 10px; background: #E6EFFF;'>";
        certificationMessage += "<h1 style='color: #004a9e;'>commINUty 인증메일</h1>";
        certificationMessage += "<p style='font-size: 16px; color: #333;'>귀하의 인증 코드는 아래와 같습니다. 아래 코드를 앱 내 인증 코드 입력란에 정확히 입력해 주세요.</p>";
        certificationMessage += "<div style='margin: 20px auto; width: 60%; padding: 15px; background-color: #004a9e; border-radius: 5px;'>";
        certificationMessage += "<h3 style='color: #ffffff;'>인증코드:</h3>";
        certificationMessage += "<p style='font-size: 24px; letter-spacing: 3px; color: #ffffff;'><strong>"
                + certificationNumber + "</strong></p>";
        certificationMessage += "</div>";
        certificationMessage += "<p style='font-size: 14px; color: #666;'>이 메일은 시스템에 의해 자동으로 발송되었습니다. 만약 인증 코드를 요청하지 않으셨다면, 본 메일을 무시해 주세요.</p>";
        certificationMessage += "</div>";
        return certificationMessage;
    }
}
