package com.example.springailearning.chatclient2.dto;

public class ReviewRequest {

    private String technology;

    public ReviewRequest(String technology) {
        this.technology = technology;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
