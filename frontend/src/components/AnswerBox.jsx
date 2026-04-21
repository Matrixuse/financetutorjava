import React from "react";

function AnswerBox({ answer, setAnswer }) {
  return (
    <div>
      <textarea
        rows="5"
        cols="50"
        placeholder="Type your answer..."
        value={answer}
        onChange={(e) => setAnswer(e.target.value)}
      />
    </div>
  );
}

export default AnswerBox;