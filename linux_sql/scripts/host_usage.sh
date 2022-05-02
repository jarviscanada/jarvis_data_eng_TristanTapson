#Setup and validate arguments (again, don't copy comments)
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of args
if [ $# -ne  5 ]; then
  echo 'Incorrect number of arguments'
  echo "Try: bash host_info [psql_host] [psql_port] [db_name] [psql_user] [psql_password]"
  exit 1
fi

#Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

#Retrieve hardware specification variables
#xargs is a trick to trim leading and trailing white spaces
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}'| tail -n1 | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}'| tail -n1 | xargs)
disk_io=$(vmstat -d | awk '{print $10}' | tail -n1 | xargs)

# temporary variable created for available disk space
# the character for the unit (M) is chopped off at the end of the string
disk_available=$( df -BM | egrep "/dev/sda2" | awk '{print $4}' | sed 's/.$//' | tail -n1 | xargs)


#Current time in `2019-11-26 14:40:19` UTC format
timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1 | xargs)

#echo "$vmstat_mb"
echo memory_free: $memory_free
echo cpu_idle: $cpu_idle
echo cpu_kernel: $cpu_kernel
echo disk_io: $disk_io
echo disk_available: $disk_available
echo timestamp: $timestamp

#lscpu_out=`lscpu`
#cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):")
#echo "$lscpu_out"
#echo $cpu_number


#cpu_idle=$(echo "$vmstat_mb" ...
#cpu_kernel=$(echo "$vmstat_mb" ...
#disk_io=$(vmstat -d | awk '{print $10}' ...
#disk_available=$(df -BM / ...

#Current time in `2019-11-26 14:40:19` UTC format
#timestamp=$(vmstat -t | awk ...

#Subquery to find matching id in host_info table
#host_id=$(psql -h $psql_host -p $psql_port -d $psql_dbName -U $psql_user -t -c "SELECT id FROM host_info WHERE hostname='$hostname'")
#host_id=(SELECT id FROM host_info WHERE hostname='$hostname')

#Insert date into a database
insert_stmt="INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$timestamp',(SELECT id FROM host_info WHERE hostname='$hostname'), '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')"

#set up env var for pql cmd
export PGPASSWORD=$psql_password


psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?