# AI chat using model hosted in local Ollama

## Requirements
- JDK 21
- Gradle isn't required because of embedded Gradle in the project
- Ollama (install locally or use it inside Docker container)
- Docker (recommended)
- NVIDIA GPU (recommended) (checked on GeForce RTX 3060 12Gb)

## Commands to communicate with Ollama inside Docker container

According to [instruction](https://ollama.com/blog/ollama-is-now-available-as-an-official-docker-image)

### Run Ollama inside a Docker container (using CPU)
```bash
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
```

### Run Ollama inside a Docker container (using NVIDIA GPU)
```bash
docker run -d --gpus=all -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
```

### Pull an AI model into Docker container
```bash
docker exec -it ollama ollama pull gemma3:4b
```

### List Ollama models inside Docker container
```bash
docker exec -it ollama ollama list
```

## How to build application
```bash
./gradlew clean build
```

## How to start application
```bash
java -jar build/libs/ai-chat-ollama-0.0.1-SNAPSHOT.jar
```

## How to ask question to AI
Make HTTP GET request to endpoint exposed by service:
```bash
curl -i http://localhost:8090/ai/generate?message=Перечисли%20типы%20в%20Java
```
