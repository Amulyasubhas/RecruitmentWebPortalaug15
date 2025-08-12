-- Remove candidate_id column from interview_tracker table
-- This is needed because we're using a shared primary key relationship
-- where interview_tracker.id serves as both primary key and foreign key to candidate_details_and_status_tracker.id

-- First, drop any foreign key constraints on candidate_id if they exist
SET @constraint_name = (
    SELECT CONSTRAINT_NAME 
    FROM information_schema.KEY_COLUMN_USAGE 
    WHERE TABLE_SCHEMA = 'recruitment_web_portal' 
    AND TABLE_NAME = 'interview_tracker' 
    AND COLUMN_NAME = 'candidate_id' 
    AND REFERENCED_TABLE_NAME IS NOT NULL
    LIMIT 1
);

SET @drop_fk_sql = IF(@constraint_name IS NOT NULL, 
    CONCAT('ALTER TABLE interview_tracker DROP FOREIGN KEY ', @constraint_name), 
    'SELECT "No foreign key constraint found" as message'
);

PREPARE stmt FROM @drop_fk_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Now drop the candidate_id column
ALTER TABLE interview_tracker DROP COLUMN candidate_id;
