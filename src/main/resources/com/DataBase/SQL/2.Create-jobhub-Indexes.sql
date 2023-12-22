use job_hub_db;

CREATE INDEX Apply_jobID_FK ON Apply (JobID);

CREATE INDEX JOB_Rid_FK ON job (RID);

CREATE INDEX Recruiter_Cid_FK ON recruiter(Cid);


