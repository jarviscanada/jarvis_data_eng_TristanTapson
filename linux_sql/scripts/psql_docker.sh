#! /bin/sh

#capture CLI arguments (please do not copy comments)
cmd=$1
db_username=$2
db_password=$3

#Start docker
#Make sure you understand `||` cmd
sudo systemctl status docker || systemctl start docker

#check container status (try the following cmds on terminal)
docker container inspect jrvs-psql
container_status=$?

#User switch case to handle create|stop|start opetions
case $cmd in
  create)

  echo "creating container..."
  #echo $container_status

  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1
	fi

  #check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  #Create container
	docker volume create pgdata
	docker run --name jrvs-psql -e POSTGRES_USERNAME=$db_username -e POSTGRES_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
	exit $?
	;;

  start|stop)

  # global variable state, taken by inspecting the state of the docker
  state=$(docker inspect -f '{{.State.Status}}' jrvs-psql)
  echo 'Initial state: '$state
  #echo $container_status

   # (check instance status; exit 1 if container has not been created)
  if [ $container_status -ne 0 ]
  then
    echo "Error: Container does not exist, cannot use start|stop commands"
    echo "Try: bash psql_docker.sh create [db_username] [db_password]"
    exit 1
  fi

  #if [ $state = "running" ]; then
    #echo "RUNNN"
  #fi

  #if [ $state = "exited" ]; then
      #echo "EXITEDDD"
  #fi

  # start command executed
  if [ $cmd = "start" ]; then
    #echo 'container_status: '$container_status

    echo "Attempting to start a stopped container..."

    if [ "$state" = "exited" ]
    then
      echo 'Starting container sucessfully.'
      docker container $cmd jrvs-psql
      #echo 'final state: '$state
      exit 0
    else
      echo 'Container has already been started.'
      #docker container $cmd jrvs-psql
      #echo 'final state: '$state
      exit 0
    fi
  fi

  # stop command executed
  if [ $cmd = "stop" ]; then
      #echo 'container_status: '$container_status

      echo "Attempting to stop a started container..."

      if [ "$state" = "running" ]
      then
        echo 'Stopping container sucessfully.'
        docker container $cmd jrvs-psql
        #echo 'final state: '$state
        exit 0
      else
        echo 'Container has already been stopped.'
        #docker container $cmd jrvs-psql
        #echo 'final state: '$state
        exit 0
      fi
  fi
  #echo $cmd



  #Start or stop the container
  #echo "STATE: "$state
	#docker container $cmd jrvs-psql
	#echo $state
	#exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac