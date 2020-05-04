* mkdir -p $HOME/docker/volumes/postgres
* docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11
* docker exec -it postgres bash
* psql -h localhost -U postgres -d postgres 
* psql -h localhost -U postgres -d postgres -W
 
 create database "starwarslocal";
 -- conntect to db
 \c "starwarslocal"
 --show tables
 \dt
 -- show schema
 \d+ <name of table>
 
 
********
 Access: 
 http://localhost:2222/usecase/Luke%20Skywalker
 
 Yatspec reports: 
 target/surefire-reports/yatspec
 
## Notes

- Run one test class at a time. All test run will take time, have not set hikari properly
- 