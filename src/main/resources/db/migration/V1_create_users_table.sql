-- V1__create_users_and_linkedin_tables.sql
-- Create Linkedin table
CREATE TABLE linkedin (
  id BIGSERIAL PRIMARY KEY,
  liat_cookie VARCHAR(512),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255)
);

-- Create Users table
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  uuid VARCHAR(255),
  verified BOOLEAN DEFAULT FALSE,
  role VARCHAR(50) NOT NULL,
  linkedin_id BIGINT UNIQUE,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Foreign key for one-to-one relationship
ALTER TABLE users ADD CONSTRAINT fk_users_linkedin FOREIGN KEY (linkedin_id) REFERENCES linkedin (id) ON DELETE CASCADE;
