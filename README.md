A project to analyze the number of road accidents depending on weather conditions and time of day in St. Petersburg over the past 10 years

## Instruction
To start the PostgreSQL database, navigate to the root directory of the project (where the docker-compose.yml file is located) and run the following command in your terminal:
```Bash
docker-compose up -d
```

docker-compose up builds, (re)creates, starts, and attaches to containers for a service.
The -d flag (detached mode) runs the containers in the background.
The database will now be running and accessible on localhost:5432.

To stop and remove the database container, run the following command from the same directory:
```Bash
docker-compose down
```
This will stop the container, but the data will be preserved in the postgres_data Docker volume, so you won't lose it the next time you run docker-compose up -d.