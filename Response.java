
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

    public <T> T getResponseBody(WriteJsonObject json, T obj) {
        return json.deserialize(this.responseBody, obj.getClass());
    }

}
