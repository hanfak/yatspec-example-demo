package webserver;

import domain.Person;
import domain.Species;

import java.io.IOException;

public interface StarWarsInterfaceService {
  Person getCharacterInfo(String id) throws IOException;

  Species getSpeciesInfo(String url) throws IOException;
}
