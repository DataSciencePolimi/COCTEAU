/*
	Manages the tutorial modal of the Quiz. If the page is opened for the first time, the tutorial is displayed.
*/
$(window).on('load', function(){
	var firstTime = $('#firstTime').val();
	if(firstTime == 0){
		$('#firstTimeInfoModal').modal('toggle');
	}
});

/*
	Manages the enabling of the submit button. If the text is too long, the button is disabled.
*/
$(document).on('keyup keydown', '.form-control', function(){
	var text = $('.form-control').val();
	
	if($('#answer').val() != 0 && text.length < 5000) {
		$('#submitButton').prop("disabled", false);
	} else {
		$('#submitButton').prop("disabled", true);
	}
});

/*
	Manages the selection of the 1st answer.
*/
$(document).on('click', '#answer1', function() {
	if($('#answer').val() != 0) {
		$('#answer' + $('#answer').val()).removeClass("btn-success");
		$('#answer' + $('#answer').val()).addClass("btn-info");
	}
	
	$('#answer').val(1);
	$('#submitButton').prop('disabled', false);
	
	$('#answer1').removeClass("btn-info");
	$('#answer1').addClass("btn-success");
	
})

/*
	Manages the selection of the 2nd answer.
*/
$(document).on('click', '#answer2', function() {
	if($('#answer').val() != 0) {
		$('#answer' + $('#answer').val()).removeClass("btn-success");
		$('#answer' + $('#answer').val()).addClass("btn-info");
	}
	
	$('#answer').val(2);
	$('#submitButton').prop('disabled', false);
	
	$('#answer2').removeClass("btn-info");
	$('#answer2').addClass("btn-success");
})

/*
	Manages the selection of the 3rd answer.
*/
$(document).on('click', '#answer3', function() {
	if($('#answer').val() != 0) {
		$('#answer' + $('#answer').val()).removeClass("btn-success");
		$('#answer' + $('#answer').val()).addClass("btn-info");
	}
	
	$('#answer').val(3);
	$('#submitButton').prop('disabled', false);

	$('#answer3').removeClass("btn-info");
	$('#answer3').addClass("btn-success");
})

/*
	Manages the selection of the 4th answer.
*/
$(document).on('click', '#answer4', function() {
	if($('#answer').val() != 0) {
		$('#answer' + $('#answer').val()).removeClass("btn-success");
		$('#answer' + $('#answer').val()).addClass("btn-info");
	}
	
	$('#answer').val(4);
	$('#submitButton').prop('disabled', false);

	$('#answer4').removeClass("btn-info");
	$('#answer4').addClass("btn-success");
})

/*
	Manages the selection of the 5th answer.
*/
$(document).on('click', '#answer5', function() {
	if($('#answer').val() != 0) {
		$('#answer' + $('#answer').val()).removeClass("btn-success");
		$('#answer' + $('#answer').val()).addClass("btn-info");
	}
	
	$('#answer').val(5);
	$('#submitButton').prop('disabled', false);

	$('#answer5').removeClass("btn-info");
	$('#answer5').addClass("btn-success");
})

/*
	Manages the increase in size of the textarea, if the text overflows.
*/
function auto_grow(element) {
    element.style.height = "5px";
    element.style.height = (element.scrollHeight) + "px";
}

/*
	Submit the answer to the question.
*/
$(document).on('submit', '#questionForm', function(event) {
	var token = $("meta[name='_csrf']").attr("content");
	
	if($('#toBeShown').val() > 0) {
		event.preventDefault();
		
		$('#submitButton').prop('disabled', true);
		
		$.ajax({
			 type: "POST",
		     url: "/answerQuestion",
		     data: $(this).serialize(),
		     success : function(fragment)
		     {
		    	if(typeof Cookies.get("errorMsg") !== 'undefined'){
		    		$(location).attr('href','/');
		    	} else {
		    		
		    		$('html, body').animate({
						scrollTop: 0
					}, 1);
		    		
		    		$('#questionDiv').replaceWith(fragment);	
		    	}
		     }
		 })
	}
})

/*
	Manage the toggle description of the Scenario.
*/
$('#collapseOne').on('shown.bs.collapse', function(){
	$('.chevron').html('<i class="fas fa-chevron-down" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the Scenario.
*/
$('#collapseOne').on('hidden.bs.collapse', function(){
	$('.chevron').html('<i class="fas fa-chevron-right" aria-hidden="true"></i>');
});