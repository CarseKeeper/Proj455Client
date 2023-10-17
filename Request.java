
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Request {
    public RequestType requestType;

    @JsonCreator
    public Request(@JsonProperty("RequestType") RequestType type) {
        this.requestType = type;
    }

}
