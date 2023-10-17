import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Dictionary;

public class HandleResponse extends WriteJsonObject {
    private Socket server;

    public HandleResponse() {

    }

    public static <T> T getResponse(BufferedReader in, Class<T> obj) {
        WriteJsonObject parser = new WriteJsonObject();

        try {
            parser.deserialize(in.readLine(), obj);
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
