package com.trackme.trackmeapplication.httpConnection;

public class Settings {

    private static int serverPort = 8443;
    private static String serverAddress = "192.168.1.12";
    private static int refreshItemTime = 10000;

    public static int getServerPort() {
        return serverPort;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getRefreshItemTime() {
        return refreshItemTime;
    }

    public void setServerPort(int serverPort) {
        Settings.serverPort = serverPort;
    }

    public void setServerAddress(String serverAddress) {
        Settings.serverAddress = serverAddress;
    }

    public void setRefreshItemTime(int refreshItemTime) {
        Settings.refreshItemTime = refreshItemTime;
    }
}
