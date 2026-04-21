import React from "react";

function QuestionCard({ question }) {
  return (
    <div>
      <h3>Question:</h3>
      <p>{question}</p>
    </div>
  );
}

export default QuestionCard;