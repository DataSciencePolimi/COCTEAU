/**
 * Toggles the cookie consent box and tooltip.
 */
$(document).ready(function readCookie() {
    var cookie = Cookies.get('errorMsg');
    var cookiestatus = Cookies.get('cookieconsent_status');
    var selected_lang = Cookies.get('localeInfo');
    
    if(selected_lang == "it" || selected_lang == "en") {
    	if(cookiestatus != "allow"){
    		$("#consent-elements").show();
    	}
    } else {
    	$("#language-selection").show();
    }
    
	$("#active-consent-cont").css("pointer-events", "none");
	$("#active-consent-cont").css("filter", "grayscale(100%)");
    
    if(typeof cookie !== 'undefined') {
    	$('.modal').modal("toggle");
    	Cookies.remove('errorMsg');
    }
    
    if($('.cc-window').is(":hidden") && cookiestatus != "allow") {	        	
    	container = $(".cc-window");
    	container.removeClass('cc-invisible');
    	container.css("display", "block")
    	
  		container.css("position", "relative");
  		container.css("max-width", "100%");
  		container.css("left", "50%");
  		container.css("border-radius", "5px");
  		container.css("transform", "translate(-50%)");
  		$(".cc-compliance").addClass("justify-content-center");
    }
    setUpCookieConsent();
});

/**
 * Parses the query string into a javascript object.
 * @param query query string
 * @returns
 */
function parse_query_string(query) {
  	var vars = query.split("&");
  	var query_string = {};
  	for (var i = 0; i < vars.length; i++) {
    	var pair = vars[i].split("=");
    	var key = decodeURIComponent(pair[0]);
    	var value = decodeURIComponent(pair[1]);
    	// If first entry with this name
    	if (typeof query_string[key] === "undefined") {
      		query_string[key] = decodeURIComponent(value);
      	// If second entry with this name
    	} else if (typeof query_string[key] === "string") {
      		var arr = [query_string[key], decodeURIComponent(value)];
      	query_string[key] = arr;
      // If third or later entry with this name
    	} else {
      	query_string[key].push(decodeURIComponent(value));
    	}
  	}
  	return query_string;
}

/**
 * Variables for holding localized messages for the user.
 */
var message_var = null;
var allow_var = null;
var link_var = null;
var href_var = null;
var deny_var = null;

/**
 * Sets the values of the localized messages depending on the language.
 */
if($('#lang').text() === "en") {
	message_var = "This website uses non-commercial cookies to ensure its correct functioning. By proceeding you accept such cookies.";
	allow_var = "Allow Cookies";
	deny_var = "Decline";
	link_var = "Learn more here.";
	href_var = "https://www.garanteprivacy.it/regolamentoue";
} else if($('#lang').text() === "it") {
	message_var = "Questo sito utilizza cookies non commerciali per il proprio funzionamento. Procedendo accetti l'utilizzo di tali cookies";
	allow_var = "Consenti Cookies";
	deny_var = "Rifiuta";
	link_var = "Più dettagli qui.";
	href_var = "https://www.garanteprivacy.it/regolamentoue";
}

/**
 * Retrieve and hold the raw query string.
 */
var query = window.location.search.substring(1);
/**
 * Holds the parsed query string.
 */
var qs = parse_query_string(query);

/**
 * Adds or removes css pointer and filter properties to the cookie consent box.
 * @returns
 */
$(document).on('change', '#privacy-accept', function() {
	if($('#privacy-accept').prop("checked")) {
		$("#active-consent-cont").css("pointer-events", "auto");
		$("#active-consent-cont").css("filter", "");
	} else {
		$("#active-consent-cont").css("pointer-events", "none");
		$("#active-consent-cont").css("filter", "grayscale(100%)");
	}
})

/**
 * Sets up the cookie consent boxes, tooltips and actions.
 * Based on the open source Cookie Consent library from Osano.
 * @returns
 */
