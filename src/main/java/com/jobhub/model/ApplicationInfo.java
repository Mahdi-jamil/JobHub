package com.jobhub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class ApplicationInfo {
    public Integer ApplyID;
    public Integer jobId;
    public Date date;
    public String status;
    public String EnglishLevel;
    public String onsite;
    public String graduated;
}
