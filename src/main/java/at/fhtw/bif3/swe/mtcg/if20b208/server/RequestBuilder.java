package at.fhtw.bif3.swe.mtcg.if20b208.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

public class RequestBuilder {
    static final String CONTENT_TYPE = "Content-Type: ";
    static final String CONTENT_LENGTH = "Content-Length: ";

    public static Request buildRequest(BufferedReader in) throws IOException {
        String line = in.readLine();
        Request request = new Request();
        if (line != null) {
            String[] splitFirstLine = line.split(" ");
            Boolean hasParams = splitFirstLine[1].indexOf("?") != -1;
            request.setHttpRequest(getMethod(splitFirstLine));
            request.setPathname(getPathname(splitFirstLine, hasParams));
            request.setParams(getParams(splitFirstLine, hasParams));
            while (!line.isEmpty()) {
                System.out.println(line);
                line = in.readLine();
                if (line.startsWith(CONTENT_LENGTH)) {
                    request.setContentLength(getContentLength(line));
                }
                if (line.startsWith("Content-Type:")) {
                    request.setContentType(getContentType(line));
                }
                if(line.contains("Authorization: Basic")){
                    String[] splitted = line.split("\\s+");
                    request.setToken(splitted[2]);
                }
            }
            if (request.getContentLength() != null && request.getContentLength()>0) {
                int asciChar;
                for (int i = 0; i < request.getContentLength(); i++) {
                    asciChar = in.read();
                    String body = request.getBody();
                    request.setBody(body + ((char) asciChar));
                }
            }
        }

        return request;
    }

    private static HttpRequest getMethod(String[] splitFirstLine) {
        return HttpRequest.valueOf(splitFirstLine[0].toUpperCase(Locale.ROOT));
    }

    private static String getPathname(String[] splitFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splitFirstLine[1].split("\\?")[0];
        }

        return splitFirstLine[1];
    }


    private static String getParams(String[] splittedFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splittedFirstLine[1].split("\\?")[1];
        }

        return "";
    }

    private static Integer getContentLength(String line) {
        return Integer.parseInt(line.substring(CONTENT_LENGTH.length()));
    }

    private static String getContentType(String line) {
        return line.substring(CONTENT_TYPE.length());
    }
}
