// Add an event listener to the login form for the 'submit' event
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the default form submission behavior

    // Get the values from the username and password input fields
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Send a POST request to the server with the username and password
    fetch('http://localhost:8080/login', {
        method: 'POST', // Specify the request method
        headers: {
            'Content-Type': 'application/json', // Indicate that the request body is in JSON format
        },
        body: JSON.stringify({ username, password }), // Convert the username and password to a JSON string
    })
        .then(response => response.text()) // Parse the response as text
        .then(data => {
            // Display the response message in the loginMessage paragraph
            document.getElementById('loginMessage').textContent = data;

            // Check if the login was successful
            if (data === "Login successful!") {
                window.location.href = 'dashboard.html';  // Redirect to the dashboard page
            }
        })
        .catch(error => console.error('Error:', error)); // Log any errors that occur during the fetch
});
