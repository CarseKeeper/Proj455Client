
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Request {
    public RequestType requestType;

    public Request(RequestType type) {
        this.requestType = type;
    }

}
