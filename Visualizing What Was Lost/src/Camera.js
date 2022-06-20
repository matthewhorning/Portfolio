import { getStorage, ref, uploadString } from "firebase/storage";
import { CameraPreview } from '@capacitor-community/camera-preview';
import { v4 as uuidv4 } from 'uuid';

class Camera {

    startCamera() {
        const cameraButton = document.getElementById("cameraButton");
        cameraButton.style.display = "none";

        const cameraPreview = document.getElementById("cameraPreview");
        cameraPreview.style.display = "block";

        const popup = document.getElementById("popup")
        popup.style.height = "0";

        const map = document.getElementById("map");
        map.style.display = "none";


        const cameraPreviewOptions = {
            position: 'rear',
            parent: 'cameraPreview',
            class: 'cameraPreview',
            toBack: true,
        };
        CameraPreview.start(cameraPreviewOptions);
    }
    
    async stopCamera() {
        await CameraPreview.stop();

        const map = document.getElementById("map");
        map.style.display = "block";

        const cameraPreview = document.getElementById("cameraPreview");
        cameraPreview.style.display = "none";

        const cameraButton = document.getElementById("cameraButton");
        cameraButton.style.display = "block";

        const popup = document.getElementById("popup")
        popup.style.height = "50%";
    }
    
    async captureImage(index) {
        const cameraPreviewOptions = {
            quality: 90,
        };
    
        const result = await CameraPreview.capture(cameraPreviewOptions);

        const user_photo_div = document.getElementById("user_photo_row");
        var capturedImage = document.createElement('img');
        capturedImage.id = "thumb_image";
        capturedImage.alt = uuidv4();
        capturedImage.src = `data:image/jpeg;base64,${result.value}`;
        user_photo_div.appendChild(capturedImage);

        uploadCapturedImage(index, capturedImage);

        stopCamera();
    }
    
    flipCamera() {
        CameraPreview.flip();
    }
}

function uploadCapturedImage(index, img) {
    const storage = getStorage();
    const storageRef = ref(storage, `rowid${index}/${img.alt}`);

    uploadString(storageRef, img.src, 'data_url').then((snapshot) => {
        console.log('Uploaded a data_url string!');
    });
}

export const { startCamera, stopCamera, captureImage, flipCamera } = new Camera();