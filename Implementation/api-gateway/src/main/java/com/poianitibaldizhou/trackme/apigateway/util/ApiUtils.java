package com.poianitibaldizhou.trackme.apigateway.util;

import java.util.ArrayList;
import java.util.List;

public class ApiUtils {

    public static List<Api> getApiList() {
        List<Api> apis = new ArrayList<>();

        List<String> stringList = new ArrayList<>();
        stringList.add("thirdPartyId");
        Api api = null;

        try {
            api = new Api("https://localhost:8443/individualrequests/individualrequestservice/requests/thirdparty/1", new ArrayList<>(),
                    stringList, Role.THIRD_PARTY);
        } catch(Exception e) {
            // Do nothing
        }

        apis.add(api);

        return apis;
    }

    public static Api getApiByUriWithNoPathVar(String requestApi) {
        for(Api api : ApiUtils.getApiList()) {
            if(api.getUri().equals(requestApi)) {
                return api;
            }
        }

        return null;
    }
}
