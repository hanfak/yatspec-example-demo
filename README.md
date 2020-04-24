mkdir -p $HOME/docker/volumes/postgres
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11
docker exec -it postgres bash
psql -h localhost -U postgres -d postgres 
 psql -h localhost -U postgres -d postgres -W
 
 create database "starwarslocal";
 -- conntect to db
 \c "starwarslocal"
 --show tables
 \dt
 -- show schema
 \d+ <name of table>