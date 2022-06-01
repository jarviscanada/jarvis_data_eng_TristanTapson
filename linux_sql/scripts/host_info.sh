#Setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of args
if [ $# -ne  5 ]; then
  echo "Illegal number of arguments"
  echo "Try: bash host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]"
  exit 1
fi

#Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)
lscpu_out=`lscpu`

#Retrieve hardware specification variables
#parse hardware specification
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model\sname:" | sed -e 's/.*://' | tail -n1 | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU\sMHz:" | awk '{print $3}' | tail -n1 | xargs)
L2_cache=$(echo "$lscpu_out"  | egrep "^L2\scache:" | awk '{print $3}' | sed 's/.$//' | tail -n1 | xargs)
total_mem=$(cat /proc/meminfo | egrep "^MemTotal:" | awk '{print $2}' | xargs)

#Current time in `2019-11-26 14:40:19` UTC format
timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1 | xargs)

#insert_stmt="INSERT INTO host_usage(timestamp, ...) VALUES('$timestamp', ..."
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, "timestamp") VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$L2_cache', '$total_mem', '$timestamp');"

#set up env var for pql cmd
export PGPASSWORD=$psql_password

#Insert data into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?