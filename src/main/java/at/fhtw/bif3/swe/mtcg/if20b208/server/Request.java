package at.fhtw.bif3.swe.mtcg.if20b208.server;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private HttpRequest httpRequest;
    private String pathname;
    private String params;
    private String contentType;
    private Integer contentLength;
    private String token;
    private String body = "";
}
