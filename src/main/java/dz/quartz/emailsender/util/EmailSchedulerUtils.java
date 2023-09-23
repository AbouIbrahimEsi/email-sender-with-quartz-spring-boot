package dz.quartz.emailsender.util;

import org.springframework.stereotype.Component;

@Component
public class EmailSchedulerUtils {

    // Job and Trigger details
    public static final String JOB_GROUP = "EMAIL_JOBS";
    public static String JOB_DESCRIPTION = "Send Email Job";
    public static String TRIGGER_GROUP = "EMAIL_TRIGGERS";
    public static String TRIGGER_DESCRIPTION = "Send Email TRIGGER";

    // Response messages
    public static String DATE_TIME_MISMATCH = "dateTime must be after current time.";
    public static String EMAIL_SCHEDULING_SUCCESS = "Email Scheduled Successfully !!!";
    public static String EMAIL_SCHEDULING_ERROR = "Error while scheduling email. Please try again later !!!";

}
