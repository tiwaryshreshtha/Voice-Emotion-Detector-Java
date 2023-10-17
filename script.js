document.addEventListener('DOMContentLoaded', function () {
    const audioFileInput = document.getElementById('audio-file');
    const analyzeButton = document.getElementById('analyze-button');
    const emotionResult = document.getElementById('emotion-result');
    const resultContainer = document.querySelector('.result-container');
    const uploadButton = document.querySelector('.upload-button');
    const uploadedFile = document.getElementById('uploaded-file');
    const fileInfoText = document.getElementById('file-info-text');

    function updateFileInfo(file) {
        const fileName = file.name;
        const fileSize = (file.size / (1024 * 1024)).toFixed(2); // Convert to MB and round to two decimal places
        fileInfoText.textContent = `${fileName} (${fileSize}MB)`;
        uploadedFile.style display = 'block';
    }

    audioFileInput.addEventListener('change', function (event) {
        const selectedFile = audioFileInput.files[0];
        if (selectedFile) {
            updateFileInfo(selectedFile);
        }
    });

    uploadButton.addEventListener('click', function () {
        audioFileInput.click();
    });

    analyzeButton.addEventListener('click', function () {
        const selectedFile = audioFileInput.files[0];

        if (!selectedFile) {
            alert('Please select an audio file.');
            return;
        }
        const formData = new FormData();
        formData.append('audioFile', selectedFile);
        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/analyze-audio', true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const detectedEmotion = xhr.responseText;
                emotionResult.textContent = detectedEmotion;
                resultContainer.style.display = 'block'; 
            }
        };
        xhr.send(formData);
    });
});
