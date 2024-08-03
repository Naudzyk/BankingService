   DO $$
   BEGIN
       IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'bank_service_db') THEN
           CREATE DATABASE bank_service_db;
       END IF;
   END $$;

