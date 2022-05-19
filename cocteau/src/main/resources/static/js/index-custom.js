/**
 * Toggles the cookie consent box and tooltip.
 */
$(document).ready(function readCookie() {
    var cookie = Cookies.get('errorMsg');
    var cookiestatus = Cookies.get('cookieconsent_status');
    var selected_lang = Cookies.get('localeInfo');
  
    if(cookiestatus == "allow") {
    	$('.cc-revoke').hide();
    }
    
	$("#active-consent-cont").css("pointer-events", "none");
    
    if($('.cc-window').is(":hidden") && cookiestatus != "allow") {	        	
    	container = $(".cc-window");
    	container.removeClass('cc-invisible');
    	container.css("display", "block")
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
 * Retrieve and hold the raw query string.
 */
var query = window.location.search.substring(1);

/**
 * Holds the parsed query string.
 */
var qs = parse_query_string(query);

/**
 * Sets the values of the localized messages depending on the language.
 */
if(!qs.hasOwnProperty('lang') || qs.lang === "en") {
	message_var = "This website uses non-commercial cookies to ensure its correct functioning. By proceeding you accept such cookies. By rejecting such cookies, you won't be able to use most functionalities proposed by the platform.";
	allow_var = "Allow Cookies";
	deny_var = "Decline";
	link_var = "Learn more here.";
	href_var = "https://www.garanteprivacy.it/regolamentoue";
} else if(qs.lang === "it") {
	message_var = "Questo sito utilizza cookies non commerciali per il proprio funzionamento. Procedendo accetti l'utilizzo di tali cookies. Rifiutando tali cookies, non potrai utilizzare molte delle funzionalità fornite dalla piattaforma";
	allow_var = "Consenti Cookies";
	deny_var = "Rifiuta";
	link_var = "Più dettagli qui.";
	href_var = "https://www.garanteprivacy.it/regolamentoue";
}

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
	  	},
	  	onStatusChange: function(status) {
	  		var check = false;
	  		var token = $("meta[name='_csrf']").attr("content");
	  		
	  		$.ajax({
	  			type: "POST",
	  			url: "/saveUpdateCookieConsent",
	  			data: { "consent": this.hasConsented(), "referral": qs.ref},
	  		})
	  	},
	  	onPopupOpen: function (){
			container = $(".cc-window");
			container.show();
	  		container.css("max-width", "100%");
	  		container.css("left", "50%");
	  		container.css("border-radius", "5px");
	  		container.css("transform", "translate(-50%)");
	  		
	  		compliance = $(".cc-compliance");
	  		compliance.css("align-self", "center");
	  		compliance.addClass("justify-content-center");
	  	},
	  	theme: "classic",
	  	position: "bottom",
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