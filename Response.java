
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
    public RequestType responseType;
    public String responseBody;

    @JsonCreator
    public Response(@JsonProperty("RequestType") RequestType type, @JsonProperty("ResponseBody") String responseBody) {
        this.responseType = type;
        this.responseBody = responseBody;
    }

    public static Event parseEvent(WriteJsonObject json, String response) {
        Response res = json.deserialize(response, Response.class);

        return json.deserialize(res.responseBody, Event.class);
    }

    public static ArrayList<Event> parseEvents(WriteJsonObject json, String response) {
        Response res = json.deserialize(response, Response.class);

        return (ArrayList<Event>) json.deserialize(res.responseBody, ArrayList.class);
    }
}
