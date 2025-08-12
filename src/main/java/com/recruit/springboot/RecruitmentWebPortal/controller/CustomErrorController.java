package com.recruit.springboot.RecruitmentWebPortal.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        
        // If this is a direct call to /error (not an actual error), return a different response
        if (status == null && message == null && exception == null) {
            errorDetails.put("status", "OK");
            errorDetails.put("message", "Error endpoint is accessible");
            errorDetails.put("path", request.getRequestURI());
            errorDetails.put("note", "This endpoint handles application errors. Call it only when an error occurs.");
            return ResponseEntity.ok(errorDetails);
        }

        // Handle actual errors
        errorDetails.put("status", status != null ? status : "Unknown");
        errorDetails.put("message", message != null ? message : "An error occurred");
        errorDetails.put("path", path != null ? path : request.getRequestURI());
        
        if (exception != null) {
            errorDetails.put("exception", exception.getClass().getName());
            // Add the actual exception message
            if (exception instanceof Exception) {
                Exception ex = (Exception) exception;
                errorDetails.put("exceptionMessage", ex.getMessage());
                // Add stack trace for debugging
                errorDetails.put("stackTrace", ex.getStackTrace());
            }
        }

        // Determine the appropriate HTTP status
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (status != null) {
            try {
                int statusCode = Integer.parseInt(status.toString());
                httpStatus = HttpStatus.valueOf(statusCode);
            } catch (NumberFormatException e) {
                // Use default status
            }
        }

        return ResponseEntity.status(httpStatus).body(errorDetails);
    }
}
