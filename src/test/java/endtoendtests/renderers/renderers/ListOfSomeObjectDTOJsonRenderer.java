package endtoendtests.renderers.renderers;

import com.googlecode.yatspec.rendering.Renderer;
import endtoendtests.renderers.helper.ListOfSomeObjectDTO;
import endtoendtests.renderers.helper.SomeObjectDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static endtoendtests.renderers.helper.JsonPrettyPrint.prettyPrintJson;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

public class ListOfSomeObjectDTOJsonRenderer implements Renderer<ListOfSomeObjectDTO> {
  @Override
  public String render(ListOfSomeObjectDTO listOfSomeObjectDTO) throws Exception {
    List<SomeObjectDTO> dtos = listOfSomeObjectDTO.getAll();
    StringBuilder result = new StringBuilder();

    result.append(join(splitByCharacterTypeCamelCase(listOfSomeObjectDTO.getClass().getSimpleName()), " "))
            .append("\n\n");
    for (SomeObjectDTO dto : dtos) {
      result.append(prettyPrintJson(ReflectionToStringBuilder.toString(dto, ToStringStyle.JSON_STYLE))).append("\n");
    }

    return result.toString();
  }
}
