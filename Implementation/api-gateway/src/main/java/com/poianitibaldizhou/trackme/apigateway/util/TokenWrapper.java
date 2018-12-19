package com.poianitibaldizhou.trackme.apigateway.util;

import lombok.Data;

import java.io.Serializable;

/**
 * Wrapper for token elements
 */
@Data
public class TokenWrapper implements Serializable {
    private String token;
}
