USE job_hub_db;

CREATE USER 'job-sekeer'@'localhost' IDENTIFIED BY 'root';
CREATE USER 'recruiter'@'localhost' IDENTIFIED BY 'mysql804218$89742';

GRANT INSERT ON job_hub_db.job TO 'recruiter'@'localhost';
GRANT UPDATE ON job_hub_db.job TO 'recruiter'@'localhost';
GRANT DELETE ON job_hub_db.job TO 'recruiter'@'localhost';
GRANT SELECT ON job_hub_db.job TO 'recruiter'@'localhost';

GRANT SELECT ON job_hub_db.apply TO 'recruiter'@'localhost';
GRANT DELETE ON job_hub_db.apply TO 'recruiter'@'localhost';
GRANT INSERT ON job_hub_db.apply TO 'recruiter'@'localhost';
GRANT UPDATE ON job_hub_db.apply TO 'recruiter'@'localhost';


GRANT SELECT ON job_hub_db.job_seeker TO 'recruiter'@'localhost';

GRANT SELECT ON job_hub_db.recruiter TO 'recruiter'@'localhost';
GRANT UPDATE ON job_hub_db.recruiter TO 'recruiter'@'localhost';
GRANT EXECUTE ON PROCEDURE job_hub_db.ChangePassword_Sp TO 'recruiter'@'localhost';




GRANT SELECT ON job_hub_db.job TO 'job-sekeer'@'localhost';

GRANT SELECT ON job_hub_db.job_seeker TO 'job-sekeer'@'localhost';
GRANT UPDATE ON job_hub_db.job_seeker TO 'job-sekeer'@'localhost';

GRANT SELECT ON job_hub_db.apply TO 'job-sekeer'@'localhost';
GRANT INSERT ON job_hub_db.apply TO 'job-sekeer'@'localhost';
GRANT UPDATE ON job_hub_db.apply TO 'job-sekeer'@'localhost';
GRANT DELETE ON job_hub_db.apply TO 'job-sekeer'@'localhost';

GRANT EXECUTE ON FUNCTION job_hub_db.CheckUsernameExists TO 'job-sekeer'@'localhost';
GRANT EXECUTE ON PROCEDURE job_hub_db.ChangePassword_Sp TO 'job-sekeer'@'localhost';




##REVOKE SELECT ON job_hub_db.job FROM 'job-sekeer'@'localhost';



