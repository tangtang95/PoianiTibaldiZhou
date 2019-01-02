package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

public interface ThirdPartyInterface extends Serializable {

    String extractName();

    String extractEmail();

    String extractPassword();

}
