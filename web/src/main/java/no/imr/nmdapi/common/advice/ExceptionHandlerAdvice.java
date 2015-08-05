package no.imr.nmdapi.common.advice;

import java.math.BigInteger;
import no.imr.nmdapi.exceptions.AlreadyExistsException;
import no.imr.nmdapi.exceptions.ConversionException;
import no.imr.nmdapi.exceptions.MissingDataException;
import no.imr.nmdapi.exceptions.NotFoundException;
import no.imr.nmdapi.generic.response.v1.ErrorElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author kjetilf
 *
 * This class is always run after the controllers and is used to define
 * the output if an error occurs.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * Returned response code should be 404 if data was not found.
     * @param ex    Caused exception.
     * @return      Error message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorElementType handleException(final NotFoundException ex) {
        LOGGER.info("Advice logging(Not found): ", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.NOT_FOUND.value()));
        element.setMessage("No data was found.");
        return element;
    }

    /**
     * Returned response code should be 412  if data pre condition is not meet
     * @param ex    Caused exception.
     * @return      Error message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ErrorElementType handleException(final MissingDataException ex) {
        LOGGER.info("Advice logging(Precondition failed): ", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.CONFLICT.value()));
        element.setMessage("Precondition failed");
        return element;
    }

    /**
     * Returned response code should be 409 if data already exists
     * @param ex    Caused exception.
     * @return      Error message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorElementType handleException(final AlreadyExistsException ex) {
        LOGGER.info("Advice logging(AlreadyExists): ", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.CONFLICT.value()));
        element.setMessage("Data already exists.");
        return element;
    }



    /**
     * If application fails or unknown error then return 500.
     *
     * @param ex    Caused exception.
     * @return      Internal server error.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorElementType handleException(final RuntimeException ex) {
        LOGGER.error("Error occured during request.", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        element.setMessage("Application error. Send mail to datahjelp@imr.no.");
        return element;
    }

    /**
     * If marshall unmarshall fails then return bad request.
     *
     * @param ex    Exception.
     * @return      Error message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorElementType handleException(final ConversionException ex) {
        LOGGER.info("Bad request.", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.BAD_REQUEST.value()));
        element.setMessage("Bad request: " + ex.getMessage());
        return element;
    }

    /**
     * If marshall unmarshall fails then return bad request.
     *
     * @param ex    Exception.
     * @return      Error message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorElementType handleException(final InvalidTokenException ex) {
        LOGGER.info("Invalid bearer token.", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.UNAUTHORIZED.value()));
        element.setMessage("User token is either missing or wrong. " + ex.getOAuth2ErrorCode());
        return element;
    }

    /**
     * If marshall unmarshall fails then return bad request.
     *
     * @param ex    Caused exception.
     * @return      Error message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorElementType handleException(final AccessDeniedException ex) {
        LOGGER.info("Access denied.", ex);
        ErrorElementType element = new ErrorElementType();
        element.setErrorcode(BigInteger.valueOf(HttpStatus.FORBIDDEN.value()));
        element.setMessage("User does not have access to the resource. Contact datahjelp@imr.no for more information.");
        return element;
    }

}
