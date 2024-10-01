// Add an event listener for the 'submit' event on the create assessment form
document.getElementById('createAssessmentForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the default form submission behavior

    // Retrieve the values from the input fields
    const question = document.getElementById('question').value; // Get the question text
    const options = document.getElementById('options').value; // Get the options for the question
    const correctAnswer = document.getElementById('correctAnswer').value; // Get the correct answer

    // Send a POST request to the backend API to create a new question
    fetch('http://localhost:8080/api/questions/create', {
        method: 'POST', // Specify the request method
        headers: {
            'Content-Type': 'application/json', // Set the content type to JSON
        },
        body: JSON.stringify({ question, options, correctAnswer }), // Convert the data to a JSON string
    })
        // Handle the response from the server
        .then(response => response.json()) // Parse the JSON response
        .then(data => {
            // Display a success message when the assessment is created
            document.getElementById('message').textContent = "Assessment created!";
        })
        // Handle any errors that occur during the fetch
        .catch(error => console.error('Error:', error)); // Log the error to the console
});
