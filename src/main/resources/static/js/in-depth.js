/**
 * Toggle the tutorial modal if the user is seeing the in-depth page for the first time.
 * @returns
 */
$(window).on('load', function(){
	var firstTime = $('#firstTime').val();
	if(firstTime == 0){
		$('#firstTimeInfoModal').modal('toggle');
	}
});

/**
 * Checks that the comment left by a user respects certain criteria.
 * @returns
 */
$(document).on('keyup keydown', '.input-form', function(){
	var text = $('.input-form').val();
	
	if(text != "" && text.length < 5000) {
		$('#check-button').prop("disabled", false);
	} else {
		$('#check-button').prop("disabled", true);
	}
});

/**
 * Adjusts the height of the text area for the user comment.
 * @returns
 */
$(document).ready(function(){
    $('.description-form').css('height', (document.getElementsByClassName('description-form')[0].scrollHeight + 5) + 'px');
})

/**
 * Submits the details of the in-depth match.
 * @returns
 */
$(document).on('click', '#end-button', function(){
	$('#button-pressed-indepth').val("end");
	$('#matchForm').submit();
});

/**
 * Submits the details of the in-depth match.
 * @returns
 */
$(document).on('click', '#check-button', function(){
	$('#button-pressed-indepth').val("check");
	$('#check-button').prop('disabled', true);
	$('#matchForm').submit();
});

/**
 * If the scenario description is hidden the chevron icon near is switched to the 'right' variant.
 * @returns
 */
$('#scenarioDesc').on('hidden.bs.collapse', function(){
	$('.chevron-scenario').html('<i class="fas fa-chevron-right" aria-hidden="true"></i>');
});

/**
 * If the scenario description is shown the chevron icon near is switched to the 'down' variant.
 * @returns
 */
$('#scenarioDesc').on('shown.bs.collapse', function(){
	$('.chevron-scenario').html('<i class="fas fa-chevron-down" aria-hidden="true"></i>');
});