$(document).on('keyup keydown click', '#keywordInsertionForm1, #keywordInsertionForm2, #keywordInsertionForm3, #descriptionInsertionForm, .vote-vis', function() {
	checkFilledElements();
});

/*
	Check if all the keywords inserted while creating the vision are different. If so, the submit button is enabled, otherwise it is disabled.
*/
function checkFilledElements(){
	var kw1 = $('#keywordInsertionForm1').val().toLowerCase();
	var kw2 = $('#keywordInsertionForm2').val().toLowerCase();
	var kw3 = $('#keywordInsertionForm3').val().toLowerCase();
	
	var disabled_submit = false;
	
	if($('#descriptionInsertionForm').val() != "" && $('#descriptionInsertionForm').val().length < 5000) {
		if(kw1 != ""  && kw2 != "" && kw3 != "") {
			if(kw1 != kw2 && kw2 != kw3 && kw3 != kw1) {
				if(!(kw1.length <= 100 && kw2.length <= 100 && kw3.length <= 100)) {
	 				disabled_submit = true;
	 			}
			} else {
				disabled_submit = true;
			}
		} else {
			disabled_submit = true;
		}
	} else {
		disabled_submit = true;
	}
	
	if($('#feelingsVote').val() == "0") {
		disabled_submit = true;
	}
	
	// Check pictures also after keywords input
	var expectedImages = 0;
	
	if (layout == 0) {
		expectedImages = 1;
	} else if (layout == 1 || layout == 2){
		expectedImages = 3;
	} else {
		expectedImages = 4;
	}
	
	selectedImages = getSelectedImages();
	
	for (var i=0; i<expectedImages; i++){
		if (selectedImages[i] == null){
			disabled_submit = true;
			break;
		}
	}
	
	$('#vision-submit-button').prop('disabled', disabled_submit);
}

/*
	Submit the form when the submit button is pressed.
*/
$(document).on('click', '#vision-submit-button', function(){
	$('#vision-submit-button').prop('disabled', true);
	$('#formVisionInfo').submit();
});

/*
	Filter the input in the forms.
*/
$(document).on('keypress', '#keywordInsertionForm1, #keywordInsertionForm2, #keywordInsertionForm3', function(key) {
	if (!(/([A-Za-zÀ-ÖØ-öø-ÿ])$/.test(key.key))){
		return false;
	}
});

var pickQueryWords = ["", "", "", ""];
var searchQueryWords = [];

/*
	Add the searched word to the array.
*/
$(document).on('click', '#search-button', function() {
	if($('#search-keyword').val() != "" && $('#search-keyword').val() != null) {
		searchQueryWords.push($('#search-keyword').val());
	}
})

/*
	Add the query word of the pictures in the vision to the array. This is done so that if the user changes the pictures multiple times,
	we are able to distinguish between the ones they searched for and the ones they picked in the final vision.
*/
$(document).on('click', '.updatePic', function() {
	var posArr = $(slot).children().children().data("pos");
	if($('#search-keyword').val() != "" && $('#search-keyword').val() != null) {
		pickQueryWords[posArr] = $('#search-keyword').val();
	}
})

$(document).on('click', '.preselected', function() {
	var id = $(this).attr('id');
	var posArr = $(slot).children().children().data("pos");
	
	pickQueryWords[posArr] = id;
	searchQueryWords.push(id);
})

/*
	Before the submission, prepares the query words fields to be stored.
*/
$("#formVisionInfo").submit( function(eventObj) {
	
	var tmpPickQueryWords = "";
	
	for(var i = 0; i < pickQueryWords.length; i++) {
		if(pickQueryWords[i] != "") {
			tmpPickQueryWords += pickQueryWords[i] + ",";
		}
	}
	
	var tmpSearchQueryWords = "";
	
	for(var i = 0; i < searchQueryWords.length; i++) {
		if(searchQueryWords[i] != "") {
			tmpSearchQueryWords += searchQueryWords[i] + ",";
		}
	}
	
	$("<input />").attr("type", "hidden")
        .attr("name", "pickQueryWords")
        .attr("value", tmpPickQueryWords)
        .appendTo("#formVisionInfo");
    
	$("<input />").attr("type", "hidden")
    	.attr("name", "searchQueryWords")
    	.attr("value", tmpSearchQueryWords)
    	.appendTo("#formVisionInfo");
    
    return true;
});

/**
 * Switches CSS classes when a user selects a particular feeling.
 * Checks also if the user left a comment explaining their feelings.
 * @returns
 */
$('.vote-vis').on('click', function(){
		if($('.vote-vis').children('path').hasClass('blink')){
			$('.vote-vis').children('path').removeClass('blink');
		}
	
		$(this).children('path').addClass('blink');
		var vote = $(this).data('vote');
	
		$('#feelingsVote').val(vote);
	});