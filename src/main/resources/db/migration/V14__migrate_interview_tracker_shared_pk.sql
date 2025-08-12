-- Migrate interview_tracker to use shared primary key with candidate_details_and_status_tracker
-- Safe/conditional migration compatible with MySQL

-- 1) Drop legacy foreign key fk_candidate if it exists
SET @has_fk := (
  SELECT COUNT(*)
  FROM information_schema.REFERENTIAL_CONSTRAINTS rc
  WHERE rc.CONSTRAINT_SCHEMA = DATABASE()
    AND rc.TABLE_NAME = 'interview_tracker'
    AND rc.CONSTRAINT_NAME = 'fk_candidate'
);
SET @sql := IF(@has_fk > 0, 'ALTER TABLE interview_tracker DROP FOREIGN KEY fk_candidate', 'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2) Drop legacy candidate_id column if it exists
SET @has_col := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'interview_tracker'
    AND COLUMN_NAME = 'candidate_id'
);
SET @sql := IF(@has_col > 0, 'ALTER TABLE interview_tracker DROP COLUMN candidate_id', 'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3) Ensure id is NOT NULL and not auto-increment, and primary key exists
-- Drop PK if exists (will be re-created)
SET @has_pk := (
  SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = DATABASE()
    AND TABLE_NAME = 'interview_tracker'
    AND CONSTRAINT_TYPE = 'PRIMARY KEY'
);
SET @sql := IF(@has_pk > 0, 'ALTER TABLE interview_tracker DROP PRIMARY KEY', 'DO 0');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Ensure id is not auto_increment and not null
ALTER TABLE interview_tracker MODIFY id BIGINT NOT NULL;

-- Recreate PK on id
ALTER TABLE interview_tracker ADD CONSTRAINT pk_interview_tracker PRIMARY KEY (id);

-- 4) Create new FK on id to candidate table if not exists
SET @has_new_fk := (
  SELECT COUNT(*) FROM information_schema.REFERENTIAL_CONSTRAINTS rc
  WHERE rc.CONSTRAINT_SCHEMA = DATABASE()
    AND rc.TABLE_NAME = 'interview_tracker'
    AND rc.CONSTRAINT_NAME = 'fk_interview_candidate'
);
SET @sql := IF(@has_new_fk = 0,
  'ALTER TABLE interview_tracker ADD CONSTRAINT fk_interview_candidate FOREIGN KEY (id) REFERENCES candidate_details_and_status_tracker(id)',
  'DO 0'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
