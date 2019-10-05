package stacs.chessgateway.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends Exception {

   abstract HttpStatus getHttpCode();
   abstract String getDebugMessage();
   abstract String getUserMessage();

}