function setUpCookieConsent(){
	top.window.cookieconsent.initialise({
		container: document.getElementById("active-consent-cont"),
		palette: {
	    	popup: {
	      		background: "#1f3b48"
	    	},
	    	button: {
	      		background: "#149cbc"
	    	}
	  	},
	  	revokable: true,
	  	animateRevokable: false,
	  	onInitialise: function(status) {
	  		var token = $("meta[name='_csrf']").attr("content");
	  		if(status == "allow"){
	  			$.ajax({
					 type: "GET",
				     url: "/getJoinButtons",
				     success : function(fragment)
				     {
				    	 $('#login-elements-div').replaceWith(fragment);
				    	 $('.consent-text').hide();
				    	 $('.privacy-container').hide();
				    	 $('#privacy-accept').prop('checked', false);
				    	 $('.cc-revoke').attr("style", "display: block !important; " 
				    	 							+ "position: fixed !important; "
				    	 							+ "bottom: 0px !important; "
				    	 							+ "border-radius: 5px 5px 0px 0px !important");
				    	 $('.cc-window').hide();
				    	 $("#active-consent-cont").css("pointer-events", "auto");
				    	 $("#active-consent-cont").css("filter", "");
				    	 $('#consent-elements').show();
				    	 $("#language-selection").hide();
				     }
			 	});
	  		}
	  	},
	  	onStatusChange: function(status) {
	  		var check = false;
	  		var token = $("meta[name='_csrf']").attr("content");
	  		$.ajax({
	  			type: "POST",
	  			url: "/saveUpdateCookieConsent",
	  			data: { "consent": this.hasConsented(), "referral": qs.ref},
	  		}).done(function(data){
	  			if (data == "false"){
  					$('#login-elements-div').html('');
  				} else if (data == "true") {
		  			$.ajax({
 						 type: "GET",
 					     url: "/getJoinButtons",
 					     success : function(fragment){
 					   		$('#login-elements-div').replaceWith(fragment);
 					   		$('.consent-text').hide();
 					   		$('.cc-window').hide();
 					   		$('.privacy-container').hide();
 					   		$('#privacy-accept').prop('checked', false);
 					   		$('.cc-revoke').attr("style", "display: block !important; " 
					    	 							+ "position: fixed !important; "
					    	 							+ "bottom: 0px !important; "
					    	 							+ "border-radius: 5px 5px 0px 0px !important");
							$('.cc-window').hide();
				    	 	$("#active-consent-cont").css("pointer-events", "auto");
				    	 	$("#active-consent-cont").css("filter", "");
				    	 	$('#consent-elements').show();
				    	 	$('#login-elements').show();
				    	 	$("#language-selection").hide();
 						}
 					});
  				}
	  		});
	  	},
	  	onPopupOpen: function (){
			container = $(".cc-window");
			container.show();
	  		container.css("position", "relative");
	  		container.css("max-width", "100%");
	  		container.css("left", "50%");
	  		container.css("border-radius", "5px");
	  		container.css("transform", "translate(-50%)");
	  		
	  		compliance = $(".cc-compliance");
	  		compliance.css("align-self", "center");
	  		compliance.addClass("justify-content-center");
			if(!$('.flag-btn').length > 0){
 		  		container.css("filter", "grayscale(100%)");
				$("#active-consent-cont").css("pointer-events", "none");
			}
			$('#login-elements-div').html('');
			$('.consent-text').show();
	  		$('.privacy-container').show();
	  		$('#login-elements').hide();
	  	},
	  	theme: "classic",
	  	position: "",
	  	type: "opt-out",
	  	content: {
	  		message: message_var,
		    allow: allow_var,
		    deny: deny_var,
		    link: link_var,
		    href: href_var
	  	}
	});

}

/**
 * Handles the greyscale filter for the cookie consent box.
 * @returns
 */
$(document).on('click', '#privacy-accept', function(){
	if($('#privacy-accept').is(':checked')) {
		$('.cc-window').css("filter", "");
	} else {
		container.css("filter", "grayscale(100%)");
	}
});

/**
 * Handles supported languages.
 * @returns
 */
$(document).on('click', '.lang-item', function(){
	var selectedLang = $(this).data('lang');
	if(selectedLang != '' && selectedLang != "ge" && selectedLang != "fr") {
		window.location.replace('?lang='+selectedLang);
	}
});

/**
 * Hides some UI elements used to access the platform if a user decides to switch language.
 * @returns
 */
$(document).on('click', '#change-language', function(){
	$("#consent-elements").hide();
	$("#login-elements").hide();
	$("#language-selection").show();
	$("#change-language-div").hide();
})