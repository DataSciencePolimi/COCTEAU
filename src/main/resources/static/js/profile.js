/*
	Manages the tutorial modal of the Profile. If the page is opened for the first time, the tutorial is displayed.
*/
$(window).on('load', function(){
	var firstTime = $('#firstTime').val();
	
	if(firstTime == 0){
		$('#firstTimeInfoModal').modal('toggle');
	}
});

/*
	Manages the toggle of the modal showing the activation of the account.
*/
$(document).on('click', '#x_activated, #close_button_activated', function() {
	$("#activated_modal").modal('toggle');
})

/*
	Manages the toggle of the modal with the information about the profile.
*/
$(document).on('click', "#x_first_time, #close_button_first_time", function() {
	$("#firstTimeInfoModal").modal('toggle');
})

/*
	Manages the toggle of the modal to edit the profile.
*/
$(document).on('click', '#editProfileOpen', function() {
	$('#editProfileModal').modal('toggle');
})

/*
	Manages the toggle of the modal to create the profile.
*/
$(document).on('click', '#createProfileOpen', function() {
	$('#createProfileModal').modal('toggle');
})

/*
	Manages the submission of the form to update the Password.
*/
$(document).on('submit', '#updatePasswordForm', function(e){
	e.preventDefault();
	$('#submitButtonPwd').prop('disabled', true);

	$("#old-password-error").hide();
	$("#new-password-error").hide();
	$("#confirm-password-error").hide();
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({
		 type: "POST",
	     url: "/updatePassword",
	     data: $(this).serialize(),
	     success: function(array) {
	    	 if(array.length > 0) {
	    		 for(var i = 0; i < array.length; i++) {
	    			 
	    			 $('#' + array[i][0]).text(array[i][1]);
	    			 $('#' + array[i][0]).show();
	    		 }
	    	 } else {
	    		 location.reload();
	    	 }
	     },
	})
	
	$('#submitButtonPwd').prop('disabled', false);
})

/*
	Manages the submission of the form to create the Profile.
*/
$(document).on('submit', '#createProfile', function(e){
	
	e.preventDefault();
	$('#SubmitButtonCreateProfile').prop('disabled', true);
	
	$("#username-error-creation").hide();
	$("#new-password-error-creation").hide();
	$("#confirm-password-error-creation").hide();
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({
		 type: "POST",
	     url: "/createProfile",
	     data: $(this).serialize(),
	     success : function(array)
	     {
	    	 if(array.length > 0) {
	    		 for(var i = 0; i < array.length; i++) {
	    			 
	    			 $('#' + array[i][0]).text(array[i][1]);
	    			 $('#' + array[i][0]).show();
	    		 }
	    		 
	    		 
	    	 } else {
	    		 location.reload();
	    	 }
	     }
	 })
	 
	 $('#SubmitButtonCreateProfile').prop('disabled', false);
})

/*
	Manages the submission of the form to update the Username.
*/
$(document).on('submit', '#updateUsernameForm', function(e){
	
	e.preventDefault();
	$('#submitButton').prop('disabled', true);
	$("#username-error").hide();
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({
		 type: "POST",
	     url: "/updateUsername",
	     data: $(this).serialize(),
	     success : function(array)
	     {
	    	 if(array.length > 0) {
	    		 for(var i = 0; i < array.length; i++) {
	    			 
	    			 $('#' + array[i][0]).text(array[i][1]);
	    			 $('#' + array[i][0]).show();
	    		 }
	    	 } else {
	    		 location.reload();
	    	 }
	     }
	 })
	 
	 $('#submitButton').prop('disabled', false);
})

/*
	Trigger the upload of a new picture when the profile picture is pressed.
*/
$(document).on('click', '#profilePicture', function() {
	$('#newProfilePicture').trigger('click');
})

/*
	Updates the profile picture of the User.
*/
$(document).on('change', '#newProfilePicture', function(e) {
	
	e.preventDefault();
	var token = $("meta[name='_csrf']").attr("content");
	if($('#newProfilePicture').get(0).files.length > 0) {
		
		var form = $('#updateProfilePict')[0]; 
		var formData = new FormData(form);
		
		$.ajax({
			 type: "POST",
		     url: "/updateProfilePicture",
		     contentType: false,
		     processData: false,
		     data: formData,
		     success : function(fragment)
		     {
		    	 location.reload();
		     }
		 })
	}
})