package com.sam.scheduler_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "professors")
public class Professor {

    @Id
    private String id;
    private String name;
    private String department;

    public Professor() {
    }

    public Professor(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return name + " (" + department + ")";
    }
}
