let questions = [];
let currentQuestionIndex = 0;
let answers = {};
const questionNav = document.getElementById('question-nav');
const questionText = document.getElementById('question-text');
const optionsContainer = document.getElementById('options-container');
const submissionModal = document.getElementById('submissionModal');
const confirmSubmitBtn = document.getElementById('confirm-submit-btn');
const loadingIndicator = document.createElement('p');
loadingIndicator.textContent = "Submitting... Please wait.";
loadingIndicator.style.display = 'none';
submissionModal.querySelector('.modal-body').appendChild(loadingIndicator);

// Start the camera feed
startCameraFeed()
    .then(() => console.log('Camera feed started successfully'))
    .catch(err => console.error('Error starting camera feed:', err));

async function fetchQuestions() {
    try {
        const response = await fetch('/api/questions');
        questions = await response.json();
        updateQuestionNav();
        loadQuestion(currentQuestionIndex);
    } catch (error) {
        console.error('Error fetching questions:', error);
    }
}

function navigateToQuestion(index) {
    currentQuestionIndex = index;
    loadQuestion(currentQuestionIndex);
    updateQuestionNav();
}

function loadQuestion(index) {
    const question = questions[index];
    questionText.textContent = `${index + 1}. ${question.questionText}`;
    optionsContainer.innerHTML = '';

    question.options.forEach((option, i) => {
        const optionLabel = String.fromCharCode(65 + i);
        const optionDiv = document.createElement('div');
        optionDiv.className = 'form-check';
        optionDiv.innerHTML = `
            <input class="form-check-input" type="radio" name="option" value="${option}" id="option${i}" ${answers[question.id] === option ? 'checked' : ''}>
            <label class="form-check-label" for="option${i}">${optionLabel}. ${option}</label>
        `;
        optionsContainer.appendChild(optionDiv);
    });

    document.querySelectorAll('input[name="option"]').forEach(input => {
        input.addEventListener('change', function() {
            answers[question.id] = this.value;
        });
    });
}

function updateQuestionNav() {
    questionNav.innerHTML = '';
    questions.forEach((_, index) => {
        const navButton = document.createElement('button');
        navButton.className = `nav-item ${index === currentQuestionIndex ? 'active' : ''}`;
        navButton.textContent = index + 1;
        navButton.onclick = () => navigateToQuestion(index);
        questionNav.appendChild(navButton);
    });
}

function showSummaryModal() {
    const attendedQuestions = Object.keys(answers).filter(key => answers[key] !== null).length;
    const notAttendedQuestions = questions.length - attendedQuestions;

    document.getElementById('summary-text').innerHTML = `
        Total Questions: ${questions.length}<br>
        Attended Questions: ${attendedQuestions}<br>
        Not Attended Questions: ${notAttendedQuestions}
    `;
    submissionModal.style.display = 'block';
}

async function submitAnswers() {
    // Disable the submit button and show loading indicator
    confirmSubmitBtn.disabled = true;
    loadingIndicator.style.display = 'block';

    try {
        alert("Your answers have been submitted. Thank you!");
        stopMediaStreams();
        window.location.href = 'logout.html';

        const response = await fetch('/api/submit-answers', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ answers })
        });

        const data = await response.json();
        if (!data.success) {
            console.error('Error sending results:', data.message);
        }

    } catch (error) {
        console.error('Error:', error);
    } finally {
        // Re-enable the submit button and hide loading indicator
        confirmSubmitBtn.disabled = false;
        loadingIndicator.style.display = 'none';
    }
}

function stopMediaStreams() {
    if (mediaStream) {
        mediaStream.getTracks().forEach(track => track.stop());
    }
}

document.addEventListener('DOMContentLoaded', () => {
    fetchQuestions();

    document.getElementById('next-btn').addEventListener('click', () => {
        if (currentQuestionIndex < questions.length - 1) {
            navigateToQuestion(currentQuestionIndex + 1);
        } else {
            showSummaryModal();
        }
    });

    document.getElementById('prev-btn').addEventListener('click', () => {
        if (currentQuestionIndex > 0) {
            navigateToQuestion(currentQuestionIndex - 1);
        }
    });

    confirmSubmitBtn.addEventListener('click', async () => {
        await submitAnswers();
    });

    document.getElementById('modal-close').addEventListener('click', () => {
        submissionModal.style.display = 'none';
    });
});
