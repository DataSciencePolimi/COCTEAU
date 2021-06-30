/*
	Manages the tutorial modal of the Match. If the page is opened for the first time, the tutorial is displayed.
*/
$(window).on('load', function(){
	var firstTime = $('#firstTime').val();
	if(firstTime == 0){
		$('#firstTimeInfoModal').modal('toggle');
	}
});

/*
	Detect the button pressed to change page.
*/
$(document).on('click', '#in-depth-button', function(){
	 $('#button-pressed').val("in-depth");
	 $('#change-page-button').submit();
})

/*
	Detect the button pressed to change page.
*/
$(document).on('click', '#end-button', function(){
	$('#button-pressed').val("end");
	$('#change-page-button').submit();
})

/*
	Detect the button pressed to change page.
*/
$(document).on('click', '#end-button-pre', function(){
	$('#button-pressed').val("end");
	$('#change-page-button').submit();
})

/*
	Detect the button pressed to change page.
*/
$(document).on('click', '#again-button', function(){
	$('#button-pressed').val("again");
	$('#change-page-button').submit();
})

/*
	Submit the form with the information of the match.
*/
$("#matchForm").submit(function(event) {
	event.preventDefault();
	$.post($(this).attr('action'), $(this).serialize());
});

/*
	Display the Vision of the challenger and computes the scores and the update of the position.
*/
function showChallengerVision(){
	
	$('#challengerInfo').prop('disabled', true);
	// Disable guess controls
	$('#firstDimGuess').val($('#firstDim_g').val());
	$('#secondDimGuess').val($('#secondDim_g').val());
	
	var points = 0;
	var firstGuess = $('#firstDim_g').val();
	var secondGuess = $('#secondDim_g').val();
	
	var firstChallenger = $('#firstDimChallenger').val();
    var secondChallenger = $('#secondDimChallenger').val();
	
	$('#end-button-pre').hide();
	
	var firstDelta = Math.abs(firstChallenger - firstGuess);
	var secondDelta = Math.abs(secondChallenger - secondGuess);
	
	if(firstDelta == 1) {
		points += 5;
	} else if(firstDelta == 0) {
		points += 10;
	}
	
	if(secondDelta == 1) {
		points += 5;
	} else if(secondDelta == 0) {
		points += 10;
	}
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({ type: "GET",
	     url: "/updated-position",
	     data: {"points" : points},
	     success : function(data)
	     {
	    	 results = JSON.parse(data);
	    	 
	    	 deltaPos = results.deltaPos;
	    	 position = results.position;
	    	 totPoints = results.totPoints;
	    	 
	    	 if(typeof Cookies.get("errorMsg") !== 'undefined'){
		    		$(location).attr('href','/');
		     } else {	    		
	    		var signed_deltaPos = deltaPos;
	    		var pos_color = "green";
	    		
	    		if(deltaPos <= 0) {
	    			signed_deltaPos = "+" + (-deltaPos);
	    			pos_color = "green"
	    		} else {
	    			signed_deltaPos = "-" + deltaPos;
	    			pos_color = "red"
	    		}
	    		
				var html_lead = '<div class="row">'
					+ '<div class="col-12 col-sm-12 col-md-6 d-flex justify-content-center align-items-center my-2">'
					+ '<span class="mr-1 font-weight-bold h2">' + totPoints + '</span>'
					+ '<span class="mr-1 font-weight-bold h2" style="color: green;">(+' + points + ')</span>'
					+ '<i class="fas fa-trophy fa-3x ml-1 mr-3" style="color: #ffcc00;"></i>'
					+ '</div>'
					+ '<div class="col-12 col-sm-12 col-md-6 d-flex justify-content-center align-items-center my-2">'
					+ '<span class="mr-1 font-weight-bold h2">' + position + '</span>'
					+ '<span class="mr-1 font-weight-bold h2" style="color: ' + pos_color + ';">(' + signed_deltaPos + ')</span>'
					+ '<i class="fas fa-3x fa-list-ol ml-2" style="color: ' + pos_color + ';"></i>'
					+ '</div>'
					+ '</div>';
	    		 
	    		 $("#pointsEarned").append(html_lead);
	    		 
		    	 $("#matchForm").submit();
		    	 
		    	// Show challenger vision
	    		$('#challengerVision').css("display", "block");
	    		$('#challengerVisionButtons').css("display", "block");
	    		
	    		$('#toBeHidden').css("display", "none");
	    		
	    		$('html, body').animate({
	    			scrollTop: $('#in-depth-button').offset().top
	    		}, 'slow');
		     }
	     }
	})
	
	$('#firstDim_g').attr("disabled", true);
	$('#secondDim_g').attr("disabled", true);
	
	// Disable toggles events
	$('#label_first_minus').off("click");
	$('#label_first_plus').off("click");
	$('#label_second_minus').off("click");
	$('#label_second_plus').off("click");
	$('#label_first_minus').hide();
	$('#label_first_plus').hide();
	$('#label_second_minus').hide();
	$('#label_second_plus').hide();
}

// Slider toggles
var step = 1.0
var minSlider = 1.0
var maxSlider = 5.0

/*
	Manage the click on the first minus button.
*/
$('#label_first_minus').on('click', function(){
	temp = parseInt($('#firstDim_g').val());
	if(temp - step >= minSlider){
		$('#firstDim_g').val(temp - step);	
	} else {
		$('#firstDim_g').val(minSlider);
	}
});

/*
	Manage the click on the first plus button.
*/
$('#label_first_plus').on('click', function(){
	temp = parseInt($('#firstDim_g').val());
	if(temp + step > maxSlider){
		$('#firstDim_g').val(maxSlider);
	} else {
		$('#firstDim_g').val(temp + step);
	}
	
});

/*
	Manage the click on the second minus button.
*/
$('#label_second_minus').on('click', function(){
	temp = parseInt($('#secondDim_g').val());
	if(temp - step >= minSlider){
		$('#secondDim_g').val(temp - step);	
	} else {
		$('#secondDim_g').val(minSlider);
	}
});

/*
	Manage the click on the second plus button.
*/
$('#label_second_plus').on('click', function(){
	temp = parseInt($('#secondDim_g').val());
	if(temp + step > maxSlider){
		$('#secondDim_g').val(maxSlider);
	} else {
		$('#secondDim_g').val(temp + step);
	}
	
});

/*
	Manage the toggle description of the scenario.
*/
$('#scenarioDesc').on('hidden.bs.collapse', function(){
	$('.chevron-scenario').html('<i class="fas fa-chevron-right" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the scenario.
*/
$('#scenarioDesc').on('shown.bs.collapse', function(){
	$('.chevron-scenario').html('<i class="fas fa-chevron-down" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the first dimension.
*/
$('#sliderDesc').on('hidden.bs.collapse', function(){
	$('.chevron-sliders').html('<i class="fas fa-chevron-right" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the second dimension.
*/
$('#sliderDesc').on('shown.bs.collapse', function(){
	$('.chevron-sliders').html('<i class="fas fa-chevron-down" aria-hidden="true"></i>');
});