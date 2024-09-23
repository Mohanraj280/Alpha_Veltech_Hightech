let questions = []; // Array to store questions
let currentQuestionIndex = 0; // Index of the current question
let answers = {}; // Object to store user's answers
const questionNav = document.getElementById('question-nav'); // Navigation element for questions
const questionText = document.getElementById('question-text'); // Element to display the current question
const optionsContainer = document.getElementById('options-container'); // Container for answer options
const submissionModal = document.getElementById('submissionModal'); // Modal for submission summary
const confirmSubmitBtn = document.getElementById('confirm-submit-btn'); // Button to confirm submission
const loadingIndicator = document.createElement('p'); // Create a loading indicator element
loadingIndicator.textContent = "Submitting... Please wait."; // Text for the loading indicator
loadingIndicator.style.display = 'none'; // Initially hide the loading indicator
submissionModal.querySelector('.modal-body').appendChild(loadingIndicator); // Add loading indicator to the modal

// Initialize camera feed (optional)
startCameraFeed()
    .then(() => console.log('Camera feed started successfully')) // Log success message
    .catch(err => console.error('Error starting camera feed:', err)); // Log error message

// Fetch questions dynamically based on selected module
async function fetchQuestionsByModule(moduleId) {
    try {
        const response = await fetch(`/api/assessment/${moduleId}/questions`); // Fetch questions from API
        questions = await response.json(); // Parse JSON response and store in questions array
        updateQuestionNav(); // Update question navigation buttons
        loadQuestion(currentQuestionIndex); // Load the first question
    } catch (error) {
        console.error('Error fetching questions:', error); // Log any fetch errors
    }
}

// Navigate to a specific question by index
function navigateToQuestion(index) {
    currentQuestionIndex = index; // Update the current question index
    loadQuestion(currentQuestionIndex); // Load the selected question
    updateQuestionNav(); // Update navigation buttons
}

// Load a question dynamically based on the current index
function loadQuestion(index) {
    const question = questions[index]; // Get the current question
    questionText.textContent = `${index + 1}. ${question.questionText}`; // Display the question text
    optionsContainer.innerHTML = ''; // Clear previous options

    question.options.forEach((option, i) => { // Loop through each option
        const optionLabel = String.fromCharCode(65 + i); // Create option label (A, B, C, ...)
        const optionDiv = document.createElement('div'); // Create a div for the option
        optionDiv.className = 'form-check'; // Add form-check class for styling
        optionDiv.innerHTML = ` 
            <input class="form-check-input" type="radio" name="option" value="${option}" id="option${i}" ${answers[question.id] === option ? 'checked' : ''}> 
            <label class="form-check-label" for="option${i}">${optionLabel}. ${option}</label> 
        `; // Create radio button and label
        optionsContainer.appendChild(optionDiv); // Add the option div to the container
    });

    document.querySelectorAll('input[name="option"]').forEach(input => { // Add change event listeners to radio buttons
        input.addEventListener('change', function() {
            answers[question.id] = this.value; // Store the selected answer
        });
    });
}

// Update question navigation buttons dynamically
function updateQuestionNav() {
    questionNav.innerHTML = ''; // Clear previous navigation buttons
    questions.forEach((_, index) => { // Loop through each question
        const navButton = document.createElement('button'); // Create a navigation button
        navButton.className = `nav-item ${index === currentQuestionIndex ? 'active' : ''}`; // Set active class if it's the current question
        navButton.textContent = index + 1; // Set button text to question number
        navButton.onclick = () => navigateToQuestion(index); // Add click event to navigate
        questionNav.appendChild(navButton); // Add button to navigation
    });
}

// Display the submission summary modal
function showSummaryModal() {
    const attendedQuestions = Object.keys(answers).filter(key => answers[key] !== null).length; // Count attended questions
    const notAttendedQuestions = questions.length - attendedQuestions; // Calculate unanswered questions

    document.getElementById('summary-text').innerHTML = ` 
        Total Questions: ${questions.length}<br> 
        Attended Questions: ${attendedQuestions}<br> 
        Not Attended Questions: ${notAttendedQuestions} 
    `; // Display summary information
    submissionModal.style.display = 'block'; // Show the submission modal
}

// Set the duration for the timer (e.g., 10 minutes)
let duration = 1 * 10; // 10 minutes in seconds
let timerDisplay = document.getElementById('time-remaining'); // Timer display element

// Start the timer when the page loads
window.onload = startTimer; // Call startTimer when the window loads
function startTimer() {
    let timer = setInterval(function() { // Create a timer that runs every second
        let minutes = parseInt(duration / 60, 10); // Calculate minutes remaining
        let seconds = parseInt(duration % 60, 10); // Calculate seconds remaining

        // Format the time remaining
        seconds = seconds < 10 ? '0' + seconds : seconds; // Add leading zero if necessary
        timerDisplay.textContent = minutes + ":" + seconds; // Update the timer display

        // If the timer reaches zero
        if (--duration < 0) {
            clearInterval(timer); // Stop the timer
            submitAnswers(); // Call your function to submit the assessment
        }
    }, 1000); // Run every 1000 milliseconds (1 second)
}

// Submit answers to the server
async function submitAnswers() {
    confirmSubmitBtn.disabled = true; // Disable the submit button to prevent multiple submissions
    loadingIndicator.style.display = 'block'; // Show the loading indicator

    const moduleId = sessionStorage.getItem('selectedModuleId'); // Get the selected module ID from session storage
    try {
        stopMediaStreams(); // Stop any media streams
        alert("Your answers have been submitted. Thank you!"); // Alert the user of submission
        window.location.href = 'logout.html';  // Logout or redirect after submission

        const response = await fetch('/api/submit-answers', { // Send the answers to the server
            method: 'POST', // Set request method to POST
            headers: { 'Content-Type': 'application/json' }, // Set content type to JSON
            body: JSON.stringify({moduleId, answers }) // Send module ID and answers as JSON
        });

        const data = await response.json(); // Parse JSON response
        if (!data.success) { // Check for success
            console.error('Error sending results:', data.message); // Log error message
        }

    } catch (error) {
        console.error('Error:', error); // Log any fetch errors
    } finally {
        confirmSubmitBtn.disabled = false; // Re-enable the submit button
        loadingIndicator.style.display = 'none'; // Hide the loading indicator
    }
}

// Stop media streams after submitting
function stopMediaStreams() {
    if (mediaStream) { // Check if mediaStream is defined
        mediaStream.getTracks().forEach(track => track.stop()); // Stop each track of the media stream
    }
}

// Event listener for DOM content loaded
document.addEventListener('DOMContentLoaded', () => {
    const moduleId = sessionStorage.getItem('selectedModuleId'); // Get selected module ID from session storage
    fetchQuestionsByModule(moduleId); // Fetch questions for the selected module

    // Event listener for next button
    document.getElementById('next-btn').addEventListener('click', () => {
        if (currentQuestionIndex < questions.length - 1) { // Check if not on the last question
            navigateToQuestion(currentQuestionIndex + 1); // Move to the next question
        } else {
            showSummaryModal(); // Show summary modal if on the last question
        }
    });

    // Event listener for previous button
    document.getElementById('prev-btn').addEventListener('click', () => {
        if (currentQuestionIndex > 0) { // Check if not on the first question
            navigateToQuestion(currentQuestionIndex - 1); // Move to the previous question
        }
    });

    // Event listener for confirm submit button
    confirmSubmitBtn.addEventListener('click', async () => {
        await submitAnswers(); // Call submitAnswers when clicked
    });

    // Event listener for modal close button
    document.getElementById('modal-close').addEventListener('click', () => {
        submissionModal.style.display = 'none'; // Hide the submission modal when closed
    });
});
