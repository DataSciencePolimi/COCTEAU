/*
	Manages the input length of the username and the password.
*/
$(document).on('keyup keydown', '#username, #password', function(){
	var username = $('#username').val();
	var password = $('#passowrd').val();
	
	if(username.length > 50 && password.length > 150){
		$('[name="submit_en"]').prop('disabled', true);
		$('[name="submit_it"]').prop('disabled', true);
	} else {
		$('[name="submit_en"]').prop('disabled', false);
		$('[name="submit_it"]').prop('disabled', false);
	}
});