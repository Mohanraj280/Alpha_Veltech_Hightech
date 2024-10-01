let mediaStream; // Variable to hold the media stream from the camera

// Check if the `run-diagnostic` button exists before adding an event listener
const runDiagnosticButton = document.getElementById('run-diagnostic');
if (runDiagnosticButton) {
    runDiagnosticButton.addEventListener('click', () => {
        startDiagnostics(); // Start diagnostics when the button is clicked
    });
}

// Check if the `continue` button exists before adding an event listener
const continueButton = document.getElementById('continue');
if (continueButton) {
    continueButton.addEventListener('click', () => {
        window.location.href = 'instruction.html'; // Redirect to the instructions page
    });
}

// Function to start the diagnostic checks
function startDiagnostics() {
    startCameraFeed() // Start camera feed
        .then(() => checkAudioPermission()) // Check audio permission after camera feed starts
        .then(() => checkNetworkStability()) // Check network stability after audio permission check
        .then(() => checkAllPermissions()) // Finally, check if all permissions are accepted
       .catch(err => {
            console.error('Error during diagnostics:', err); // Log any errors that occur
            checkAllPermissions(); // Update permission statuses even if an error occurs
        });
}

// Function to start the camera feed and attach it to a video element
function startCameraFeed() {
    return navigator.mediaDevices.getUserMedia({ video: true }) // Request access to the camera
        .then(stream => {
            mediaStream = stream; // Store the stream in the global variable
            attachStreamToVideo(mediaStream); // Attach the stream to the video element

            // Update the camera status on the UI if the element exists
            const cameraStatusElement = document.getElementById('camera-status');
            if (cameraStatusElement) {
                updateStatus(cameraStatusElement, 'Accepted'); // Set status to accepted
            }
        })
        .catch(error => {
            // If access to the camera is denied
            const cameraStatusElement = document.getElementById('camera-status');
            if (cameraStatusElement) {
                updateStatus(cameraStatusElement, 'Denied'); // Set status to denied
            }
        });
}

// Function to check if audio permissions are granted
function checkAudioPermission() {
    return navigator.mediaDevices.getUserMedia({ audio: true }) // Request access to the microphone
        .then(stream => {
            const audioStatusElement = document.getElementById('audio-status');
            if (audioStatusElement) {
                updateStatus(audioStatusElement, 'Accepted'); // Set status to accepted
            }
            stream.getTracks().forEach(track => track.stop()); // Stop microphone if not needed
        })
        .catch(error => {
            const audioStatusElement = document.getElementById('audio-status');
            if (audioStatusElement) {
                updateStatus(audioStatusElement, 'Denied'); // Set status to denied
            }
        });
}

// Function to check network stability
function checkNetworkStability() {
    return new Promise((resolve, reject) => {
        const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection; // Get connection information
        if (connection) {
            const speed = connection.downlink; // Get downlink speed
            const networkStatusElement = document.getElementById('network-status');
            if (networkStatusElement) {
                // Check the speed and update the status accordingly
                if (speed >= 1.5) {
                    updateStatus(networkStatusElement, 'Accepted'); // Speed is good
                } else {
                    updateStatus(networkStatusElement, 'Weak'); // Speed is weak
                }
            }
            resolve(); // Resolve the promise
        } else {
            // If connection info is not available
            const networkStatusElement = document.getElementById('network-status');
            if (networkStatusElement) {
                updateStatus(networkStatusElement, 'Unknown'); // Set status to unknown
            }
            resolve(); // Resolve the promise
        }
    });
}

// Function to attach the media stream to the video element
function attachStreamToVideo(stream) {
    const videoElement = document.getElementById('camera-feed');
    if (videoElement) {
        videoElement.srcObject = stream; // Set the video source to the stream
    }
}

// Function to update the status of permissions in the UI
function updateStatus(element, status) {
    const statusTextElement = element.querySelector('.status-text'); // Select the status text element
    if (statusTextElement) {
        statusTextElement.textContent = status; // Update the text content to the status
        // Change the color based on the status
        if (status === 'Accepted') {
            element.style.color = 'green'; // Accepted status is green
        } else {
            element.style.color = 'red'; // Denied or other statuses are red
        }
    }
}

// Function to check if all permissions are accepted
function checkAllPermissions() {
    const cameraStatusElement = document.getElementById('camera-status');
    const audioStatusElement = document.getElementById('audio-status');
    const networkStatusElement = document.getElementById('network-status');

    // Check the status of each permission
    const cameraAccepted = cameraStatusElement && cameraStatusElement.querySelector('.status-text').textContent === 'Accepted';
    const audioAccepted = audioStatusElement && audioStatusElement.querySelector('.status-text').textContent === 'Accepted';
    const networkAccepted = networkStatusElement && networkStatusElement.querySelector('.status-text').textContent === 'Accepted';

    // Enable the continue button if all permissions are accepted
    if (cameraAccepted && audioAccepted && networkAccepted) {
        const continueButton = document.getElementById('continue');
        if (continueButton) {
            continueButton.disabled = false; // Enable the continue button
        }
    }
}

// Initialize the camera feed when the window loads
window.addEventListener('load', () => {
    // If no media stream is already active, start the camera feed
    if (!mediaStream) {
        startCameraFeed();
    }
});

// Clean up the media stream when the window is about to be unloaded
window.addEventListener('beforeunload', () => {
    if (mediaStream) {
        mediaStream.getTracks().forEach(track => track.stop()); // Stop all tracks to release camera and microphone
    }
});
function enterFullscreen() {
    const elem = document.documentElement; // Use the entire document
    if (elem.requestFullscreen) {
        elem.requestFullscreen();
    } else if (elem.mozRequestFullScreen) { // Firefox
        elem.mozRequestFullScreen();
    } else if (elem.webkitRequestFullscreen) { // Chrome, Safari, and Opera
        elem.webkitRequestFullscreen();
    } else if (elem.msRequestFullscreen) { // IE/Edge
        elem.msRequestFullscreen();
    }
}

// Show the fullscreen modal on page load
window.onload = function() {
    document.getElementById('fullscreenModal').style.display = 'block';
};

// Add event listener to the button in the modal
document.getElementById('enter-fullscreen-btn').addEventListener('click', function() {
    enterFullscreen();
    startTimer();
    document.getElementById('fullscreenModal').style.display = 'none';

});

// Close modal when the close button is clicked
document.getElementById('modal-close').addEventListener('click', function() {
    document.getElementById('fullscreenModal').style.display = 'none';
});