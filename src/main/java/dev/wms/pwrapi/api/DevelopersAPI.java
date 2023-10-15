package dev.wms.pwrapi.api;

import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import dev.wms.pwrapi.service.internationalization.SupportedLanguage;
import dev.wms.pwrapi.service.user.ApiUserService;
import dev.wms.pwrapi.utils.config.SentryReporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/developers")
@RequiredArgsConstructor
@SecurityRequirements
public class DevelopersAPI {

    private final SentryReporter sentryReporter;
    private final ApiUserService userService;
    private final LocalizedMessageService msgService;

    @PostMapping("/feedback")
    @Operation(summary = "Send feedback to API Developers! Or request help with exception", description = "Wanna send feedback to us? Just simply" +
            " invoke this POST request! Every successfull response is us getting notified about your message :) Leave " +
            "your contact details there, if you want us to contact you about your feedback. You can also paste you supportCode " +
            "so we can investigate your issue faster")
    public ResponseEntity<String> sendFeedback(@RequestParam(required = false) String email,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String errorId,
                                               String feedback) {
        sentryReporter.captureFeedback(feedback, email, name, errorId);
        return ResponseEntity.ok("Thank you for your feedback!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String email) {
        userService.registerUser(email);

        return ResponseEntity.ok(msgService.getMessage(
                "msg.mail.subject.registration",
                SupportedLanguage.EN)
        );
    }


    @GetMapping("/confirm-email")
    @Operation(summary = "Confirm email of the user using token", description = "This endpoint is used in email when " +
            "user is prompted to confirm the email address. The method is GET as POST is sometimes restricted by email " +
            "clients. If used twice will REMOVE previous api keys from user account")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        userService.confirmEmail(token);

        return ResponseEntity.ok(msgService.getMessageWithArgs(
                "msg.mail.subject.generated-api-key",
                SupportedLanguage.EN)
        );
    }
}
