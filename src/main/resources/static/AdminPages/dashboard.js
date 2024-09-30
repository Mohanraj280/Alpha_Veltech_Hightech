// Function to check user authorization on page load
async function checkAuthorization() {
    try {
        // Fetch the authorization status from the server
        const response = await fetch('/dashboardAuth', {
            method: 'GET',
            credentials: 'include' // Include cookies with the request
        });

        if (response.status === 403) {
            // If unauthorized, redirect to the login page
            window.location.href = '/AdminPages/admin.html';
        } else {
            // Handle authorized response, e.g., load modules or data
            const data = await response.text(); // Can also use response.json() if data is in JSON format
            console.log(data); // Log the response or handle it as needed
        }
    } catch (error) {
        console.error('Error checking authorization:', error);
        // Redirect to the login page in case of a network error
        window.location.href = '/AdminPages/admin.html';
    }
}

// Call the authorization check when the window loads
window.onload = checkAuthorization;
