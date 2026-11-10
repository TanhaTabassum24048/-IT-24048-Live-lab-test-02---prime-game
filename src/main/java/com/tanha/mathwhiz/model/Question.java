package com.tanha.mathwhiz.model;

import java.io.Serializable;

public class Question implements Serializable {
    private final String type;
    private final String prompt;
    private final int marks;
    private final int first;
    private final int second;
    private final String operation;
    private final double angle;

    public Question(String type, String prompt, int marks, int first, int second,
                    String operation, double angle) {
        this.type = type;
        this.prompt = prompt;
        this.marks = marks;
        this.first = first;
        this.second = second;
        this.operation = operation;
        this.angle = angle;
    }

    public String getType() { return type; }
    public String getPrompt() { return prompt; }
    public int getMarks() { return marks; }
    public int getFirst() { return first; }
    public int getSecond() { return second; }
    public String getOperation() { return operation; }
    public double getAngle() { return angle; }
}
