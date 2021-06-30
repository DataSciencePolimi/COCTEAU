/**
 * AJAX call to create an Administrator account. Only accounts with root privileges can do this.
 * */

$(document).on('submit', '#createAdminProfile', function(e){
	e.preventDefault();
	$('#SubmitButtonCreateProfile').prop('disabled', true);
	
	$("#username-error-creation").hide();
	$("#new-password-error-creation").hide();
	$("#confirm-password-error-creation").hide();
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({
		 type: "POST",
	     url: "/createAdminProfile",
	     data: $(this).serialize(),
	     success : function(array)
	     {
	    	 if(array[0][0] != "error" && array[0][0] != "success") {		    		 
	    		 for(var i = 0; i < array.length; i++) {
	    			 
	    			 $('#' + array[i][0]).text(array[i][1]);
	    			 $('#' + array[i][0]).show();
	    		 }
	    	 } else {
	    		 window.location.href = window.location.origin + array[0][1];
	    	 }
	     }
	 })
	 
	 $('#SubmitButtonCreateProfile').prop('disabled', false);
})