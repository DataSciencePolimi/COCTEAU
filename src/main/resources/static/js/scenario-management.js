/*
	Manages the selection of the scenarios associable with the one in another language.
*/
$(document).on("change", "#narrative", function() {
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		 type: "GET",
	     url: "/retrieve-coupable-scenarios",
	     data: {
	    	 narrativeName: $(this).children("option:selected").data("name"),
	    	 narrativeLang: $(this).children("option:selected").data("lang"),
	     },
	     success : function(fragment)
	     {
	    	 $('#coupableScenariosFragment').replaceWith(fragment);
	     }
	 })
})

/*
	Manages the modal to create a Narrative.
*/
$(document).on("click", "#createNarrativeButton", function( ){
	$("#modalCreateNarrative").modal("toggle");
})

/*
	Create the Narrative.
*/
$(document).on("submit", "#createNarrativeModalForm", function(e){
	e.preventDefault();
	$("#modalCreateNarrative").modal("toggle");
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({
		 type: "POST",
	     url: "/create-narrative",
	     data: $(this).serialize(),
	     success : function(fragment)
	     {
	    	 $('#narrativeCreationFragment').replaceWith(fragment);
	     }
	 })
})

/*
	Manages the modal to create a Referral Code.
*/
$(document).on("click", "#createReferralButton", function( ){
	$("#modalCreateReferral").modal("toggle");
})

/*
	Creates a Referral Code.
*/
$(document).on("submit", "#createReferralModalForm", function(e){
	e.preventDefault();
	$("#modalCreateReferral").modal("toggle");
	var token = $("meta[name='_csrf']").attr("content");
	
	$.ajax({
		 type: "POST",
	     url: "/create-referral-code",
	     data: $(this).serialize(),
	     success : function(fragment)
	     {
	    	 $('#referralCodeFragment').replaceWith(fragment);
	     }
	 })
})