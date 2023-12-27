package com.jobhub.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Application {
    public Integer ApplyID;
    public Integer jobId;
    public String title;
    public String description;
    public Date date;
    public Double salary;
    public String Location;
    public String status;
}
