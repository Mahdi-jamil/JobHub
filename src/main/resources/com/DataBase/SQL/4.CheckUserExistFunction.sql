
DELIMITER //
CREATE FUNCTION CheckUsernameExists(
    p_username VARCHAR(10),
    p_role VARCHAR(20)
)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE user_exists INT;

    IF p_role = 'job_seeker' THEN
        SELECT COUNT(*) INTO user_exists FROM job_hub_db.job_seeker WHERE Username = p_username;
    ELSEIF p_role = 'recruiter' THEN
        SELECT COUNT(*) INTO user_exists FROM job_hub_db.recruiter WHERE Username = p_username;
    END IF;

    RETURN user_exists;
END //

DELIMITER ;
