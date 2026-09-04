package com.project.stayEase.advices;

import com.project.stayEase.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
       return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidBookingState(InvalidBookingStateException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GuestCapacityExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleGuestCapacity(GuestCapacityExceededException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoomUnavailable(RoomNotAvailableException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InventoryUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInventoryUpdate(InventoryUpdateException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentRefundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentRefund(PaymentRefundException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidWebhookException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidWebhook(InvalidWebhookException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PaymentProviderException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentProvider(PaymentProviderException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentAlreadyCompletedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyCompletedPayment(PaymentAlreadyCompletedException e){
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentRetryLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentRetryLimitExceeded(PaymentAlreadyCompletedException e){
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.TOO_MANY_REQUESTS);
    }


}
