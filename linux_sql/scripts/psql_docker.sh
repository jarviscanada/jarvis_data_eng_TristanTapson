#! /bin/sh

#capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

#Start docker
sudo systemctl status docker || systemctl start docker

#check container status
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
      echo "Try: bash psql_docker.sh create [db_username] [db_password]"
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

   # (check instance status; exit 1 if container has not been created)
  if [ $container_status -ne 0 ]
  then
    echo "Error: Container does not exist, cannot use start|stop commands"
    echo "Try: bash psql_docker.sh create [db_username] [db_password]"
    exit 1
  fi

  # start command executed
  if [ $cmd = "start" ]; then

    echo "Attempting to start a stopped container..."

    if [ "$state" = "exited" ]
    then
      echo 'Starting container sucessfully.'
      docker container $cmd jrvs-psql
      exit 0
    else
      echo 'Container has already been started.'
      exit 0
    fi
  fi

  # stop command executed
  if [ $cmd = "stop" ]; then

      echo "Attempting to stop a started container..."

      if [ "$state" = "running" ]
      then
        echo 'Stopping container sucessfully.'
        docker container $cmd jrvs-psql
        exit 0
      else
        echo 'Container has already been stopped.'
        exit 0
      fi
  fi
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac
