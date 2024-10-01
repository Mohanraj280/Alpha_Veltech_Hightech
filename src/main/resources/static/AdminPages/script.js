let studentResults = []; // Define the studentResults array to hold the fetched data

// Function to fetch student results from the API
async function fetchStudentResults() {
    try {
        const response = await fetch('/api/student-results');  // Replace with your actual API endpoint
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        studentResults = await response.json(); // Store the results in the studentResults variable
        displayResults(studentResults); // Display the fetched results
    } catch (error) {
        console.error('Error fetching student results:', error);
    }
}

// Function to display results in the table
function displayResults(results) {
    const tableBody = document.getElementById('results-table-body');
    tableBody.innerHTML = ""; // Clear existing rows

    results.forEach(result => {
        const percentage = calculatePercentage(result.score, result.totalQuestions);
        const row = `<tr>
                        <td>${result.username}</td>
                        <td>${result.email}</td>
                        <td>${result.score}</td>
                        <td>${result.totalQuestions}</td>
                        <td>${percentage}%</td>
                        <td>${result.date}</td>
                    </tr>`;
        tableBody.innerHTML += row;
    });
}

// Function to calculate percentage
function calculatePercentage(score, totalQuestions) {
    return ((score / totalQuestions) * 100).toFixed(2);
}

// Initial fetch of student results
fetchStudentResults();

// Search functionality
document.getElementById('searchBtn').addEventListener('click', () => {
    const searchInput = document.getElementById('searchInput').value.toLowerCase();
    const filteredResults = studentResults.filter(result =>
        result.username.toLowerCase().includes(searchInput) ||
        result.email.toLowerCase().includes(searchInput)
    );
    displayResults(filteredResults);
});

// Sorting functionality
document.getElementById('sortAsc').addEventListener('click', () => {
    const sortedResults = [...studentResults].sort((a, b) => {
        const percentageA = calculatePercentage(a.score, a.totalQuestions);
        const percentageB = calculatePercentage(b.score, b.totalQuestions);
        return percentageA - percentageB; // Ascending order
    });
    displayResults(sortedResults);
});

document.getElementById('sortDesc').addEventListener('click', () => {
    const sortedResults = [...studentResults].sort((a, b) => {
        const percentageA = calculatePercentage(a.score, a.totalQuestions);
        const percentageB = calculatePercentage(b.score, b.totalQuestions);
        return percentageB - percentageA; // Descending order
    });
    displayResults(sortedResults);
});

// Fetch questions dynamically based on the selected module ID
