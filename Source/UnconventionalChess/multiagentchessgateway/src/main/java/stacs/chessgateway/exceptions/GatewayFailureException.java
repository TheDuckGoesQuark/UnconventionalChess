package stacs.chessgateway.exceptions;

import org.springframework.http.HttpStatus;

public class GatewayFailureException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private String debugMessage;

    public GatewayFailureException(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    @Override
    HttpStatus getHttpCode() {
        return STATUS;
    }

    @Override
    String getDebugMessage() {
        return debugMessage;
    }

    @Override
    String getUserMessage() {
        return "Something went wrong when communicating with the agents.";
    }
}
