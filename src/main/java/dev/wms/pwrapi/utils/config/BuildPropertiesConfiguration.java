package dev.wms.pwrapi.utils.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class BuildPropertiesConfiguration {

    /**
     * Fail safe in case of compilation without BuildProperties generation
     */
    @Bean
    @ConditionalOnMissingBean(BuildProperties.class)
    BuildProperties buildProperties() {
        return new BuildProperties(new Properties());
    }

}
