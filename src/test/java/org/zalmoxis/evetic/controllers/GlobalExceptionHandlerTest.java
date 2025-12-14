package org.zalmoxis.evetic.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.zalmoxis.evetic.dtos.common.ErrorResponse;
import org.zalmoxis.evetic.exceptions.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleUserNotFoundException_ShouldReturnBadRequest() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getErrorMessage());
    }

    @Test
    void handleEventNotFoundException_ShouldReturnBadRequest() {
        EventNotFoundException exception = new EventNotFoundException("Event not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEventNotFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Event not found", response.getBody().getErrorMessage());
    }

    @Test
    void handleTicketNotFoundException_ShouldReturnBadRequest() {
        TicketNotFoundException exception = new TicketNotFoundException("Ticket not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleTicketNotFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ticket not found", response.getBody().getErrorMessage());
    }

    @Test
    void handleTicketTypeNotFoundException_ShouldReturnBadRequest() {
        TicketTypeNotFoundException exception = new TicketTypeNotFoundException("Ticket type not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleTicketTypeNotFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ticket type not found", response.getBody().getErrorMessage());
    }

    @Test
    void handleQrCodeNotFoundException_ShouldReturnNotFound() {
        QrCodeNotFoundException exception = new QrCodeNotFoundException("QR code not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleQrCodeNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("QR code not found", response.getBody().getErrorMessage());
    }

    @Test
    void handleUserNameAlreadyExistsException_ShouldReturnConflict() {
        UserException exception = new UserException("Username already exists");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNameAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Username already exists", response.getBody().getErrorMessage());
    }

    @Test
    void handleEventUpdatingException_ShouldReturnBadRequest() {
        EventUpdatingException exception = new EventUpdatingException("Cannot update event ID");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEventUpdatingException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cannot update event ID", response.getBody().getErrorMessage());
    }

    @Test
    void handleTicketSoldOutException_ShouldReturnBadRequest() {
        TicketsSoldOutException exception = new TicketsSoldOutException("Tickets sold out");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleTicketSoldOutException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ticket type is sold out", response.getBody().getErrorMessage());
    }

    @Test
    void handleQrCodeGenerationException_ShouldReturnInternalServerError() {
        QrCodeGenerationException exception = new QrCodeGenerationException("QR code generation failed", new RuntimeException());

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleQrCodeGenerationException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to generate QR code", response.getBody().getErrorMessage());
    }

    @Test
    void handleQrCodeInvalidException_ShouldReturnBadRequest() {
        QrCodeInvalidException exception = new QrCodeInvalidException("QR code is invalid");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleQrCodeInvalidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Qr code is invalid", response.getBody().getErrorMessage());
    }

    @Test
    void handleUserNotAuthorizedException_ShouldReturnForbidden() {
        UserNotAuthorized exception = new UserNotAuthorized("User not authorized");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotAuthorizedException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not authorized to perform this action", response.getBody().getErrorMessage());
    }

    @Test
    void handleAuthorizationDeniedException_ShouldReturnForbidden() {
        AuthorizationDeniedException exception = new AuthorizationDeniedException("Access denied");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAuthorizationDeniedException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access denied", response.getBody().getErrorMessage());
    }

    @Test
    void handleBadCredentialsException_ShouldReturnUnauthorized() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBadCredentialsException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid username or password", response.getBody().getErrorMessage());
    }

    @Test
    void handleMethodArgumentNotValidExceptions_ShouldReturnBadRequest() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "must not be blank");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("must not be blank", response.getBody().getErrorMessage());
    }

    @Test
    void handleMethodArgumentNotValidExceptions_ShouldReturnDefaultMessage_WhenNoFieldErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Method not valid error occurred.", response.getBody().getErrorMessage());
    }

    @Test
    @SuppressWarnings("unchecked")
    void handleValidationExceptions_ShouldReturnBadRequest() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("field");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");
        when(exception.getConstraintViolations()).thenReturn(Set.of(violation));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrorMessage().contains("field"));
        assertTrue(response.getBody().getErrorMessage().contains("must not be null"));
    }

    @Test
    void handleAllExceptions_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred.", response.getBody().getErrorMessage());
    }
}
