let mediaStream;

// Check if the `run-diagnostic` button exists before adding an event listener
const runDiagnosticButton = document.getElementById('run-diagnostic');
if (runDiagnosticButton) {
    runDiagnosticButton.addEventListener('click', () => {
        startDiagnostics();
    });
}

// Check if the `continue` button exists before adding an event listener
const continueButton = document.getElementById('continue');
if (continueButton) {
    continueButton.addEventListener('click', () => {
        window.location.href = 'instruction.html';
    });
}

function startDiagnostics() {
    startCameraFeed()
        .then(() => checkAudioPermission())
        .then(() => checkNetworkStability())
        .then(() => checkAllPermissions())
        .catch(err => {
            console.error('Error during diagnostics:', err);
            checkAllPermissions();  // Even if an error occurs, update the button status
        });
}

function startCameraFeed() {
    return navigator.mediaDevices.getUserMedia({ video: true })
        .then(stream => {
            mediaStream = stream;
            attachStreamToVideo(mediaStream);

            // Only update status if the element exists on the page
            const cameraStatusElement = document.getElementById('camera-status');
            if (cameraStatusElement) {
                updateStatus(cameraStatusElement, 'Accepted');
            }
        })
        .catch(error => {
            const cameraStatusElement = document.getElementById('camera-status');
            if (cameraStatusElement) {
                updateStatus(cameraStatusElement, 'Denied');
            }
        });
}

function checkAudioPermission() {
    return navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
            const audioStatusElement = document.getElementById('audio-status');
            if (audioStatusElement) {
                updateStatus(audioStatusElement, 'Accepted');
            }
            stream.getTracks().forEach(track => track.stop()); // Stop microphone if not needed
        })
        .catch(error => {
            const audioStatusElement = document.getElementById('audio-status');
            if (audioStatusElement) {
                updateStatus(audioStatusElement, 'Denied');
            }
        });
}

function checkNetworkStability() {
    return new Promise((resolve, reject) => {
        const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;
        if (connection) {
            const speed = connection.downlink;
            const networkStatusElement = document.getElementById('network-status');
            if (networkStatusElement) {
                if (speed >= 1.5) {
                    updateStatus(networkStatusElement, 'Accepted');
                } else {
                    updateStatus(networkStatusElement, 'Weak');
                }
            }
            resolve();
        } else {
            const networkStatusElement = document.getElementById('network-status');
            if (networkStatusElement) {
                updateStatus(networkStatusElement, 'Unknown');
            }
            resolve();
        }
    });
}

function attachStreamToVideo(stream) {
    const videoElement = document.getElementById('camera-feed');
    if (videoElement) {
        videoElement.srcObject = stream;
    }
}

function updateStatus(element, status) {
    const statusTextElement = element.querySelector('.status-text');
    if (statusTextElement) {
        statusTextElement.textContent = status;
        if (status === 'Accepted') {
            element.style.color = 'green';
        } else {
            element.style.color = 'red';
        }
    }
}

function checkAllPermissions() {
    const cameraStatusElement = document.getElementById('camera-status');
    const audioStatusElement = document.getElementById('audio-status');
    const networkStatusElement = document.getElementById('network-status');

    const cameraAccepted = cameraStatusElement && cameraStatusElement.querySelector('.status-text').textContent === 'Accepted';
    const audioAccepted = audioStatusElement && audioStatusElement.querySelector('.status-text').textContent === 'Accepted';
    const networkAccepted = networkStatusElement && networkStatusElement.querySelector('.status-text').textContent === 'Accepted';

    if (cameraAccepted && audioAccepted && networkAccepted) {
        const continueButton = document.getElementById('continue');
        if (continueButton) {
            continueButton.disabled = false;
        }
    }
}

window.addEventListener('load', () => {
    // If already on the page, initialize the camera feed
    if (!mediaStream) {
        startCameraFeed();
    }
});

window.addEventListener('beforeunload', () => {
    if (mediaStream) {
        mediaStream.getTracks().forEach(track => track.stop());
    }
});