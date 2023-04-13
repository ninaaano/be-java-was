package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void forward(String url) {
        try {
            ContentType type = ContentType.of(url);
            byte[] body = Files.readAllBytes(new File(type.getPath() + url).toPath());

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            setHeader("Content-Type", type.getType());
            setHeader("Content-Length" , String.valueOf(body.length));
            getKey();
            dos.writeBytes("\r\n");
            responseBody(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public Map<String, String> setHeader(String key, String value) {
        headers.put(key, value);
        return headers;
    }

    public void redirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            getKey();
            setHeader("Location: ",url);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void getKey() {
        try {
            for (Map.Entry<String, String> key : headers.entrySet()) {
                dos.writeBytes(String.format("%s: %s\r\n", key.getKey(), key.getValue()));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
