package learning.fileservice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

  public static void main(String... args) throws IOException {
    // Grab files from directory
    Path fileLocation = Paths.get("src/test/resources/testFiles/example.json");
    // read file
    String fileContents = new String(Files.readAllBytes(fileLocation));
    //    System.out.println("fileContents = " + fileContents);

    //put in object
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    ExampleObject exampleObject = objectMapper.readValue(Files.newInputStream(fileLocation), ExampleObject.class);
    Integer randomInt = exampleObject.getRandomInteger();
    double randomFloat = exampleObject.getRandomFloat();
    System.out.println("randomInt = " + randomInt);
    System.out.println("randomFloat = " + randomFloat);

    // Get object,
    ObjectMapper xmlMapper = new XmlMapper();
    xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    ExampleObject object = new ExampleObject(1, 13.14, false, Arrays.asList(new Dictionary(0, "creole"), new Dictionary(1, "English")));

    // create location for new file
    Files.createDirectories(Paths.get("target/test-classes/learning/testFiles/created"));

    // put in file in xml and json format
    xmlMapper.writeValue(new FileOutputStream("target/test-classes/learning/testFiles/created/example.xml"), object);
    objectMapper.writeValue(new FileOutputStream("target/test-classes/learning/testFiles/created/example.json"), object);
  }
}
