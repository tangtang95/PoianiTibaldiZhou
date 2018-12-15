package com.poianitibaldizhou.trackme.apigateway.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationFilter;
import com.poianitibaldizhou.trackme.apigateway.security.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.security.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.util.Api;
import com.poianitibaldizhou.trackme.apigateway.util.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.AccessControlException;
import java.util.List;

public class AccessControlFilter extends ZuulFilter {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        List<Api> apiList = ApiUtils.getApiList();

        // Find the api that is currently being accessed
        Api calledApi = ApiUtils.getApiByUriWithNoPathVar(request.getRequestURL().toString());

        if(calledApi == null) {
            throw new ZuulException("Api found found", HttpStatus.BAD_REQUEST.value(), "Api not found");
        }

        // Check if the client accessing the api has real access to it
        final String token = tokenAuthenticationFilter.getToken(request);

        switch (calledApi.getRole()) {
            case THIRD_PARTY:
                if(!thirdPartyAuthenticationService.findThirdPartyByToken(token).isPresent()) {
                    throw new AccessControlException("Can't access user methods");
                }
                break;
            case USER:
                if(!userAuthenticationService.findUserByToken(token).isPresent()) {
                    throw new AccessControlException("Can't access third party methods");
                }
                break;
            case ALL:
                // Everything is fine nothing to do
                break;
        }

        return null;
    }
}
