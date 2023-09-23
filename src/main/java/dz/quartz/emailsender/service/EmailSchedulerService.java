package dz.quartz.emailsender.service;

import dz.quartz.emailsender.dto.EmailRequest;
import dz.quartz.emailsender.dto.EmailResponse;
import dz.quartz.emailsender.quartz.job.JobEmail;
import dz.quartz.emailsender.util.EmailSchedulerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSchedulerService {

    private final Scheduler scheduler;

    public EmailResponse scheduleEmail(EmailRequest emailRequest) {
        try{
            ZonedDateTime dateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                return new EmailResponse(false, EmailSchedulerUtils.DATE_TIME_MISMATCH);
            }
            JobDetail jobDetail = this.buildJobDetail(emailRequest);
            Trigger trigger = this.buildTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);
            return new EmailResponse(true, jobDetail.getKey().getName(),
                    jobDetail.getKey().getGroup(),
                    EmailSchedulerUtils.EMAIL_SCHEDULING_SUCCESS);
        } catch (SchedulerException se) {
            log.error("Error while scheduling email: ", se);
            return new EmailResponse(false, EmailSchedulerUtils.EMAIL_SCHEDULING_ERROR);
        }
    }

    private JobDetail buildJobDetail(EmailRequest scheduledEmailRequest){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email", scheduledEmailRequest.getEmail());
        jobDataMap.put("subject", scheduledEmailRequest.getSubject());
        jobDataMap.put("body", scheduledEmailRequest.getBody());
        return JobBuilder.newJob(JobEmail.class)
                .withIdentity(UUID.randomUUID().toString(), EmailSchedulerUtils.JOB_GROUP)
                .withDescription(EmailSchedulerUtils.JOB_DESCRIPTION)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), EmailSchedulerUtils.TRIGGER_GROUP)
                .withDescription(EmailSchedulerUtils.TRIGGER_DESCRIPTION)
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withMisfireHandlingInstructionFireNow())
                .build();
    }
}
