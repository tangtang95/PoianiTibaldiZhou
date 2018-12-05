package com.poianitibaldizhou.trackme.sharedataservice.util.generator;

import java.io.IOException;
import java.util.List;

public interface Generator {

    List<Object> generateObjects(int numberOfObjects) throws IOException;

}
