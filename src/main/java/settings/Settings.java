package settings;

import java.util.Properties;
// TODO can add settings for db config, fileservice config etc
public class Settings {

  private final EnhancedProperties properties;

  public Settings(Properties properties) {
    this.properties = new EnhancedProperties(properties);
  }

  public String starWarsApiAddress() {
    return properties.getPropertyOrDefaultValue("star.wars.character.info.api", "http://swapi.py4e.com/api/");
  }
}
