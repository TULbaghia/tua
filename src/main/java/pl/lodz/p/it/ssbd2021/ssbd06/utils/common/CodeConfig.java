package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

@ApplicationScoped
public class CodeConfig implements Serializable {
    private static final String CONFIG_FILE = "config.properties";
    private static final String RESET_EXPIRATION_MINUTES = "reset.expiration.minutes";

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

    private int getInt(String key)  {
        return Integer.parseInt(get(key));
    }

    public int getResetExpirationMinutes() {
        return getInt(RESET_EXPIRATION_MINUTES);
    }
}
