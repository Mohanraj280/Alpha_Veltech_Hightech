document.getElementById('createAssessmentForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const question = document.getElementById('question').value;
    const options = document.getElementById('options').value;
    const correctAnswer = document.getElementById('correctAnswer').value;

    fetch('http://localhost:8080/api/questions/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ question, options, correctAnswer }),
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('message').textContent = "Assessment created!";
        })
        .catch(error => console.error('Error:', error));
});
