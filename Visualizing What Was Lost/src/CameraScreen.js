import './CameraScreen.css';
import { IonButton, IonIcon } from '@ionic/react';
import { closeCircleOutline, cameraOutline, cameraReverseOutline } from "ionicons/icons";
import { stopCamera, captureImage, flipCamera } from './Camera';

var index = 0;

function CameraScreen() {
    return (
        <div id="cameraPreview" className="cameraPreview" style={{display: 'none'}}>
            <img id="historicalImage" className="image-overlay" alt=""></img>
            <IonButton id="close" className="button" onClick={() => {stopCamera()}}>
                <IonIcon icon={closeCircleOutline} slot="icon-only"></IonIcon>
            </IonButton>
            <IonButton id="capture" className="button" onClick={() => {captureImage(getIndex())}}>
                <IonIcon icon={cameraOutline} slot="icon-only"></IonIcon>
            </IonButton>
            <IonButton id="flip" className="button" onClick={() => {flipCamera()}}>
                <IonIcon icon={cameraReverseOutline} slot="icon-only"></IonIcon>
            </IonButton>
        </div>
    );
}

function setIndex(i) {
    index = i;
}

function getIndex() {
    return index;
}

export { CameraScreen, setIndex };