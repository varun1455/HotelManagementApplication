package com.project.stayEase.aspect;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcRequestContextFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = getRequestId(request);

        MDC.put("requestId", requestId);

        response.setHeader(REQUEST_ID_HEADER, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
    private String getRequestId(HttpServletRequest request) {
        String incomingRequestId = request.getHeader(REQUEST_ID_HEADER);

        if (incomingRequestId != null&& !incomingRequestId.isBlank()) {
            return incomingRequestId;
        }

        return UUID.randomUUID().toString();
    }
}
