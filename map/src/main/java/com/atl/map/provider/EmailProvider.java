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

        String certificationMessage = "<div style='padding:20px 10%; background-color: #d3d3d3;'>"
        + "<div style='font-family: Arial, sans-serif; margin:auto; padding: 20px; width:500px; height: 600px; background-color: white;'>"
        + "<h1 style='color: #004a9e;'>commINUty</h1>"
        + "<hr>"
        + "<p style='font-size: 16px; color: #333; margin-top: 4rem;'>안녕하세요, commINUty입니다.</p>"
        + "<p style='font-size: 16px; color: #333; margin-bottom: 4rem;'>아래와 같이 인증번호를 발급해드립니다.</p>"
        + "<p>인증번호 :</p>"
        + "<p style='font-size: 24px; color: #004a9e;'>"
        + certificationNumber
        + "</p>"
        + "<p style='font-size: 16px; color: #333; margin: 4rem 0;'>commINUty로 이동하셔서 해당 인증번호를 정확히 입력해주세요.</p>"
        + "<hr>"
        + "<p style='font-size: 14px; color: #666;'>이 메일은 시스템에 의해 자동으로 발송되었습니다. 만약 인증 코드를 요청하지 않으셨다면, 본 메일을 무시해 주세요.</p>"
        + "</div>"
        + "</div>";
        
        return certificationMessage;
    }
}
