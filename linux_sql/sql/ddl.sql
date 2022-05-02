-- connect to host_agent database --
\c host_agent

-- creating the database table host_info and its fields --
CREATE TABLE IF NOT EXISTS PUBLIC.host_info
(
    id                  SERIAL NOT NULL PRIMARY KEY,
    hostname            varchar NOT NULL UNIQUE,
    cpu_number          int NOT NULL,
    cpu_architecture    varchar NOT NULL,
    cpu_model           varchar NOT NULL,
    cpu_mhz             float NOT NULL,
    L2_cache            int NOT NULL,
    total_mem           int NOT NULL,
    timestamp           timestamp NOT NULL
);

-- creating the database table host_usage and its fields --
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage
(
    timestamp       timestamp NOT NULL,
    host_id         SERIAL REFERENCES host_info(id),
    memory_free     int NOT NULL,
    cpu_idle        int NOT NULL,
    cpu_kernel      int NOT NULL,
    disk_io         int NOT NULL,
    disk_available  int NOT NULL
);
