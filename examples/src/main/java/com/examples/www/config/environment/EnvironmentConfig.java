package com.examples.www.config.environment;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource(value="classpath:/pg.properties")
@RequiredArgsConstructor
@Configuration
public class EnvironmentConfig {
    private final Environment environment;

    public String getProperty(String key) {
        return environment.getProperty(key);
    }
}
