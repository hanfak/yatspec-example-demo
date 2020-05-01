package endtoendtests.renderers.renderers;

import com.googlecode.yatspec.rendering.Renderer;
import endtoendtests.renderers.helper.ListOfSomeObjectDTO;
import endtoendtests.renderers.helper.SomeObjectDTO;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class ListOfSomeObjectDTORenderer implements Renderer<ListOfSomeObjectDTO> {
    @Override
    public String render(ListOfSomeObjectDTO listOfSomeObjectDTO) throws Exception {
        List<SomeObjectDTO> dtos = listOfSomeObjectDTO.getAll();
        StringBuilder result = new StringBuilder();

        for (SomeObjectDTO dto : dtos) {
            result.append(ReflectionToStringBuilder.toString(dto, new MultilineRecursiveToStringStyle())).append("\n");
        }

        return result.toString();
    }
}
