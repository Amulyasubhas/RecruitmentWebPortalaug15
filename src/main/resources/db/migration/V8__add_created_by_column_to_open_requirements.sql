ALTER TABLE open_requirement
ADD COLUMN created_by VARCHAR(100) NOT NULL DEFAULT 'unknown';
