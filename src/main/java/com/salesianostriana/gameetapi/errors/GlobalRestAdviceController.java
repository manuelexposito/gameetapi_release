package com.salesianostriana.gameetapi.errors;

import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.FriendshipAlreadyExists;
import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.RequestAlreadyExists;
import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.UserAlreadyExistsException;
import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.UserAlreadyInvitedException;
import com.salesianostriana.gameetapi.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.gameetapi.errors.exceptions.forbidden.PrivateSessionException;
import com.salesianostriana.gameetapi.errors.exceptions.storage.WrongFormatException;
import com.salesianostriana.gameetapi.errors.model.ApiError;
import com.salesianostriana.gameetapi.errors.model.ApiSubError;
import com.salesianostriana.gameetapi.errors.model.ApiValidationSubError;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestAdviceController extends ResponseEntityExceptionHandler {

    // @Value("${useralreadyexists.exception}")
    // public String userAlreadyExistsExceptionMsg;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.buildApiError(ex, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {


        List<ApiSubError> subErrorList = new ArrayList<>();

        ex.getAllErrors().forEach(error -> {

            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;

                subErrorList.add(
                        ApiValidationSubError.builder()
                                .objeto(fieldError.getObjectName())
                                .campo(fieldError.getField())
                                .valorRechazado(fieldError.getRejectedValue())
                                .mensaje(fieldError.getDefaultMessage())
                                .build()
                );
            } else {
                ObjectError objectError = (ObjectError) error;

                subErrorList.add(
                        ApiValidationSubError.builder()
                                .objeto(objectError.getObjectName())
                                .mensaje(objectError.getDefaultMessage())
                                .build()
                );
            }


        });


        return buildApiError(ex, HttpStatus.BAD_REQUEST, request,
                subErrorList.isEmpty() ? null : subErrorList

        );
    }



    @ExceptionHandler({PrivateSessionException.class})
    public ResponseEntity<?> handlePrivateSessionException(PrivateSessionException ex, WebRequest request) {

        return buildApiError(ex, "Esta sesión es privada y no pertenece a ella.", HttpStatus.FORBIDDEN, request);
    }


    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {

        return buildApiError(ex, "userAlreadyExistsExceptionMsg", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({RequestAlreadyExists.class})
    public ResponseEntity<?> handleRequestAlreadyExists(RequestAlreadyExists ex, WebRequest request) {

        return buildApiError(ex, "Ya se ha enviado esta petición.", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<?> handleConversionFailedException(ConversionFailedException ex, WebRequest request) {
        return buildApiError(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        return buildApiError(ex, HttpStatus.BAD_REQUEST, request, ex.getConstraintViolations()
                .stream()
                .map(cv -> ApiValidationSubError.builder()
                        .objeto(cv.getRootBeanClass().getSimpleName())
                        .campo(((PathImpl) cv.getPropertyPath()).getLeafNode().asString())
                        .valorRechazado(cv.getInvalidValue())
                        .mensaje(cv.getMessage())
                        .build()).collect(Collectors.toList()));
    }

    @ExceptionHandler({WrongFormatException.class})
    public ResponseEntity<?> handleWrongFormatException(WrongFormatException e, WebRequest request) {

        return buildApiError(e, HttpStatus.BAD_REQUEST, request);

    }

    @ExceptionHandler({SingleEntityNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(SingleEntityNotFoundException e, WebRequest request) {

        return this.buildApiError(e, HttpStatus.NOT_FOUND, request);


    }

  /*  @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<?> handleRequestAlreadySentException(EntityExistsException e, WebRequest request) {

        return buildApiError(e, "La petición aún está por confirmarse", HttpStatus.resolve(409), request);

    }*/



    @ExceptionHandler({FriendshipAlreadyExists.class})
    public ResponseEntity<?> handleFriendshipAlreadyExists(FriendshipAlreadyExists e, WebRequest request){

        return buildApiError(e, "No puedes enviar una petición de amistad a alguien que ya es tu amigo.", HttpStatus.resolve(409), request);

    }


    @ExceptionHandler({UserAlreadyInvitedException.class})
    public ResponseEntity<?> handleUserAlreadyInvitedException(UserAlreadyInvitedException e, WebRequest request){

        return buildApiError(e, "Este usuario ya fue invitado a la partida", HttpStatus.resolve(409), request);

    }

    //MENSAJES
    private String getMessageForLocale(String messageKey, Locale locale) {

        return ResourceBundle.getBundle("errors", locale).getString(messageKey);

    }

    ///BUILD API ERROR
    //Con sub errors
    private ResponseEntity<Object> buildApiError(Exception exception, HttpStatus status, WebRequest request, List<ApiSubError> subErrorList) {


        ApiError error = ApiError.builder()
                .estado(status)
                .codigo(status.value())
                .ruta(((ServletWebRequest) request).getRequest().getRequestURI())
                //TODO: Internacionalizar este mensaje
                .mensaje("Hubo errores en la validación")
                .apiSubErrors(subErrorList)
                .build();

        return ResponseEntity.status(status).body(error);
    }

    //Sin sub errors
    private ResponseEntity<Object> buildApiError(Exception exception, HttpStatus status, WebRequest request) {

        ApiError error = ApiError.builder()
                .estado(status)
                .codigo(status.value())
                .ruta(((ServletWebRequest) request).getRequest().getRequestURI())
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<Object> buildApiError(Exception exception, String customMessage, HttpStatus status, WebRequest request) {

        ApiError error = ApiError.builder()
                .estado(status)
                .codigo(status.value())
                .ruta(((ServletWebRequest) request).getRequest().getRequestURI())
                .mensaje(customMessage)
                .build();

        return ResponseEntity.status(status).body(error);
    }


}