-- Delete data from all tables to have a fresh test environment ---

TRUNCATE TABLE analysis_history CASCADE;
TRUNCATE TABLE user_watchlist CASCADE;
TRUNCATE TABLE repositories CASCADE;
TRUNCATE TABLE languages CASCADE;
TRUNCATE TABLE domains CASCADE;
TRUNCATE TABLE owners CASCADE;