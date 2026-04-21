import React from "react";

function ResultCard({ result }) {
  return (
    <div>
      <h3>Evaluation:</h3>
      <p>{result}</p>
    </div>
  );
}

export default ResultCard;