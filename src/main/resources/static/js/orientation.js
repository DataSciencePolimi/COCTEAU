/*
	Manages the orientation of the screen.
*/
window.addEventListener("orientationchange", function() {
    if(screen.orientation.type == "portrait-primary") {
    	$('html, body').animate({
			scrollTop: 0
		}, 1);
    }
});