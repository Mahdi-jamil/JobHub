package com.jobhub.components;

public class Job {
    public Integer JobID;
    public String title;
    public String Description;
    public String Requirements;
    public Double Salary;
    public String Location;
    public Integer rid;
    public String status;

    public Job(int ID, String title, String description, String requirements, Double salary, String location, Integer rid, String status) {
        this.JobID = ID;
        this.title = title;
        this.Description = description;
        this.Requirements = requirements;
        this.Salary = salary;
        this.Location = location;
        this.rid = rid;
        this.status = status;
    }

    public Integer getJobID() {
        return JobID;
    }

    public void setID(Integer ID) {
        this.JobID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRequirements() {
        return Requirements;
    }

    public void setRequirements(String requirements) {
        Requirements = requirements;
    }

    public Double getSalary() {
        return Salary;
    }

    public void setSalary(Double salary) {
        Salary = salary;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
