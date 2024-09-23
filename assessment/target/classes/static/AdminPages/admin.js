document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
    })
        .then(response => response.text())
        .then(data => {
            document.getElementById('loginMessage').textContent = data;
            if (data === "Login successful!") {
                window.location.href = 'dashboard.html';  // Redirect to dashboard
            }
        })
        .catch(error => console.error('Error:', error));
});
