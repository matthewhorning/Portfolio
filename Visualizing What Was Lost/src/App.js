import './App.css';
import MapContainer from "./MapContainer.js";
import { Sidebar } from './Sidebar.js';
import SliderDemo from './SliderDemo.js';
import { CameraScreen } from './CameraScreen.js';

function App() {
  return (
    <div className="App" id = "App">
      <div id="map">
        <SliderDemo></SliderDemo>
        <MapContainer></MapContainer>
        <Sidebar></Sidebar>
        <div id="toggle"></div>
      </div>
      <CameraScreen></CameraScreen>
    </div>
  );
}

export default App;
