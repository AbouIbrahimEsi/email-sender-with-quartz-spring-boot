package dz.quartz.emailsender.quartz.job;


import dz.quartz.emailsender.model.EmailSent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEmail extends QuartzJobBean {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        EmailSent email = EmailSent.builder()
                .emailSender(mailProperties.getUsername())
                .emailRecipient(jobDataMap.getString("email"))
                .subject(jobDataMap.getString("subject"))
                .body(jobDataMap.getString("body"))
                .build();
        this.sendEmail(email);
    }

    private void sendEmail(EmailSent email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getBody());
            messageHelper.setText(email.getBody());
            messageHelper.setFrom(email.getEmailSender());
            messageHelper.setTo(email.getEmailRecipient());
            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Error while sending email: ", ex);
        }
    }
}
