package com.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Admin
 */
public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
