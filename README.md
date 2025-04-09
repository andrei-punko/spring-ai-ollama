# AI chat using model hosted in Ollama

https://ollama.com/blog/ollama-is-now-available-as-an-official-docker-image

## Run Ollama inside a Docker container
```bash
docker run -d --gpus=all -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
```

## Pull an AI model into Docker container
```bash
docker exec -it ollama ollama pull gemma3:4b
```

## Run an AI model inside Docker container
```bash
docker exec -it ollama ollama run gemma3:4b
```

## List Ollama models inside Docker container
```bash
docker exec -it ollama ollama list
```

## Request example with question to AI
```bash
curl -i http://localhost:8090/ai/generate?message=Перечисли%20типы%20в%20Java
```
