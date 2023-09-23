package dz.quartz.emailsender.controller;

import dz.quartz.emailsender.dto.EmailRequest;
import dz.quartz.emailsender.dto.EmailResponse;
import dz.quartz.emailsender.service.EmailSchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailSchedulerController {

    private final EmailSchedulerService emailSchedulerService;

    @PostMapping("/schedule/email")
    public ResponseEntity<EmailResponse> scheduleEmail(@Valid @RequestBody EmailRequest email) {
        EmailResponse emailResponse = emailSchedulerService.scheduleEmail(email);
        return emailResponse.isSuccess() ?
                ResponseEntity.ok(emailResponse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emailResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Tested successfully !!! ");
    }

}
