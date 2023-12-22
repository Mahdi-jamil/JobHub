use job_hub_db;

ALTER TABLE `Recruiter` ADD CONSTRAINT `recruiter_cid_foreign` FOREIGN KEY(`Cid`) REFERENCES `Company`(`Cid`);
ALTER TABLE `Job` ADD CONSTRAINT `job_rid_foreign` FOREIGN KEY(`RID`) REFERENCES `Recruiter`(`Rid`);
ALTER TABLE `Apply` ADD CONSTRAINT `apply_jobid_foreign` FOREIGN KEY (`JobID`) REFERENCES `Job` (`JobID`);
ALTER TABLE `Apply` ADD CONSTRAINT `apply_sid_foreign` FOREIGN KEY (`Sid`) REFERENCES `Job_Seeker` (`Sid`);
ALTER TABLE `Apply` ADD CONSTRAINT `apply_rid_foreign` FOREIGN KEY (`Rid`) REFERENCES `recruiter` (`Rid`);
