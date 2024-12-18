package com.spotify.oauth2.pojo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
    @JsonProperty("error")
    private InnerError error;

    public InnerError getError() {
        return error;
    }

    public void setError(InnerError error) {
        this.error = error;
    }


}
