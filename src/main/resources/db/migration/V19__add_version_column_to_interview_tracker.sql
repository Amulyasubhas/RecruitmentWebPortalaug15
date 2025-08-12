-- V19__add_version_column_to_interview_tracker.sql
-- Add version column for optimistic locking

ALTER TABLE interview_tracker ADD COLUMN version BIGINT DEFAULT 0;

-- Update existing records to have version 0
UPDATE interview_tracker SET version = 0 WHERE version IS NULL;
