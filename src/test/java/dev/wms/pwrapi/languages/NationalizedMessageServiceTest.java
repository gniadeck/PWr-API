package dev.wms.pwrapi.languages;

import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import dev.wms.pwrapi.service.internationalization.SupportedLanguage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NationalizedMessageServiceTest {

    @Autowired
    private LocalizedMessageService msgService;

    @Test
    public void getPolishString(){
        assertEquals("Witaj", msgService.getMessage("hello", SupportedLanguage.PL));
    }

    @Test
    public void getEnglishString(){
        assertEquals("Hello", msgService.getMessage("hello", SupportedLanguage.EN));
    }

    @Test
    public void getPolishStringWithArgs(){
        assertEquals("Witaj Świecie!", msgService.getMessageWithArgs("hello_arg", SupportedLanguage.PL, "Świecie"));
    }

    @Test
    public void getEnglishStringWithArgs(){
        assertEquals("Hello World!", msgService.getMessageWithArgs("hello_arg", SupportedLanguage.EN, "World"));
    }

    @Test
    public void getStringRedundantArg_ShouldSkipArgument(){
        assertEquals("Hello", msgService.getMessageWithArgs("hello", SupportedLanguage.EN, "World"));
    }
}
