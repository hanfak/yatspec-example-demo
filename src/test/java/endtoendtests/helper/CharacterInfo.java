package endtoendtests.helper;

public class CharacterInfo {
  private final Integer personId;
  private final String personName;
  public final String birthYear;

  public CharacterInfo(Integer personId, String personName, String birthYear) {
    this.personId = personId;
    this.personName = personName;
    this.birthYear = birthYear;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CharacterInfo that = (CharacterInfo) o;

    if (!personId.equals(that.personId)) return false;
    if (!personName.equals(that.personName)) return false;
    return birthYear.equals(that.birthYear);
  }

  @Override
  public int hashCode() {
    int result = personId.hashCode();
    result = 31 * result + personName.hashCode();
    result = 31 * result + birthYear.hashCode();
    return result;
  }
}
