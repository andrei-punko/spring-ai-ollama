#!/bin/bash

# Start Ollama in the background.
/bin/ollama serve &

# Record Process ID.
pid=$!
# Pause for Ollama to start.
sleep 5

echo "🔴 Retrieve $OLLAMA_MODEL_NAME model..."
ollama pull "$OLLAMA_MODEL_NAME"
echo "🟢 Done!"

# Wait for the Ollama process to finish.
wait $pid
