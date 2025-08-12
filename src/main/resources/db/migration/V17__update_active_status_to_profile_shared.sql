-- Update any existing records with status 'ACTIVE' to 'PROFILE_SHARED'
-- This is needed because we removed 'ACTIVE' from the Status enum
UPDATE candidate_details_and_status_tracker 
SET status = 'PROFILE_SHARED' 
WHERE status = 'ACTIVE';
