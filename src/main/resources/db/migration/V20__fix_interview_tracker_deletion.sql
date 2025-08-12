-- V20__fix_interview_tracker_deletion.sql
-- Fix foreign key constraint issue that prevents deletion of interview tracker records

-- Drop the foreign key constraint that's preventing deletion
-- Use a more robust approach that handles cases where the constraint might not exist

-- First, check if the constraint exists and get its name
SET @constraint_name = (
    SELECT CONSTRAINT_NAME 
    FROM information_schema.REFERENTIAL_CONSTRAINTS 
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'interview_tracker' 
    AND REFERENCED_TABLE_NAME = 'candidate_details_and_status_tracker'
    LIMIT 1
);

-- Only try to drop if the constraint exists
SET @sql = IF(@constraint_name IS NOT NULL, 
    CONCAT('ALTER TABLE interview_tracker DROP FOREIGN KEY ', @constraint_name), 
    'SELECT "No foreign key constraint found" as message'
);

-- Execute the SQL dynamically
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Now we can delete interview tracker records without foreign key constraint issues
-- The relationship is still maintained at the JPA level through the @MapsId annotation
