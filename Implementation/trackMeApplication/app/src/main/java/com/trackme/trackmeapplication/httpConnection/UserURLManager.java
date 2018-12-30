package com.trackme.trackmeapplication.httpConnection;

import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.baseUtility.Constant;

import java.util.List;
import java.util.Map;

public class UserURLManager {

    private static UserURLManager instance;

    private String IPAddress;
    private int accountPort;

    private Map<String,Map<String, String>> map = null;

    private UserURLManager(){
        this.IPAddress = Settings.getServerAddress();
        this.accountPort = Settings.getServerPort();
    }

    public static UserURLManager getInstance(){
        if (instance == null)
            instance = new UserURLManager();
        return instance;
    }

    /**
     * Utility method to form the url with the injected port for a certain uri
     * @param uri uri that will access a certain resource of the application
     * @return url for accessing the resource identified by the uri
     */
    public String createURLWithPort(String uri) {
        return "https://"+ IPAddress + ":" + accountPort + uri;
    }

    public void setUrls(List<String> urls) {
        //Log.d("URLS", urls.toString());
        List<Map<String, Map<String, String>>> mapList =  JsonPath.read(urls.toString(), "$");
        map = mapList.get(0);
    }

    public String getLogoutLink() {
        return map.get(Constant.MAP_LOGOUT_KEY).get(Constant.MAP_HREF_KEY);
    }

    public String getUserInfoLink() {
        return map.get(Constant.MAP_USER_INFO_KEY).get(Constant.MAP_HREF_KEY);
    }

    public String getPendingRequestsLink() {
        return map.get(Constant.MAP_PENDING_REQUEST_KEY).get(Constant.MAP_HREF_KEY);
    }

    public String getPostHealthDataLink() {
        return map.get(Constant.MAP_POST_HEALTH_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }

    public String getPostPositioinDataLink() {
        return map.get(Constant.MAP_POST_POSITION_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }

    public String getPostClusterDataLink() {
        return map.get(Constant.MAP_CLUSTER_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }

    public String getGetOwnDataLink() {
        return map.get(Constant.MAP_OWN_DATA_KEY).get(Constant.MAP_HREF_KEY);
    }
}
