# Idiomatic

Idiomatic is a telegram bot that allows you to manage your own gifs and query them like the official @gif bot.

---
## Installation
1. Install docker and docker-compose
2. Download docker-compose.yml and .env.example from this repository
3. Configure the .env.example file and rename it to .env
4. ```docker-compose up -d```

## Building from scratch
```
$ sbt docker:stage
$ cd target/docker/stage && docker buildx build --platform linux/amd64,linux/arm64 -t <username>/<image>:<tag> --push .
```
