package com.poianitibaldizhou.trackme.apigateway.filter.route;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationFilter;
import com.poianitibaldizhou.trackme.apigateway.security.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.security.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public class TranslationFilter extends ZuulFilter {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private ThirdPartyAuthenticationService thirdPartyAuthenticationService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        final String token = tokenAuthenticationFilter.getToken(ctx.getRequest());

        if (thirdPartyAuthenticationService.findThirdPartyByToken(token).isPresent()) {
            ctx.addZuulRequestHeader("id",
                    thirdPartyAuthenticationService.findThirdPartyByToken(token).orElseThrow(IllegalStateException::new).getId().toString());
        } else if (userAuthenticationService.findUserByToken(token).isPresent()) {
            ctx.addZuulRequestHeader("ssn",
                    userAuthenticationService.findUserByToken(token).orElseThrow(IllegalStateException::new).getSsn());
        } else {
            throw new IllegalStateException();
        }

        return null;
    }
}
