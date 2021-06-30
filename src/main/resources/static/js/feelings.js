/**
 * Toggles a different modal depending on whether it is the 
 * first time or not a user accesses the Feelings page.
 * @returns
 */
$(window).on('load', function(){
	var firstTime = $('#firstTime').val();
	var secondTime = $('#secondTime').val();
	
	if(firstTime == 0){
		$('#firstTimeInfoModal1').modal('toggle');
	} else if(secondTime == 0) {
		$('#firstTimeInfoModal2').modal('toggle');
	}
});

/**
 * Holds a user's feeling
 */
var vote = "";

/**
 * Disables the submit button by default.
 * @returns
 */
$('#submitFeels').prop("disabled", true);

/**
 * Switches CSS classes when a user selects a particular feeling.
 * Checks also if the user left a comment explaining their feelings.
 * @returns
 */
$('.vote').on('click', function(){
	if($('.vote').hasClass('selected')){
		$('.vote').removeClass('selected');
	}

	$(this).addClass('selected');
	vote = $(this).data('vote');
	
	$('#feelingsVote').val(vote);
	
	check_filled();
});

/**
 * Calls the function for checking the comment left by the user as it is being typed.
 * @returns
 */
$(document).on('keyup keydown', '.form-control', function(){
	check_filled();
});

/**
 * Submits the feeling + comment data to the backend.
 * @returns
 */
$(document).on('click', '#submitFeels', function() {
	$('#submitFeels').prop("disabled", true);
	$('#formFeelings').submit();
});

/**
 * Utility function used to check that the comment left by a user respects certain criteria.
 * If a comment is present and a feeling has been selected the submit button is enabled.
 * @returns
 */
function check_filled(){
	var text = $('.form-control').val();
	
	if(text.length > 0 && text.length < 5000 && vote != "") {
		$('#submitFeels').prop("disabled", false);
	} else {
		$('#submitFeels').prop("disabled", true);
	}
}