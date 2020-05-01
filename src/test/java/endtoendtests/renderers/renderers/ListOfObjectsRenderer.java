package endtoendtests.renderers.renderers;

import com.google.common.util.concurrent.AtomicDouble;
import com.googlecode.yatspec.rendering.Renderer;
import endtoendtests.renderers.helper.ListOfMapsToRender;
import endtoendtests.renderers.helper.ListOfSomeObjectDTO;
import endtoendtests.renderers.helper.SomeObjectDTO;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ListOfObjectsRenderer implements Renderer<ListOfSomeObjectDTO> {

    private DecimalFormat decimalFormat = new DecimalFormat();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String render(ListOfSomeObjectDTO listOfSomeObjectDTO) throws Exception {
        List<Map<String, Object>> objectsToPrint = new ArrayList<>();
        AtomicDouble aDouble = new AtomicDouble(1.5);
        AtomicLong aLong = new AtomicLong(0L);
        for (SomeObjectDTO someObjectDTO : listOfSomeObjectDTO.getAll()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("string", someObjectDTO.getString());
            map.put("date", LocalDateTime.now().plusDays(aLong.getAndAdd(2L)).format(dateTimeFormatter));
            map.put("isBool", someObjectDTO.isBool());
            map.put("number",someObjectDTO.getNum());
            map.put("decimal", getFormat(aDouble.getAndAdd(aDouble.doubleValue())));
            objectsToPrint.add(map);
        }

        return new ListOfMapsRenderer().render(new ListOfMapsToRender(objectsToPrint));
    }

    private String getFormat(Double number) {
        if (number == null) {
            return null;
        }
        return decimalFormat.format(number);
    }


}
