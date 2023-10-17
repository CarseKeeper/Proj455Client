import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.json.JsonWriteContext;

public class WriteJsonObject {
    ObjectMapper objMap = new ObjectMapper();

    public String serialize(Object Object) {
        try {
            return this.objMap.writeValueAsString(Object);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T deserialize(String json, Class<T> obj) {
        try {
            return this.objMap.readValue(json, obj);
        } catch (Exception e) {
            return null;
        }
    }
}
