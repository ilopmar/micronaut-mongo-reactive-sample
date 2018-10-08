# Micronaut Mongo Reactive example #

Run the application and send the request:

```bash
curl -X POST localhost:8080/users -d '{"name": "Iván","age": 38}' -H 'Content-Type:application/json'
```

You can run Mongo in a Docker container:
```bash
$ docker run  -p 27017:27017 bitnami/mongodb:latest
```