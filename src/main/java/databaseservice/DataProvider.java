package databaseservice;

import domain.Person;
import domain.SpeciesInfo;

public interface DataProvider {
  Integer getPersonId(String personName);
  void storeCharacterInfo(String personId, Person characterInfo);
  SpeciesInfo getSpeciesInfo(Integer speciesId);
}
