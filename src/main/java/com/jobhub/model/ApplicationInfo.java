package com.jobhub.components;

import java.util.Date;

public class ApplicationInfo {
    public Integer ApplyID;
    public Integer jobId;
    public Date date;
    public String status;

    public String EnglishLevel;
    public String onsite;
    public String graduated;

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

    public ApplicationInfo(Integer applyID, Integer jobId, Date date, String status, String englishLevel, String onsite, String graduated) {
        ApplyID = applyID;
        this.jobId = jobId;
        this.date = date;
        this.status = status;
        EnglishLevel = englishLevel;
        this.onsite = onsite;
        this.graduated = graduated;
    }

    public String getEnglishLevel() {
        return EnglishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        EnglishLevel = englishLevel;
    }

    public String getOnsite() {
        return onsite;
    }

    public void setOnsite(String onsite) {
        this.onsite = onsite;
    }

    public String getGraduated() {
        return graduated;
    }

    public void setGraduated(String graduated) {
        this.graduated = graduated;
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
