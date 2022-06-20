/* eslint-disable jsx-a11y/anchor-is-valid */
/* eslint-disable no-script-url */
import './Sidebar.css';
import { getStorage, ref, listAll, getDownloadURL } from "firebase/storage";
import { locationChecker } from './Location';

function Sidebar() {
    return(
        <div className='ion-content'>
            <div id="popup" className="sidebar">
                <div id = "controls_row" className= "vertical_div">
                    <a href="#" className="closebtn" onClick={() => {
                        document.getElementById("popup").style.height = "0";
                    }}>×</a>
                    
                </div>
                <div id = "location_info_row" className = "vertical_div">

                </div>
                <p className = "headertext">Legacy Photos</p>
                <div id = "legacy_photo_row" className = "vertical_div">
                    
                </div>
                <p className = "headertext">User Photos</p>
                <div id = "user_photo_row" className = "vertical_div">

                </div>
            
                <button id="cameraButton" className="cameraButton" onClick={() => {locationChecker()}}>Camera</button>
            </div>
        </div>
    );
}

//Function for creating
function createNameDate(element) {
    var location_info_div = document.getElementById('location_info_row');

    var name_par = document.createElement('h1');
    var name = document.createTextNode(element.Name);
    name_par.id = 'location_name'
    name_par.appendChild(name);
    location_info_div.appendChild(name_par);
    if (element.date !== "NA") {
        var date_par = document.createElement('p');
        var date = document.createTextNode(element.date);
        date_par.id = 'location_date'
        date_par.appendChild(date);
        location_info_div.appendChild(date_par);
    }
}

/*Function for creating a thumbnail image*/
function createThumbImage(url) {
    var legacy_photo_div = document.getElementById('legacy_photo_row');

    var thumb_image = document.createElement('img');
    thumb_image.id = "thumb_image";
    thumb_image.src = url;
    console.log(url);
    legacy_photo_div.appendChild(thumb_image);
}

function getHistoricalImage(image) {
    const historicalImage = document.getElementById("historicalImage");
    historicalImage.src = image;
}

function makeImageFullscreen(url) {
    /* 
        Event listener for allowing users to make image thumbnail full screen by tapping thumbnail or larger image. Creates
        initial full screen image and flips visibility value when tapping either the thumbnail image or the full screen image.
    */
   
    document.addEventListener('click', function(e){
        var target = e.target;
        //Only
        if ((target.id === "thumb_image" || target.id === "large_image") && url !== "NA") {
            if (document.getElementById("large_image")) {
                //The image already exists, so simply flip its visiblity around
                var full_image = document.getElementById('large_image');

                if (full_image.style.visibility === 'hidden') {
                    console.log("showing");
                    full_image.style.visibility = 'visible';
                    full_image.src = target.src;
                } else {
                    console.log("hiding");
                    full_image.style.visibility = 'hidden';
                }
            } else {
                //The image doesn't exist, so we have to make it
                var full_img = document.createElement('img');
                full_img.id = "large_image";
                full_img.src = target.src;
                
                //Add to 'App' so it appears on top
                document.getElementById("App").appendChild(full_img);
            }
        }
    }, false)
}

function retrieveCapturedImage(index) {
    const user_photo_div = document.getElementById("user_photo_row");
    const storage = getStorage();
    const storageRef = ref(storage, `rowid${index}/`);
    listAll(storageRef).then((res) => {
        res.items.forEach((imageRef) => {
            getDownloadURL(imageRef).then((url) => {
                var retrievedImage = document.createElement('img');
                retrievedImage.id = "thumb_image";
                retrievedImage.src = url;
                user_photo_div.appendChild(retrievedImage);
            })
        })
    });
}

export { Sidebar, createNameDate, createThumbImage, getHistoricalImage, makeImageFullscreen, retrieveCapturedImage };