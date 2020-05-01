package endtoendtests.renderers.renderers;

import com.googlecode.yatspec.rendering.Renderer;
import endtoendtests.renderers.helper.ListOfMapsToRender;

import java.util.List;
import java.util.Map;

// Only works if each map in the list has equal number of keys
public class ListOfMapsRenderer implements Renderer<ListOfMapsToRender> {

    @Override
    public String render(final ListOfMapsToRender listOfMaps) throws Exception {
        StringBuilder html = new StringBuilder("<table>");

        List<Map<String, Object>> result = listOfMaps.getResult();

        if(nothingToPrint(result)) {
            return closeTableAndPrint(html);
        }

        appendMapKeysAsTableHeaders(html, result);
        appendMapValuesAsTableBody(html, result);

        return closeTableAndPrint(html);
    }

    private boolean nothingToPrint(final List<Map<String, Object>> result) {
        return result == null || result.isEmpty();
    }

    private void appendMapKeysAsTableHeaders(final StringBuilder html, final List<Map<String, Object>> result) {
        String rowContents = convertMapKeysToThs(result);
        appendTableRow(html, rowContents);
    }

    private void appendMapValuesAsTableBody(final StringBuilder html, final List<Map<String, Object>> result) {
        for (Map<String, Object> stringObjectMap : result) {
            appendTableRow(html, convertMapValuesToTds(stringObjectMap));
        }
    }

    private void appendTableRow(final StringBuilder html, final String rowContents) {
        html.append("<tr>").append(rowContents).append("</tr>");
    }

    private String convertMapKeysToThs(final List<Map<String, Object>> result) {
        StringBuilder tableHeaders = new StringBuilder();
        for (String columnsName : result.get(0).keySet()) {
            tableHeaders.append("<th>").append(columnsName).append("</th>");
        }
        return tableHeaders.toString();
    }

    private String convertMapValuesToTds(final Map<String, Object> stringObjectMap) {
        StringBuilder tableData = new StringBuilder();
        for (Object stringObjectEntry : stringObjectMap.values()) {
            tableData.append("<td>").append(stringObjectEntry).append("</td>");
        }
        return tableData.toString();
    }

    private String closeTableAndPrint(final StringBuilder html) {
        html.append("</table>");
        return html.toString();
    }
}
