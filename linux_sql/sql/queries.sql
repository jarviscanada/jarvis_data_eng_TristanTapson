-- timestamp helper function, rounds current timestamp every 5 minutes
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;

-- percentage helper function, to calculate memory percentage used --
-- _num2 is divided by 1000 due to kB to mB conversion --
CREATE FUNCTION memUseCalc_(_num1 integer, _num2 integer) RETURNS integer AS
$$
BEGIN
    RETURN (((_num2/1000.0) - (_num1)) / (_num2/1000.0)) * 100.0;
END;
$$
    LANGUAGE PLPGSQL;

-- hosts grouped by hardware info --
-- hosts are grouped by ascending CPU number order, and descending total memory
SELECT cpu_number, id AS host_id, total_mem
FROM host_info
ORDER BY cpu_number ASC, total_mem DESC;

-- average memory usage --
-- average used memory in percentage over 5 min intervals for each host, calculated using a helper function
SELECT host_info.id AS host_id,
       host_info.hostname AS host_name,
       round5(host_usage.timestamp) AS timestamp,
       memUseCalc_(CAST (AVG(host_usage.memory_free) AS integer), host_info.total_mem) AS avg_used_mem_percentage
FROM host_usage, host_info
GROUP BY host_info.id, host_info.hostname, round5(host_usage.timestamp);

-- detect host failure
-- finds all server failures (less than 3 inserts) over a host's 5 minute interval by counting existing rows
SELECT host_info.id AS host_id,
       round5(host_usage.timestamp) AS timestamp,
       count(*) AS num_data_points
FROM host_usage, host_info
GROUP BY host_info.id, round5(host_usage.timestamp)
HAVING COUNT(*) < 3
ORDER BY host_id ASC;
