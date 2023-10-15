package dev.wms.pwrapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public abstract class BaseTest {

    protected ObjectMapper mapper;
    protected Random random;

    public BaseTest() {
        random = new Random();
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }
}
