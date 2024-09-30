let questions = []; // Array to store questions fetched from the server
let currentQuestionIndex = 0; // Index of the currently displayed question
let answers = {}; // Object to store the user's answers, keyed by question ID

// Element references for the question navigation, question text, options container, and submission modal
const questionNav = document.getElementById('question-nav');
const questionText = document.getElementById('question-text');
const optionsContainer = document.getElementById('options-container');
const submissionModal = document.getElementById('submissionModal');
const confirmSubmitBtn = document.getElementById('confirm-submit-btn');

// Create and set up a loading indicator element
const loadingIndicator = document.createElement('p'); // Create a new paragraph element for the loading indicator
loadingIndicator.textContent = "Submitting... Please wait."; // Set the text content
loadingIndicator.style.display = 'none'; // Hide the loading indicator initially
submissionModal.querySelector('.modal-body').appendChild(loadingIndicator); // Add loading indicator to the modal body

// Initialize camera feed (optional step for user monitoring)
startCameraFeed()
    .then(() => console.log('Camera feed started successfully')) // Log success message to console
    .catch(err => console.error('Error starting camera feed:', err)); // Log error if camera feed fails to start

// Fetch questions dynamically based on the selected module ID
async function fetchQuestionsByModule(moduleId) {
    try {
        const response = await fetch(`/api/assessment/${moduleId}/questions`); // Fetch questions from the server API

        // Check if the response status is not OK
        if (!response.ok) {
            if (response.status === 403) {
                // If unauthorized, redirect to the login page
                window.location.href = '/CandidatePages/main.html';
                return; // Exit the function
            } else if (response.status === 404) {
                // If no questions are found, handle accordingly
                console.error('Module not found or no questions available.'); // Log error to console
                alert('No questions available for this module.'); // Alert the user
                window.location.href = '/CandidatePages/main.html'; // Redirect to main page
                return; // Exit the function
            } else {
                // Log any other errors
                console.error(`Error fetching questions: ${response.statusText}`);
                alert(`Error: ${response.statusText}`); // Alert the user about the error
                return; // Exit the function
            }
        }

        // Parse JSON response and store it in the questions array if response is OK
        questions = await response.json();
        updateQuestionNav(); // Update the question navigation buttons based on the number of questions
        loadQuestion(currentQuestionIndex); // Load the first question for display

    } catch (error) {
        console.error('Error fetching questions:', error); // Log any errors during the fetch process
    }
}

// Navigate to a specific question by its index
function navigateToQuestion(index) {
    currentQuestionIndex = index; // Update the current question index
    loadQuestion(currentQuestionIndex); // Load the selected question
    updateQuestionNav(); // Update the navigation buttons to reflect the current question
}

// Load a question dynamically based on the current index
function loadQuestion(index) {
    const question = questions[index]; // Get the current question object from the questions array
    questionText.textContent = `${index + 1}. ${question.questionText}`; // Display the question text in the question text element
    optionsContainer.innerHTML = ''; // Clear any previous answer options displayed

    // Loop through each option in the current question and create radio buttons
    question.options.forEach((option, i) => {
        const optionLabel = String.fromCharCode(65 + i); // Create option label (A, B, C, ...)
        const optionDiv = document.createElement('div'); // Create a new div for the option
        optionDiv.className = 'form-check'; // Assign the 'form-check' class for Bootstrap styling
        // Create the HTML for the radio input and label
        optionDiv.innerHTML = ` 
            <input class="form-check-input" type="radio" name="option" value="${option}" id="option${i}" ${answers[question.id] === option ? 'checked' : ''}> 
            <label class="form-check-label" for="option${i}">${optionLabel}. ${option}</label> 
        `;
        optionsContainer.appendChild(optionDiv); // Append the option div to the options container
    });

    // Add change event listeners to the radio buttons
    document.querySelectorAll('input[name="option"]').forEach(input => {
        input.addEventListener('change', function() {
            answers[question.id] = { answer: this.value, attended: true }; // Store the selected answer in the answers object
            updateQuestionNav();
        });
    });
}

// Update question navigation buttons dynamically based on the number of questions
function updateQuestionNav() {
    questionNav.innerHTML = ''; // Clear any previous navigation buttons
    questions.forEach((_, index) => { // Loop through each question
        const navButton = document.createElement('button'); // Create a new navigation button
        navButton.className = `nav-item ${index === currentQuestionIndex ? 'active' : ''}`; // Set active class if it's the current question
        // Check the state of the question
        if (answers[questions[index].id]?.attended) {
            navButton.classList.add('btn-custom-success'); // Change color if attended
        } else {
            navButton.classList.add('btn-custom-danger'); // Change color if not attended
        }
        navButton.textContent = index + 1; // Set button text to the question number
        navButton.onclick = () => navigateToQuestion(index); // Add click event to navigate to the selected question
        questionNav.appendChild(navButton); // Append the button to the navigation
    });
}

