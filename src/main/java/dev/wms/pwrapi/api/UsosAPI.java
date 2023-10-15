package dev.wms.pwrapi.api;

import dev.wms.pwrapi.service.usos.UsosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/usos")
@RequiredArgsConstructor
public class UsosAPI {

    private final UsosService usosService;

    @GetMapping
    public void loginToUsos(@RequestParam String login, @RequestParam String password) {
        usosService.loginToUsos(login, password);
    }

}
