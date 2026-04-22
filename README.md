💰 Finance Tutor Bot

An AI-powered web application that helps users learn financial concepts interactively through quizzes, answer evaluation, and topic explanations.

🚀 Overview

Finance Tutor Bot is a full-stack application that combines:

🎯 Interactive quizzes
🤖 AI-generated questions & explanations
📊 Answer evaluation
📚 Concept learning on demand

It is designed to make finance learning simple, engaging, and practical.

🧠 Key Features
1. Quiz System
Generates finance-related questions dynamically
Supports difficulty levels:
Easy
Medium
Hard
2. Answer Evaluation
Users submit answers
Backend evaluates using AI
Returns:
Score (optional based on your feature)
Correctness
3. Topic Explanation
User enters any finance topic
AI generates a simple explanation
4. Full Stack Architecture
Frontend ↔ Backend communication via REST APIs
🏗️ Tech Stack
🔹 Frontend
React.js
Axios (API calls)
CSS (UI styling)
🔹 Backend
Java
Spring Boot
REST APIs
🔹 AI Integration
Groq API (LLM for:
Question generation
Answer evaluation
Topic explanation)
🔹 Deployment
Backend → Render (Docker)
Frontend → Render Static Site
⚙️ Project Architecture
Frontend (React)
        ↓
Axios API Calls
        ↓
Backend (Spring Boot)
        ↓
Groq API (LLM)
🔗 API Endpoints

Base URL:

https://financetutorjava.onrender.com/api/quiz
📌 1. Get Question
GET /question?difficulty=easy
📌 2. Evaluate Answer
POST /evaluate

Body:

{
  "question": "string",
  "userAnswer": "string"
}
📌 3. Explain Topic
GET /explain?topic=bonds
🔧 How It Works (Flow)
🟢 Step 1: User selects difficulty

Frontend sends:

GET /question
🟢 Step 2: Backend
Receives request
Calls Groq API
Generates question
Sends response
🟢 Step 3: User submits answer

Frontend sends:

POST /evaluate
🟢 Step 4: Backend
Sends data to Groq
Gets evaluation
Returns result
🟢 Step 5: Topic Explanation

Frontend:

GET /explain

Backend:

Calls AI
Returns explanation
🧪 Local Setup
🔹 Backend
cd backend
mvn spring-boot:run

Runs on:

http://localhost:8080
🔹 Frontend
cd frontend
npm install
npm run dev

Runs on:

http://localhost:5173
🌐 Deployment Notes
https://financetutor-1.onrender.com/

⚠️ Important Fixes Done
✅ Removed API key from GitHub (security issue)
✅ Used Docker for Java deployment
✅ Fixed CORS issue for frontend-backend communication
✅ Configured environment variables
🔐 Environment Variables
Backend (.env or Render)
GROQ_API_KEY=your_api_key_here
⚠️ Common Issues & Fixes
❌ CORS Error

✔ Fixed using WebConfig.java

❌ 400 Bad Request

✔ Ensure:

/question?difficulty=easy
❌ Backend not responding

✔ Check:

Render logs
Port configuration
API URL in frontend
📌 Future Improvements
User authentication
Progress tracking
Score analytics dashboard
Leaderboard system
More finance topics
🎯 Interview Explanation (Short)

"This is a full-stack AI-powered finance learning app where users can practice quizzes, get answers evaluated, and understand concepts.
Frontend is built in React, backend in Spring Boot, and AI features are powered using Groq API.
The system dynamically generates questions and explanations using LLMs and is deployed using Render."

👨‍💻 Author

Naman Sharma
