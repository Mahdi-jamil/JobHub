use job_hub_db;
DELIMITER //

CREATE TRIGGER after_apply_update
AFTER UPDATE ON Apply
FOR EACH ROW
BEGIN
    IF NEW.status = 'accepted' AND OLD.status != 'accepted' THEN
        -- Decrease numberOfApplication by 1
        UPDATE Job
        SET numberOfApplication = numberOfApplication - 1
        WHERE JobID = NEW.JobID;

        -- Check if numberOfApplication is zero, then delete the job
        IF (SELECT numberOfApplication FROM Job WHERE JobID = NEW.JobID) = 0 THEN
            UPDATE Job SET status = 'closed' WHERE JobID = NEW.JobID;
        END IF;
    END IF;
END //

DELIMITER ;


