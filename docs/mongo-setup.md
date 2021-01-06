# Mongo Docker container setup
This is the guide how to run single MongoDB docker container hosting
multiple databases and having data stored on local file system directory.
```
docker run -d --name mongo-db \
    -p 27017:27017 \
    -e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
    -e MONGO_INITDB_ROOT_PASSWORD=secret \
    -v /opt/micro-services/mongo-data:/data/db \
    mongo:4.2.11
```

```
./mongo --username mongoadmin --password --host localhost:27017
show dbs
use admin
db.createUser(
   {
     user: "eventloggeruser",
     pwd: "secret",
     roles: [ { role: "readWrite", db: "loggerdb" } ]
   }
)
use loggerdb
```