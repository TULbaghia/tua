package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JWTConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static final String JWT_SECRET_KEY = "jwt.secretKey";
    private static final String JWT_EXPIRE_TIMEOUT = "jwt.expire";
    private static final String JWT_ISS = "jwt.iss";

    private final Properties properties = new Properties();

    @PostConstruct
    private void init() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (inputStream == null) {
                throw new FileNotFoundException("Couldn't find file: " + CONFIG_FILE);
            }

            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String get(String key) {
        return properties.getProperty(key);
    }

    private long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public String getJWTSecretKey() {
        return get(JWT_SECRET_KEY);
    }

    public Long getJWTExpireTimeout() {
        return getLong(JWT_EXPIRE_TIMEOUT);
    }

    public String getJWTIss() { return get(JWT_ISS); }
}
