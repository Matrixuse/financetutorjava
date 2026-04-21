import { useEffect, useRef, useState } from "react";
import { evaluateAnswer, explainTopic, getQuestion } from "../services/api";

export default function QuizPage() {
  const didLoadInitialQuestion = useRef(false);
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [score, setScore] = useState("");
  const [feedback, setFeedback] = useState("");
  const [difficulty, setDifficulty] = useState("easy");
  const [topic, setTopic] = useState("");
  const [explanation, setExplanation] = useState("");
  const [error, setError] = useState("");
  const [loadingQuestion, setLoadingQuestion] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [explaining, setExplaining] = useState(false);
  const [level, setLevel] = useState("easy");

  const getErrorMessage = (err, fallbackMessage) =>
    err?.response?.data?.message ||
    err?.response?.data?.error ||
    fallbackMessage;

  const loadQuestion = async () => {
    setEvaluation("");
    setAnswer("");

    try {
      const res = await getQuestion(level);
      setQuestion(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    if (didLoadInitialQuestion.current) {
      return;
    }

    didLoadInitialQuestion.current = true;

    const initializeQuestion = async () => {
      setLoadingQuestion(true);
      setError("");
      setScore("");
      setFeedback("");

      try {
        const res = await getQuestion(level);
        setQuestion(res.data.question ?? "No question received.");
      } catch (err) {
        setQuestion("");
        setError(
          getErrorMessage(
            err,
            "Unable to load a question. Make sure the backend is running."
          )
        );
      } finally {
        setLoadingQuestion(false);
      }
    };

    void initializeQuestion();
  }, [difficulty]);

  const handleSubmit = async () => {
    if (!question || !answer.trim()) {
      setError("Enter an answer before submitting.");
      return;
    }

    setSubmitting(true);
    setError("");

    try {
      const res = await evaluateAnswer({
        question,
        answer,
      });
      setScore(res.data.score ?? "No score returned.");
      setFeedback(res.data.feedback ?? res.data.evaluation ?? "No feedback available.");
    } catch (err) {
      setScore("");
      setFeedback("");
      setError(
        getErrorMessage(
          err,
          "Unable to evaluate your answer right now. Check the backend logs and try again."
        )
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleExplain = async () => {
    if (!topic.trim()) {
      setError("Enter a topic to explain.");
      return;
    }

    setExplaining(true);
    setError("");
    setExplanation("");

    try {
      const res = await explainTopic(topic.trim());
      setExplanation(res.data.explanation ?? "No explanation returned.");
    } catch (err) {
      setError(
        getErrorMessage(
          err,
          "Unable to explain that topic right now. Check the backend and try again."
        )
      );
    } finally {
      setExplaining(false);
    }
  };

  return (
    <div className="quiz-container">
      {/* Header */}
      <div className="quiz-header">
        <h1>Finance Tutor</h1>
        <p>Master financial concepts with interactive quizzes</p>
      </div>

      {/* Error Message - Display at top if exists */}
      {error && (
        <div className="error-message">
          ⚠️ {error}
        </div>
      )}

      {/* Question Card */}
      <div className="card">
        <div className="card-in">
          <h3>Question</h3>
          <label className="difficulty-label">Select Level: </label>
          <select 
            className="difficulty-select"
            value={level}
            onChange={(e) => setDifficulty(e.target.value)}
          >
            <option value="easy">Easy</option>
            <option value="medium">Medium</option>
            <option value="hard">Hard</option>
          </select>
        </div>
        <div className="question-section">
          <p>
            {loadingQuestion ? (
              <span className="loading-text">Loading question...</span>
            ) : question ? (
              question
            ) : (
              "No question loaded yet."
            )}
          </p>
        </div>
        <h3>Your Answer</h3>
        <textarea
          placeholder="Type your answer here..."
          value={answer}
          onChange={(e) => setAnswer(e.target.value)}
        />
        <div className="button-group">
          {!score ? (
            <button 
              className="btn-primary"
              onClick={handleSubmit} 
              disabled={submitting || loadingQuestion}
            >
              {submitting ? "Submitting..." : "Submit Answer"}
            </button>
          ) : (
            <button 
              className="btn-secondary"
              onClick={loadQuestion} 
              disabled={loadingQuestion}
            >
              {loadingQuestion ? "Loading..." : "Next Question"}
            </button>
          )}
        </div>
      </div>

      {/* Score and Feedback Section */}
      {score && (
        <div className="score-section">
          <h3>🎯 Your Score</h3>
          <div className="score-value">{score}</div>
          {feedback && (
            <div className="feedback-box">
              <h4>Explanation</h4>
              <p>{feedback}</p>
            </div>
          )}
        </div>
      )}

      {/* Divider */}
      <hr className="divider" />

      {/* Explanation Section */}
      <div className="card">
        <h3>Want to Ask ?</h3>
        <p style={{ marginBottom: "15px" }}>
          Want to ask any financial topic ? Ask me anything!
        </p>
        <input
          type="text"
          placeholder="Enter topic (e.g. inflation, stocks, bonds)..."
          value={topic}
          onChange={(e) => setTopic(e.target.value)}
        />
        <div className="button-group">
          <button 
            className="btn-primary"
            onClick={handleExplain} 
            disabled={explaining}
          >
            {explaining ? "Explaining..." : "Explain"}
          </button>
        </div>
      </div>

      {/* Explanation Result */}
      {explanation && (
        <div className="explanation-section">
          <h3>Topic Explanation</h3>
          <div className="explanation-box">
            <p>{explanation}</p>
          </div>
        </div>
      )}
    </div>
  );
}
