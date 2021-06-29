package com.example.ordinaryscoreapp.Model;

public class Student {
    private String no;
    private String name;
    private String belongClass;

    public Student() {
    }

    public Student(String no, String name, String belongClass) {
        this.no = no;
        this.name = name;
        this.belongClass = belongClass;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBelongClass() {
        return belongClass;
    }

    public void setBelongClass(String belongClass) {
        this.belongClass = belongClass;
    }
}
