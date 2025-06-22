package com.stringcompare.stringcompare.dto;

import java.util.List;
import java.util.Map;

public class CompareResponse {

    private List<Map<String, String>> status;
    private boolean complete;

    public CompareResponse(List<Map<String, String>> status, boolean complete) {
        this.status = status;
        this.complete = complete;
    }

    public List<Map<String, String>> getStatus() {
        return status;
    }

    public void setStatus(List<Map<String, String>> status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}