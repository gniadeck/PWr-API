package dev.wms.pwrapi.service.usos;

import dev.wms.pwrapi.dao.auth.AuthDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsosServiceImpl implements UsosService {

    private final AuthDao usosAuthDao;

    @Override
    public void loginToUsos(String login, String password) {
        usosAuthDao.login(login, password);
    }

}
