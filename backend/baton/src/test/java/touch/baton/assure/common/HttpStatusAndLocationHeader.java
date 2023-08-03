package touch.baton.assure.common;

import org.springframework.http.HttpStatus;

public class HttpStatusAndLocationHeader {

    private final HttpStatus httpStatus;
    private final String location;

    public HttpStatusAndLocationHeader(final HttpStatus httpStatus, final String location) {
        this.httpStatus = httpStatus;
        this.location = location;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getLocation() {
        return location;
    }
}
