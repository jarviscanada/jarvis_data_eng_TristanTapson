#Setup and validate arguments (again, don't copy comments)
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of args
if [ $# -ne  5 ]; then
  echo "Illegal number of arguments"
  exit 1
fi

#Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)
lscpu_out=`lscpu`

#Retrieve hardware specification variables
#xargs is a trick to trim leading and trailing white spaces
#parse hardware specification
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model\sname:" | sed -e 's/.*://' | tail -n1 | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU\sMHz:" | awk '{print $3}' | tail -n1 | xargs)
L2_cache_temp=$(echo "$lscpu_out"  | egrep "^L2\scache:" | awk '{print $3}' | tail -n1 | xargs)
L2_cache="${L2_cache_temp::-1}"

meminfo=$(cat /proc/meminfo)
total_mem=$(echo "$meminfo" | egrep "^MemTotal:" | awk '{print $2}' | xargs)

#Current time in `2019-11-26 14:40:19` UTC format
timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1 | xargs)

echo cpu_number: $cpu_number
echo cpu_architecture: $cpu_architecture
echo cpu_model: $cpu_model
echo cpu_mhz: $cpu_mhz
#echo L2_cache_temp: $L2_cache_temp
echo L2_cache: $L2_cache
echo total_mem: $total_mem
echo timestamp: $timestamp
#PSQL command: Inserts server usage data into host_usage table
#Note: be careful with double and single quotes

#insert_stmt="INSERT INTO host_usage(timestamp, ...) VALUES('$timestamp', ..."
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, "timestamp") VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$L2_cache', '$total_mem', '$timestamp');"


#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?