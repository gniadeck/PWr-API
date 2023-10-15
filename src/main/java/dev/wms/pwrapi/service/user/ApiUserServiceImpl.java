package dev.wms.pwrapi.service.user;

import dev.wms.pwrapi.dao.user.ApiUserRepository;
import dev.wms.pwrapi.entity.token.ConfirmationToken;
import dev.wms.pwrapi.entity.user.ApiUser;
import dev.wms.pwrapi.security.encryption.SymmetricEncryptImpl;
import dev.wms.pwrapi.service.email.EmailService;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import dev.wms.pwrapi.service.internationalization.SupportedLanguage;
import dev.wms.pwrapi.service.token.TokenService;
import dev.wms.pwrapi.utils.email.EmailUtils;
import dev.wms.pwrapi.utils.generalExceptions.ExpiredConfirmationTokenException;
import dev.wms.pwrapi.utils.generalExceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ApiUserServiceImpl implements ApiUserService {

    private final ApiUserRepository userRepository;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final EmailUtils emailUtils;
    private final SymmetricEncryptImpl passwordEncryption;
    private final LocalizedMessageService msgService;
    private final ApiUserFactory apiUserFactory;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.getApiUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nie zaleziono użytkownika o podanym adresie email: " + email)
                );
    }

    @Override
    public Optional<ApiUser> getUserByApiKey(String apiKey) {
        String encodedKey = passwordEncryption.encode(apiKey);
        return userRepository.getApiUserByApiKey(encodedKey);
    }

    @Override
    @Transactional
    public ApiUser registerUser(String email) {
        Optional<ApiUser> userOpt = userRepository.getApiUserByEmail(email);

        return userOpt
                .map(this::addAndSendTokenToUser)
                .orElseGet(() -> createUserFromEmailAndSendToken(email));
    }

    @Override
    @Transactional
    public ApiUser confirmEmail(String token) {
        ConfirmationToken confirmationToken = tokenService.getConfirmationToken(token);

        if(confirmationToken.isExpired())
            throw new ExpiredConfirmationTokenException();

        ApiUser user = userRepository.findById(confirmationToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                            "Użytkownik o tokenie: " + confirmationToken.getToken() + " nie istnieje")
                );


        String apiKey = tokenService.generateUUID().toString();
        String apiKeyEncrypted = passwordEncryption.encode(apiKey);
        user.setApiKey(apiKeyEncrypted);

        String emailGeneratedMsg = msgService.getMessageWithArgs(
                "msg.mail.subject.generated-api-key",
                SupportedLanguage.EN
        );

        emailService.sendMessage(
                user.getEmail(),
                emailGeneratedMsg,
                emailUtils.createReceiveApiKeyEmailTemplate(apiKey),
                true
        );

        tokenService.deleteAllConfirmationTokensOfUser(user);
        return userRepository.save(user);
    }



    private ApiUser addAndSendTokenToUser(ApiUser user){
        ConfirmationToken confirmationToken = tokenService.addConfirmationTokenToUser(user);
        sendConfirmationEmail(user.getEmail(), confirmationToken.getToken());
        return user;
    }

    private ApiUser createUserFromEmailAndSendToken(String email){
        ApiUser user = userRepository.save(apiUserFactory.createNewUser(email));
        return addAndSendTokenToUser(user);
    }

    private void sendConfirmationEmail(String email, String token){
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        String url = baseUrl + "/api/developers/confirm-email?token=" + token;

        emailService.sendMessage(
                email,
                msgService.getMessage("msg.mail.subject.confirmation", SupportedLanguage.EN),
                emailUtils.createEmailConfirmationEmailTemplate(url),
                true
        );
    }
}
