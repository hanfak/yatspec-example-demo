package endtoendtests.renderers.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonPrettyPrint {

    public static String prettyPrintJson(String json) {
        try {
            return new JSONObject(json).toString(2);
        } catch (JSONException e) {
            try {
                return new JSONArray(json).toString(2);
            } catch (JSONException e1) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

}