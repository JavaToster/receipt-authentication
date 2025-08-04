package com.example.authentication.filters;

import com.example.authentication.repositories.RedisCacheRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/auth/forgot_password_replace_password")
@RequiredArgsConstructor
public class RecoveryCodesFilter extends OncePerRequestFilter {

    private final RedisCacheRepository redisCacheRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String recoveryCodeHeader = request.getHeader("recovery-code-id");

        if(isValidHeader(recoveryCodeHeader)){
            try {
                String id = recoveryCodeHeader.substring(3);
                if (!id.isBlank()){
                    redisCacheRepository.removeRecoveryCodeEntry(Long.parseLong(id));
                    filterChain.doFilter(request, response);
                }
            }catch (Exception e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request");
            }
        }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }
    }

    private boolean isValidHeader(String recoveryCodeHeader){
        return (recoveryCodeHeader != null && !recoveryCodeHeader.isBlank() && recoveryCodeHeader.startsWith("id-"));
    }
}

