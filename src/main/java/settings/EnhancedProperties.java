package settings;

import logging.LoggingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static java.lang.String.format;

public class EnhancedProperties {

    private final static Logger logger = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());

    private final Properties properties;

    public EnhancedProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPropertyOrDefaultValue(String propertyName, String defaultValue) {
        String property = properties.getProperty(propertyName);
        if (property != null) {
            return property;
        }
        logger.warn(format("The property %s was not set, defaulting to %s", propertyName, defaultValue));
        return defaultValue;
    }
}
