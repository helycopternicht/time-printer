package ee.neotech.timeprinter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyHolder {
    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String user;

    @Value("${database.password}")
    private String pwd;

    @Value("${database.reconnection-interval-sec}")
    private Integer reconnectionInterval;

    @Value("${database.connection-validate-timeout-sec}")
    private Integer validateTimeout;

    public Integer getReconnectionInterval() {
        return reconnectionInterval;
    }

    public Integer getValidateTimeout() {
        return validateTimeout;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }
}
