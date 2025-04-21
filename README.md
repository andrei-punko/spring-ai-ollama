# AI chat with Ollama model using Spring AI

[![Java CI with Gradle](https://github.com/andrei-punko/spring-ai-ollama/actions/workflows/gradle.yml/badge.svg)](https://github.com/andrei-punko/spring-ai-ollama/actions/workflows/gradle.yml)

## Prerequisites
- JDK 21
- Gradle isn't required because of embedded Gradle in the project
- Docker (used by unit tests and as preferred way to start Ollama)
- Ollama (install it from [official site](https://ollama.com/download) or use it inside Docker container)
- NVIDIA GPU (recommended) (checked on GeForce RTX 3060 12Gb)
  - On Linux to use GPU you need to install `nvidia-container-toolkit` according
    to [article](https://stackoverflow.com/questions/25185405/using-gpu-from-a-docker-container)

## How to build application

```bash
./gradlew clean build
```

## How to start Docker containers with Ollama model and Spring AI application

```bash
docker compose -f docker-compose-prod.yml up
```
or use [run-all.bat](run-all.bat) script

## How to ask question to AI model

Send POST request to `/api/generate` endpoint exposed by service with question inside `prompt` field of request body.
For example:
```shell
curl -i -H 'Content-Type: application/json' \
  -d '{ "prompt": "Tell me about Belarus" }' \
  -X POST http://localhost:8090/api/generate
```

```shell
curl -i -H 'Content-Type: application/json' \
  -d '{ "prompt": "Describe primitive types in Java" }' \
  -X POST http://localhost:8090/api/generate
```

```shell
curl -i -H 'Content-Type: application/json' \
  -d '{ "prompt": "Write code of bubble sort using Java" }' \
  -X POST http://localhost:8090/api/generate
```

Or you could use prepared collection of Postman requests from [postman folder](postman). Just import them into your
Postman

## Video with description of the project

[//]: # ([YouTube link]&#40;https://youtu.be/SoDIjw-Ov8o&#41;)
[![YouTube link](https://markdown-videos-api.jorgenkh.no/url?url=https%3A%2F%2Fyoutu.be%2FSoDIjw-Ov8o)](https://youtu.be/SoDIjw-Ov8o)

## Appendix: communication with Ollama started inside a Docker container

According to [instruction](https://ollama.com/blog/ollama-is-now-available-as-an-official-docker-image)

### Run Ollama inside a Docker container (using NVIDIA GPU) (preferred)
```bash
docker run -d --gpus=all -v ollama:/root/.ollama -p 11434:11434 --name ollama_container ollama/ollama
```
or

### Run Ollama inside a Docker container (using CPU)
```bash
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama_container ollama/ollama
```

### Pull an AI model into Ollama Docker container
```bash
docker exec -it ollama_container ollama pull gemma3:4b
```

### List AI models inside Ollama Docker container
```bash
docker exec -it ollama_container ollama list
```

### Stop Docker container
```bash
docker stop ollama_container && docker rm ollama_container
```
