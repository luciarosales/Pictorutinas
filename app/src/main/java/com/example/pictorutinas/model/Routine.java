package com.example.pictorutinas.model;

public class Routine {
    private long id;
    private String name;

    public Routine(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public long getId() { return id; }
    public String getName() { return name; }
}