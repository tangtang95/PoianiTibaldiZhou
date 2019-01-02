package com.trackme.trackmeapplication.request.groupRequest;

import com.trackme.trackmeapplication.baseUtility.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GroupRequestBuilder implements Serializable {

    private GroupRequest groupRequest;
    private List<LinkedHashMap<String, String>> filterStatementList;

    public GroupRequestBuilder(){
        filterStatementList = new ArrayList<>();
    }

    void addNewFilter(String[] filters){
        LinkedHashMap<String, String> item = new LinkedHashMap<>();
        for (String filter : filters) {
            String[] param = filter.split(" ");
            item.put(Constant.MAP_COLUMN_KEY, param[0]);
            item.put(Constant.MAP_COMPARISON_SYMBOL_KEY, param[1]);
            item.put(Constant.MAP_VALUE_KEY, param[2]);
            filterStatementList.add(item);
        }
    }

    public void setGroupRequest(GroupRequest groupRequest) {
        this.groupRequest = groupRequest;
    }

    public GroupRequest getGroupRequest() {
        return groupRequest;
    }

    public List<LinkedHashMap<String, String>> getFilterStatementList() {
        return filterStatementList;
    }
}
