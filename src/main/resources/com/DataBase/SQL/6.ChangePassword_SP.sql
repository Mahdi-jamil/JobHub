DELIMITER //

CREATE PROCEDURE ChangePassword_Sp(
    IN p_username VARCHAR(10),
    IN P_password VARCHAR(10),
    IN p_role VARCHAR(20)
)
BEGIN
	 CASE
        WHEN p_role ='job_seeker' THEN
             UPDATE job_hub_db.job_seeker SET password = P_password WHERE Username = p_username ;
        WHEN p_role = 'recruiter' THEN
             UPDATE job_hub_db.recruiter SET password = P_password WHERE Username = p_username ;
    END CASE;

END //

DELIMITER ;
