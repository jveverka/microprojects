package one.microproject.logger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ContextConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ContextConfig.class);

    @Value("${app.context-path}")
    private String contextPath;
    private String normalizedContextPath;

    @PostConstruct
    private void normalizeContextPath() {
        if (contextPath == null) {
            normalizedContextPath = "";
        } else {
            contextPath = contextPath.trim();
            if (contextPath.isEmpty() || "/".equals(contextPath)) {
                normalizedContextPath = "";
            } else if (!contextPath.startsWith("/")) {
                normalizedContextPath = "/" + contextPath;
            } else {
                normalizedContextPath = contextPath;
            }
        }
        LOG.info("## INIT: contextPath=\"{}\"", contextPath);
        LOG.info("## INIT: normalizedContextPath=\"{}\"", normalizedContextPath);
    }

    public String getPath(String relativePath) {
        return normalizedContextPath + relativePath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

}