// Display the submission summary modal when ready to submit
function showSummaryModal() {
    const attendedQuestions = Object.keys(answers).filter(key => answers[key] !== null).length; // Count attended questions
    const notAttendedQuestions = questions.length - attendedQuestions; // Calculate unanswered questions

    // Display summary information in the modal
    document.getElementById('summary-text').innerHTML = ` 
        Total Questions: ${questions.length}<br> 
        Attended Questions: ${attendedQuestions}<br> 
        Not Attended Questions: ${notAttendedQuestions} 
    `;
    submissionModal.style.display = 'block'; // Show the submission modal
}

// Set the duration for the timer (e.g., 10 minutes)
let duration = 1 * 30; // Set duration to 10 minutes in seconds
let timerDisplay = document.getElementById('time-remaining'); // Get the timer display element

// Start the timer when the page loads
//  window.onload = startTimer; // Call startTimer when the window loads
function startTimer() {
    let timer = setInterval(function() { // Create a timer that runs every second
        let minutes = parseInt(duration / 60, 10); // Calculate the remaining minutes
        let seconds = parseInt(duration % 60, 10); // Calculate the remaining seconds

        // Format the time remaining for display
        seconds = seconds < 10 ? '0' + seconds : seconds; // Add leading zero if necessary
        timerDisplay.textContent = minutes + ":" + seconds; // Update the timer display with formatted time

        // If the timer reaches zero, submit answers automatically
        if (--duration < 0) {
            clearInterval(timer); // Stop the timer
            submitAnswers(); // Call the function to submit the answers
        }
    }, 1000); // Run the interval every 1000 milliseconds (1 second)
}

// Submit answers to the server
async function submitAnswers() {
    confirmSubmitBtn.disabled = true; // Disable the submit button to prevent multiple submissions
    loadingIndicator.style.display = 'block'; // Show the loading indicator during submission

    const moduleId = sessionStorage.getItem('selectedModuleId'); // Retrieve the selected module ID from session storage
    try {
        stopMediaStreams(); // Stop any media streams to release resources
        alert("Your answers have been submitted. Thank you!"); // Alert the user that answers are submitted
        window.location.href = 'logout.html';  // Redirect to logout page after submission

        // Send the answers to the server via a POST request
        const response = await fetch('/api/submit-answers', {
            method: 'POST', // Set the request method to POST
            headers: { 'Content-Type': 'application/json' }, // Set content type to JSON
            body: JSON.stringify({moduleId, answers }) // Send the module ID and answers as JSON
        });

        const data = await response.json(); // Parse the JSON response
        if (!data.success) { // Check for success in the response
            console.error('Error sending results:', data.message); // Log any error messages
        }

    } catch (error) {
        console.error('Error:', error); // Log any errors encountered during submission
    } finally {
        confirmSubmitBtn.disabled = false; // Re-enable the submit button
        loadingIndicator.style.display = 'none'; // Hide the loading indicator
    }
}

// Stop media streams after submitting to free up resources
function stopMediaStreams() {
    if (mediaStream) { // Check if mediaStream is defined
        mediaStream.getTracks().forEach(track => track.stop()); // Stop each track of the media stream
    }
}

// Event listener for when the DOM content is loaded
document.addEventListener('DOMContentLoaded', () => {
    const moduleId = sessionStorage.getItem('selectedModuleId'); // Get the selected module ID from session storage
    fetchQuestionsByModule(moduleId); // Fetch questions for the selected module

    // Event listener for the next button
    document.getElementById('next-btn').onclick = () => {
        if (currentQuestionIndex < questions.length - 1) {
            navigateToQuestion(currentQuestionIndex + 1); // Navigate to the next question
        }else {
            showSummaryModal();
        }
    };

    // Event listener for the previous button
    document.getElementById('prev-btn').onclick = () => {
        if (currentQuestionIndex > 0) {
            navigateToQuestion(currentQuestionIndex - 1); // Navigate to the previous question
        }
    };
    //
    // // Event listener for the submit button
    // document.getElementById('submit-btn').onclick = showSummaryModal; // Show summary modal on submit button click
    confirmSubmitBtn.addEventListener('click', async () => {
        await submitAnswers();
    });

    document.getElementById('modal-close').addEventListener('click', () => {
        submissionModal.style.display = 'none';
    });
});

