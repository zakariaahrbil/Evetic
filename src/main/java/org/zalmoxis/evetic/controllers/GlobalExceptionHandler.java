package org.zalmoxis.evetic.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalmoxis.evetic.dtos.common.ErrorResponse;
import org.zalmoxis.evetic.exceptions.EventNotFoundException;
import org.zalmoxis.evetic.exceptions.EventUpdatingException;
import org.zalmoxis.evetic.exceptions.QrCodeGenerationException;
import org.zalmoxis.evetic.exceptions.QrCodeNotFoundException;
import org.zalmoxis.evetic.exceptions.TicketNotFoundException;
import org.zalmoxis.evetic.exceptions.TicketsSoldOutException;
import org.zalmoxis.evetic.exceptions.TicketTypeNotFoundException;
import org.zalmoxis.evetic.exceptions.UserException;
import org.zalmoxis.evetic.exceptions.UserNotAuthorized;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(TicketNotFoundException ex)
    {
        log.error("Caught TicketNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Ticket not found");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TicketsSoldOutException.class)
    public ResponseEntity<ErrorResponse> handleTicketSoldOutException(TicketsSoldOutException ex)
    {
        log.error("Caught TicketsSoldOutException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Ticket type is sold out");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQrCodeNotFoundException(QrCodeNotFoundException ex)
    {
        log.error("Caught QrCodeNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("QR code not found");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ErrorResponse> handleQrCodeGenerationException(QrCodeGenerationException ex)
    {
        log.error("Caught QrCodeGenerationException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Failed to generate QR code");

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EventUpdatingException.class)
    public ResponseEntity<ErrorResponse> handleEventUpdatingException(EventUpdatingException ex)
    {
        log.error("Caught EventUpdatingException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Cannot update event ID");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAuthorized.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthorizedException(UserNotAuthorized ex)
    {
        log.error("Caught UserNotAuthorized exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("User not authorized to perform this action");

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketTypeNotFoundException(TicketTypeNotFoundException ex)
    {
        log.error("Caught TicketTypeNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Ticket type not found");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException ex)
    {
        log.error("Caught EventNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Event not found");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserNameAlreadyExistsException(UserException ex)
    {
        log.error("Caught UserNameAlreadyExists exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex)
    {
        log.error("Caught BadCredentialsException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("Invalid username or password");

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex)
    {
        log.error("Caught UserNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse =
                new ErrorResponse("User not found");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex)
    {
        log.error("Caught MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Method not valid error occurred.");

        ErrorResponse errorResponse =
                new ErrorResponse(errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(ConstraintViolationException ex)
    {
        log.error("Caught ConstraintViolationException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .orElse("Validation error occurred.");

        ErrorResponse errorResponse =
                new ErrorResponse(errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex)
    {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
