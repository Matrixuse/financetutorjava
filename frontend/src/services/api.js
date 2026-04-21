import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/quiz",
  timeout: 10000,
});

export const getQuestion = (difficulty) =>
  api.get("/question", {
    params: { difficulty },
  });

export const evaluateAnswer = (payload) => api.post("/evaluate", payload);

export const explainTopic = (topic) =>
  api.get("/explain", {
    params: { topic },
  });

export default api;
