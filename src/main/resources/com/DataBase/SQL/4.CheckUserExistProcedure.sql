DELIMITER //

CREATE PROCEDURE CheckUsernameExists(
    IN p_username VARCHAR(10),
    IN p_role VARCHAR(20),
    OUT p_user_exists INT
)
BEGIN
	 CASE
        WHEN p_role ='job_seeker' THEN
            SET p_user_exists = (SELECT COUNT(*) FROM job_hub_db.job_seeker WHERE Username = p_username );
        WHEN p_role = 'recruiter' THEN
            SET p_user_exists = (SELECT COUNT(*) FROM job_hub_db.job_seeker WHERE Username = p_username );
    END CASE;

END //

DELIMITER ;
