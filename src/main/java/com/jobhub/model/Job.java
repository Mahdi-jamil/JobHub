package com.jobhub.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Job {
    public Integer JobID;
    public String title;
    public String Description;
    public String Requirements;
    public Double Salary;
    public String Location;
    public Integer rid;
    public String status;
}
