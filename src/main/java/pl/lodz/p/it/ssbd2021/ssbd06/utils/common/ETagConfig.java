package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

@ApplicationScoped
public class ETagConfig implements Serializable {
    private static final String CONFIG_FILE = "config.properties";
    private static final String ETAG_SECRET_KEY = "etag.secretKey";

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

    public String getEtagSecretKey() {
        return properties.getProperty(ETagConfig.ETAG_SECRET_KEY);
    }
}
