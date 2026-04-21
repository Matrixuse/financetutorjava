import axios from "axios";

const api = axios.create({
  baseURL: "https://financetutorjava.onrender.com/api/quiz",
  timeout: 15000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add request interceptor for debugging
api.interceptors.request.use(
  (config) => {
    console.log("API Request:", config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => Promise.reject(error)
);

// Add response interceptor for debugging
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error Details:", {
      status: error.response?.status,
      statusText: error.response?.statusText,
      message: error.message,
      url: error.config?.url,
    });
    return Promise.reject(error);
  }
);

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
