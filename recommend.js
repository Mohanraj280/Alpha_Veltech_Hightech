const express = require('express');
const { GoogleGenerativeAI, HarmCategory, HarmBlockThreshold } = require('@google/generative-ai');
const dotenv = require('dotenv').config();

const app = express();
const port = process.env.PORT || 3000;
app.use(express.json());

// Define the model name and API key from the environment variables
const MODEL_NAME = "gemini-pro";
const API_KEY = process.env.API_KEY;

async function runChat(userInput, chatHistory) {
    const genAI = new GoogleGenerativeAI(API_KEY);
    const model = genAI.getGenerativeModel({ model: MODEL_NAME });

    const generationConfig = {
        temperature: 0.9,
        topK: 1,
        topP: 1,
        maxOutputTokens: 1000,
    };

    const safetySettings = [
        {
            category: HarmCategory.HARM_CATEGORY_HARASSMENT,
            threshold: HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
        },
        // Add other safety settings as needed
    ];

    const chat = model.startChat({
        generationConfig,
        safetySettings,
        history: chatHistory,
    });

    const result = await chat.sendMessage(userInput);
    const responseText = result.response.text();

    // Format the response text
    const formattedResponse = formatResponse(responseText);

    return { formattedResponse };
}

// Function to format the response into structured feedback
function formatResponse(responseText) {
    // Replace asterisks with markdown syntax for bold text
    let formattedText = responseText
        .replace(/\*\*Feedback:\*\*/g, "\n**Feedback:**\n")
        .replace(/\*\*Strengths:\*\*/g, "\n**Strengths:**\n")
        .replace(/\*\*Areas for Improvement:\*\*/g, "\n**Areas for Improvement:**\n")
        .replace(/\*\*Recommendations for Skill Improvement:\*\*/g, "\n**Recommendations for Skill Improvement:**\n")
        .replace(/\*\*Additional Tips:\*\*/g, "\n**Additional Tips:**\n");

    

    return formattedText.trim();
}

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/main.html');
});

app.post('/chat', async (req, res) => {
    try {
        const userInput = req.body?.userInput;
        console.log('Incoming /chat request:', userInput);

        if (!userInput) {
            return res.status(400).json({ error: 'Invalid request body' });
        }

        // Example of maintaining the chat history
        const chatHistory = [
            {
                role: "user",
                parts: [{ text: "You are an assessment assistant that gives feedback and recommendations for skill improvement based on user performance." }],
            },
            {
                role: "model",
                parts: [{ text: "Please provide the user's performance details." }],
            },
            {
                role: "user",
                parts: [{ text: userInput }],
            }
        ];

        // Generate response using the AI model
        const { formattedResponse } = await runChat(userInput, chatHistory);

        res.json({ response: formattedResponse });
    } catch (error) {
        console.error('Error in chat endpoint:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});
