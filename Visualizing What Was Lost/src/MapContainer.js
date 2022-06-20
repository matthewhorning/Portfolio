import React, { useRef, useEffect } from 'react';
import { onValue } from "firebase/database";
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css' //Must have this otherwise pins don't anchor
import firebase from './query/connect_to_firebase.js';
import { createNameDate, createThumbImage, getHistoricalImage, makeImageFullscreen, retrieveCapturedImage } from './Sidebar';
import { setPinLatLong, setUserLatLong } from './Location';
import { setIndex } from './CameraScreen';

mapboxgl.accessToken = 'pk.eyJ1Ijoibmlja2ZhZGRpcyIsImEiOiJja3Vwemt0amwwbnVoMnZwZ2dreW81OTQwIn0.qerYpAFHJUlYynqbympvAA';

const MapContainer = () => {
    const mapContainer = useRef(null);
   
    useEffect(() => {
        const map = new mapboxgl.Map({
            container: mapContainer.current,
            style: 'mapbox://styles/nickfaddis/ckurogok00w1l18mmby6l9zsf',
            center: [-90.215916, 38.629888],
            zoom: 12.5
        });

        map.on('load', () => {
            map.addSource('mvc_border', {
                'type': 'vector',
                'url': 'mapbox://nickfaddis.cl2p9p65v003320tk7nljmzxl-0ljp3'
            });

            // Add a new layer to visualize the polygon.
            map.addLayer({
                'id': 'border',
                'type': 'fill',
                'source': 'mvc_border', // reference the data source
                'source-layer':'Mill_Creek_Valley_Outline',
                'layout': {},
                'paint': {
                    'fill-color': '#0080ff', // blue color fill
                    'fill-opacity': 0.3
                }
            });
            // Add a black outline around the polygon.
            map.addLayer({
                'id': 'outline',
                'type': 'line',
                'source': 'mvc_border',
                'source-layer':'Mill_Creek_Valley_Outline',
                'layout': {},
                'paint': {
                    'line-color': '#000',
                    'line-width': 3
                }
            });
            map.setLayoutProperty('border', 'visibility', 'visible');
            map.setLayoutProperty('outline', 'visibility', 'visible');
        });

        map.addControl(new mapboxgl.NavigationControl(), 'top-right');

        let geolocate = new mapboxgl.GeolocateControl({
            positionOptions: {
                enableHighAccuracy: true
            },
            trackUserLocation: true,
            showUserHeading: true
        });
        map.addControl(geolocate);

        geolocate.on('geolocate', function (position) {
            const userLat = position.coords.latitude;
            const userLong = position.coords.longitude;
            console.log('lat, lng', userLat, userLong);
            
            setUserLatLong(userLat, userLong);
        });

        map.on('idle', () => {
            if (!map.getLayer('border') || !map.getLayer('outline')) {
                return;
            }

            // Enumerate ids and urls of the layers.
            const toggleableLayerIds = new Map([
                ['border', 'https://cdn-icons-png.flaticon.com/512/3199/3199097.png'],
                ['outline', 'http://cdn.onlinewebfonts.com/svg/img_406792.png'],
            ]);
            
            for (const [id, url] of toggleableLayerIds) {
                // Skip layers that already have a button set up.
                if (document.getElementById(id)) {
                    continue;
                }

                // Create a link.
                const link = document.createElement('a');
                link.id = id;
                link.href = '#';
                link.className = 'active';

                const icon = document.createElement('img');
                icon.src = url;
                link.appendChild(icon);
                
                // Show or hide layer when the toggle is clicked.
                link.onclick = function (e) {
                    const clickedLayer = id;
                    e.preventDefault();
                    e.stopPropagation();
                    
                    const visibility = map.getLayoutProperty(clickedLayer, 'visibility');

                    console.log(visibility);
                    
                    // Toggle layer visibility by changing the layout object's visibility property.
                    if (visibility === 'visible') {
                        map.setLayoutProperty(clickedLayer, 'visibility', 'none');
                        this.className = '';
                    } else {
                        this.className = 'active';
                        map.setLayoutProperty(clickedLayer, 'visibility', 'visible');
                    }
                };
                
                const layers = document.getElementById('toggle');
                layers.appendChild(link);
            }
        });
    
        onValue(firebase, (snapshot) => {
            const data = snapshot.val();
            const keys = Object.keys(data).map(function(key){ return {key, ...data[key]} });
            
            keys.forEach(element => {
                const marker = new mapboxgl.Marker();
                var long = element.lng;
                var lat = element.lat;
                marker.setLngLat([long,lat]);
                marker.addTo(map);

                marker.getElement().addEventListener('click', () => {
                    setIndex(element.key);
                    retrieveCapturedImage(element.key);
                    setPinLatLong(lat, long);
                    
                    //Variables for sidebar divs
                    var location_info_div = document.getElementById('location_info_row');
                    var legacy_photo_div = document.getElementById('legacy_photo_row');
                    var user_photo_div = document.getElementById('user_photo_row');

                    //Make popup visible by modifying height
                    document.getElementById("popup").style.height = "50%";

                    //Clear each div of past elements
                    while(location_info_div.childElementCount > 0) {
                        location_info_div.removeChild(location_info_div.lastChild);
                    }
                    while(legacy_photo_div.childElementCount > 0) {
                        legacy_photo_div.removeChild(legacy_photo_div.lastChild);
                    }
                    while(user_photo_div.childElementCount > 0) {
                        user_photo_div.removeChild(user_photo_div.lastChild);
                    }

                    createNameDate(element);
                    createThumbImage(element.image_url);
                    getHistoricalImage(element.image_url);
                })

                makeImageFullscreen(element.image_url);
            })
        });

        //TODO: Create radio button to change between labeled and non-labeled styles
        //map.setStyle('mapbox://styles/mapbox/outdoors-v11');
    }, []);

    return <div className="map-container" style={{height: '100vh'}} ref={ mapContainer } />;
}

export default MapContainer;
