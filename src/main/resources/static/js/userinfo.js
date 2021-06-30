/*
	Submit the information about the User.
*/
$(document).on('click', '#submitKey', function() {
	$('#submitKey').prop('disabled', true);
	$('#dataInfo').submit();
});

/*
	Manages the enabling of the submit button. If all the mandatory informations are not inserted, the button is disabled, otherwise it is enabled.
*/
$(document).on('change', '#ageSelection, #gender, #nationalitySelection, #educationSelection, #interestSelection', function(){
	if($('#ageSelection').find('option:selected').val() != 0  &&
			$('#nationalitySelection').find('option:selected').val() != 'NULL' &&
			$('#educationSelection').find('option:selected').val() != 'NULL' &&
			$('#interestSelection').find('option:selected').val() != 'NULL') {
		$('#submitKey').prop('disabled', false);
	} else {
		$('#submitKey').prop('disabled', true);
	}
});

/*
	Manages the nationality field. If the European nationality is picked, the Country field is displayed. If the Non-European nationality is picked, the Region field is displayed.
*/
$(document).on('change', '#nationalitySelection', function(){
	if($('#nationalitySelection').find('option:selected').val() == 'Eu') {
		$('#countryRow').css('display', 'table-row');
		$('#regionRow').css('display', 'none');
	} else if($('#nationalitySelection').find('option:selected').val() == 'Non-Eu') {
		$('#countryRow').css('display', 'none');
		$('#regionRow').css('display', 'table-row');
	}
});

/*
	Disables the selection of the default option when another is picked.
*/
$(document).on('change', '#ageSelection, #nationalitySelection, #educationSelection, #interestSelection, #regionSelection, #countrySelection', function(){
	$(this).children('#firstOption').remove();
});

/*
	Check whether the e-mail is correct.
*/
$(document).on('keyup', '#mail', function(){
	var mail = $('#mail').val().toLowerCase();
	
	if(mail.length > 254 || mail.length == 0) {
		$('#submitKey').prop('disabled', true);
	} else {
		if($('#ageSelection').find('option:selected').val() != 0  &&
 				$('#nationalitySelection').find('option:selected').val() != 'NULL' &&
 				$('#educationSelection').find('option:selected').val() != 'NULL' &&
 				$('#interestSelection').find('option:selected').val() != 'NULL') {
 			$('#submitKey').prop('disabled', false);
 		} else {
 			$('#submitKey').prop('disabled', true);
 		}
	}
});