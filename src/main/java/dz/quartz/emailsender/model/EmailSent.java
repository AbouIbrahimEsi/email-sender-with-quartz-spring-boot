package dz.quartz.emailsender.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailSent {

    @Email
    @NotEmpty
    private String emailSender;

    @Email
    @NotEmpty
    private String emailRecipient;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;
}
