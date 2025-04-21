#!/bin/bash

# Start Ollama in the background.
/bin/ollama serve &

# Record Process ID.
pid=$!
# Pause for Ollama to start.
sleep 5

echo "🔴 Retrieve $1 model..."
ollama pull $1
echo "🟢 Done!"

# Wait for the Ollama process to finish.
wait $pid
