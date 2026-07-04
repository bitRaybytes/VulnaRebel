# Utilities help to fix the application

## Clean install or rebuild
```bash
# 1. remove old database fragments for clean install
docker-compose down -v

# 2. then build the app again
docker-compose up --build
```

## Logging and Debugging

```bash
docker-compose logs web

docker-compose logs db
```