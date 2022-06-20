import './SliderDemo.css';
import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react/swiper-react';
import 'swiper/swiper.min.css';
import 'swiper/modules/pagination/pagination.min.css';

import pin_highlight from './img/pin_highlight.png';
import camera_highlight from './img/camera_highlight.png';
import location_highlight from './img/location_highlight.jpg';

/*Utility function to disable the swiper object*/
function hide_swiper(swiper_obj) {
	console.log("Hiding swiper!");
	swiper_obj.el.style.visibility = "hidden";
	swiper_obj.slideTo(0);
	console.log(swiper_obj.el.id);
	//swiper_obj.disable(); //Disabling the swiper causes issues

	//Show the button
	var button = document.getElementById("info_button");
	if (button != null) {
		console.log("Elem found");
		button.style.visibility = "visible";
	}
}

/*Utility function to show the swiper object*/
function show_swiper() {
	//Issue: attempt to pass a swiper object (and so access swiper methods) does not work
	//Temp workaround: get the cards object via ID, if found, set visible
	var elem = document.getElementById("test_id")
	if (elem != null) {
		console.log("Elem found");
		elem.style.visibility = "visible";
		
		//Hide the button
		var button = document.getElementById("info_button");
		if (button != null) {
			console.log("Elem found");
			button.style.visibility = "hidden";
		}
	}
}

/*React component for creating a Slider class object to display app information*/
export default function InfoCards() {
	return (
		<>
			<button 
				className = "close_button"
				id = "info_button"
				onClick={(swiper) => show_swiper(swiper)}
			>
				Map Info
			</button>
			<Swiper 
				className="mySwiper"
				id = "test_id"
				style = {{position: 'absolute'}}
				onReachEnd={(swiper) => hide_swiper(swiper)}
			>
				<SwiperSlide>
					<p><em>Visualizing What Was Lost</em> highlights the urban-renewal
						that occurred in the primarily African American St. Louis neighborhood, Mill Creek Valley in the 1950s.
						In pursuit of economic development goals, leadership at the time decided to demolish buildings present in the
						neighborhood for new development, forcibly displacing over 20,000 residents.
						Currently, Saint Louis University and Harris-Stowe University occupy much of the area.
						</p>
				</SwiperSlide>
				<SwiperSlide>
					<img id="pin-highlight" src={pin_highlight} alt=''></img>
					<p>Tap on a pin to pull up information about the location.</p>
				</SwiperSlide>
				<SwiperSlide>
					<img id="camera-highlight" src={camera_highlight} alt=''></img>
					<p>When you are within 100 meters of a pin, tap the Camera button to open the Camera and take a picture of the location. Compare and contrast what was there with what is there today.</p>
				</SwiperSlide>
				<SwiperSlide>
					<img id="location-highlight" src={location_highlight} alt=''></img>
					<p>Use the location button to highlight your current location.</p>
				</SwiperSlide>
				<SwiperSlide>
					<p>
						That's it! Swipe to close!
					</p>
					<p className = "hint_text">Remember you can always pull this up from the button in the top left of the screen!</p>
				</SwiperSlide>
				<SwiperSlide>
					Nothing to see here
				</SwiperSlide>
			</Swiper>
		</>
	);
}

