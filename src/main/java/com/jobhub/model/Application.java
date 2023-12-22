package com.jobhub.model;


import java.util.Date;

public class Application {
    public Integer ApplyID;
    public Integer jobId;
    public String title;
    public String description;
    public Date date;

    public Double salary;

    public Application(Integer applyID, Integer jobId, String title, String description, Date date, Double salary, String location, String status) {
        ApplyID = applyID;
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.salary = salary;
        this.Location = location;
        this.status = status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String Location;

    public String status;


    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getApplyID() {
        return ApplyID;
    }

    public void setApplyID(Integer applyID) {
        ApplyID = applyID;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
