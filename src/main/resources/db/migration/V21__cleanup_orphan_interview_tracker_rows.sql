-- Remove orphan interview_tracker rows whose id doesn't exist in candidate_details_and_status_tracker
-- Safe for MySQL; uses a LEFT JOIN filter

DELETE it
FROM interview_tracker it
LEFT JOIN candidate_details_and_status_tracker c ON c.id = it.id
WHERE c.id IS NULL;


