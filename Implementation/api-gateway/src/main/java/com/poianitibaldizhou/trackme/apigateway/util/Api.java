package com.poianitibaldizhou.trackme.apigateway.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URI;
import java.util.List;

/**
 * It presents an api, with its URI, the params that it needs and also the path
 * variables. The URI inserted here is not completed: path variables are missing
 */
@EqualsAndHashCode
@Getter
public class Api {
    private final String uri;
    private final List<String> params;
    private final List<String> pathVariables;
    private final Role role;

    /**
     * Creates a new API
     *
     * @param uri uri without path variables
     * @param params params of the api
     * @param pathVariables path variables that are needed to access the api, in the correct order
     * @param role type of users that are allowed to access the API
     */
    public Api(String uri, List<String> params, List<String> pathVariables, Role role) {
        this.uri = uri;
        this.params = params;
        this.pathVariables = pathVariables;
        this.role = role;
    }
}
