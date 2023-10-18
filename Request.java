
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Request {
    public RequestType requestType;
    public String requestBody;

    @JsonCreator
    public Request(@JsonProperty("RequestType") RequestType type, @JsonProperty("RequestBody") String requestBody) {
        this.requestType = type;
        this.requestBody = requestBody;
    }

}
