package fileservice;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import domain.CharacterDetails;
import domain.Person;
import domain.Species;
import logging.LoggingCategory;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class FileService {

  private static final Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());
  private static final String DIRECTORY_OF_LOCATION_OF_CREATED_FILES = "target/test-classes/testFiles/created";
  private static final String FILE_NAME = "/data-%d.xml";

  private final CounterService counterService;
  private final XmlMapper xmlMapper;

  public FileService(CounterService counterService, XmlMapper xmlMapper) {
    this.counterService = counterService;
    this.xmlMapper = xmlMapper;
  }

  public void storeData(String personId, Person characterInfo, Species speciesInfo) {
    xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    try {
      Files.createDirectories(Paths.get(DIRECTORY_OF_LOCATION_OF_CREATED_FILES));
      CharacterDetails data = new CharacterDetails(
              personId,
              characterInfo.getName(),
              characterInfo.getBirthYear(),
              speciesInfo.getAverageHeight(),
              speciesInfo.getLifeSpan(),
              speciesInfo.getName());
      String s = ReflectionToStringBuilder.toString(data, new MultilineRecursiveToStringStyle());
      APPLICATION_LOGGER.info("file contents written to file: " + s);
      xmlMapper.writeValue(new FileOutputStream(
              format(DIRECTORY_OF_LOCATION_OF_CREATED_FILES + FILE_NAME,
              counterService.execute())), data);
    } catch (IOException e) {
      APPLICATION_LOGGER.error("error processing file", e);
    }
  }

  public CharacterDetails readData(String filename) throws IOException {
    Path fileLocation = Paths.get("target/classes/files/" + filename);
    xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    CharacterDetails characterDetails = xmlMapper.readValue(Files.newInputStream(fileLocation), CharacterDetails.class);
    String s = ReflectionToStringBuilder.toString(characterDetails, new MultilineRecursiveToStringStyle());
    System.out.println("s = " + s);
    return characterDetails;
  }

  public static void main(String... args) throws IOException {
    FileService fileService = new FileService(new CounterService(), new XmlMapper());
    fileService.readData("example.xml");
  }
}
