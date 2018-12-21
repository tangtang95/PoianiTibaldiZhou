package com.poianitibaldizhou.trackme.apigateway.filter.post;

import com.google.common.io.CharStreams;
import com.jayway.jsonpath.JsonPath;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.List;

/**
 * Filter all the HREFSs in a response in order to substitute the URL part of the hypermedia
 * content with gateway information
 */
public class HrefFilter extends ZuulFilter {

    @Value(Constants.SERVER_ADDRESS)
    private String serverAddress;

    @Value(Constants.PORT)
    private String port;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        try (final InputStream responseDataStream = ctx.getResponseDataStream()) {
            final String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, Constants.UTF8_CHAR_SET));
            String newResponseBody = responseData;

            List<String> hrefs = JsonPath.read(newResponseBody, Constants.JSON_HREF_QUERY);

            for(String elem : hrefs) {
                URL url = new URL(elem);
                String path = url.getPath();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Constants.HTTPS_PREFIX);
                stringBuilder.append(serverAddress);
                stringBuilder.append(Constants.PORT_SEPARATOR);
                stringBuilder.append(port);
                stringBuilder.append(path);
                newResponseBody = newResponseBody.replace(elem, stringBuilder);
            }

            ctx.setResponseBody(newResponseBody);
        } catch (IOException e) {
            throw new ZuulException(
                    Constants.ERROR_IN_RESPONSE,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constants.ERROR_IN_RESPONSE);
        }

        return null;
    }
}
