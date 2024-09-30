function fetchModules() {
    // Fetch and display modules from the server
    fetch('/api/api/modules')
        .then(response => response.json())
        .then(modules => {
            const container = document.getElementById('modules-container');
            modules.forEach(module => {
                const moduleDiv = document.createElement('div');
                moduleDiv.classList.add('module'); // Assign CSS class for styling

                const moduleName = document.createElement('h2');
                moduleName.textContent = module.moduleName; // Set the module name

                const moduleDescription = document.createElement('p');
                moduleDescription.textContent = module.moduleDescription; // Set the module description


                // Append title and description to the module div
                moduleDiv.appendChild(moduleName);
                moduleDiv.appendChild(moduleDescription);
                container.appendChild(moduleDiv); // Append module div to the container
            });
        })
        .catch(error => {
            console.error('Error fetching modules:', error); // Log errors in fetching modules
        });
}

document.getElementById("registrationForm").addEventListener("submit", function(event) {
    event.preventDefault();  // Prevent the form from submitting in the traditional way

    // Get form data
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // Prepare data to be sent
    const formData = new URLSearchParams();
    formData.append("username", username);
    formData.append("password", password);

    // Send an AJAX request to the server
    fetch("/api/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData.toString()
    })
        .then(response => response.json())
        .then(data => {
            // Handle the server response
            const messageContainer = document.getElementById("message-container");
            if (data.status === "success") {
                messageContainer.innerHTML = `<p style="color: green;">${data.message}</p>`;
            } else {
                messageContainer.innerHTML = `<p style="color: red;">${data.message}</p>`;
            }
        })
        .catch(error => {
            console.error("Error during registration:", error);
            document.getElementById("message-container").innerHTML = `<p style="color: red;">An error occurred. Please try again later.</p>`;
        });
    document.getElementById('registrationForm').reset();
});

// Fetch the modules when the page loads
window.onload = fetchModules;
