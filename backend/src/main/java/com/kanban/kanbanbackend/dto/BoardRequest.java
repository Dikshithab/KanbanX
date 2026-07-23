package com.kanban.kanbanbackend.dto;

public class BoardRequest {

    private String name;

    public BoardRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BoardRequest{name='" + name + "'}";
    }
}