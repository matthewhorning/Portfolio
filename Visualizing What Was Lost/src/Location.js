//File for various location based utility functions
import { startCamera } from './Camera';

var [latitude, longitude] = [0, 0];
var [userLatitude, userLongitude] = [0, 0];
var distance = 100 //Meters

//Set and get pin long/lat
function setPinLatLong(lat, long) {
    latitude = lat;
    longitude = long;
}

function getPinLatLong() {
    return [latitude, longitude]
}

//Set and get user long/lat
function setUserLatLong(lat, long) {
    userLatitude = lat;
    userLongitude = long;
}

function getUserLatLong() {
    return [userLatitude, userLongitude]
}

//Check user's location before allowing 
function locationChecker() {
    const pinCoords = getPinLatLong();
    const pinLat = pinCoords[0];
    const pinLong = pinCoords[1];

    const userCoords = getUserLatLong();
    const userLat = userCoords[0];
    const userLong = userCoords[1];

    console.log(`pin lat: ${pinLat}, pin long: ${pinLong}`)
    console.log(`user lat: ${userLat}, user long: ${userLong}`)

    if(calculateDistance(userLat, userLong, pinLat, pinLong) > distance) {
        alert("You must be closer to location to take a picture.");
    } else {
        startCamera();
    }
}

//Utility function to calculate distance
function calculateDistance(lat1, long1, lat2, long2) {
    var R = 6371000; // Radius of the earth in meters
    var dLat = deg2rad(lat2-lat1);
    var dLong = deg2rad(long2-long1); 
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLong/2) * Math.sin(dLong/2); 
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    var d = R * c; // Distance in meters

    console.log(`distance: ${d}`);
    return d;
}

function deg2rad(deg) {
    return deg * (Math.PI/180)
}

export { setPinLatLong, setUserLatLong, locationChecker };