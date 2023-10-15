package dev.wms.pwrapi.dao.library;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Repository;

@Repository
public class LibraryAuthDao {

    public OkHttpClient getAnonymousClient(){
        return new OkHttpClient();
    }

}
