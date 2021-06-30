var allKeywordsSet = false;

/*
	Check if it is the first time the page is loaded. If so, the modal with the tutorial is displayed.
*/
$(window).on('load', function(){
	var firstTime = $('#firstTime').val();
	if(firstTime == 0){
		$('#firstTimeInfoModal').modal('toggle');
	}
});

/*
	Check if all the keywords inserted while creating the vision are different. If so, the submit button is enabled, otherwise it is disabled.
*/
$(document).on('keyup keydown', '#keywordInsertionForm1, #keywordInsertionForm2, #keywordInsertionForm3, #descriptionInsertionForm', function(){
	var kw1 = $('#keywordInsertionForm1').val().toLowerCase();
	var kw2 = $('#keywordInsertionForm2').val().toLowerCase();
	var kw3 = $('#keywordInsertionForm3').val().toLowerCase();
	if($('#descriptionInsertionForm').val() != "" && $('#descriptionInsertionForm').val().length < 5000) {
		if(kw1 != ""  && kw2 != "" && kw3 != "") {
			if(kw1 != kw2 && kw2 != kw3 && kw3 != kw1) {
				if(kw1.length <= 100 && kw2.length <= 100 && kw3.length <= 100) {
	 				$('#submitKey').prop('disabled', false);
	 				allKeywordsSet = true;
				} else {
	 				$('#submitKey').prop('disabled', true);
	 				allKeywordsSet = false;
	 			}
			} else {
				$('#submitKey').prop('disabled', true);
				allKeywordsSet = false;
			}
		} else {
			$('#submitKey').prop('disabled', true);
			allKeywordsSet = false;
		}
	} else {
		$('#submitKey').prop('disabled', true);
		allKeywordsSet = false;
	}
	// Check pictures also after keywords input
	checkPictures();
});

/*
	Submit the form when the submit button is pressed.
*/
$(document).on('click', '#submitKey', function(){
	$('#submitKey').prop('disabled', true);
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

// Quick toggles for the sliders
var step = 1.0
var minSlider = 1.0
var maxSlider = 5.0

/*
	Manage the click on the first minus button.
*/
$(document).on('click', '#label_first_minus', function(){
	temp = parseInt($('#firstDim').val());
	if(temp - step >= minSlider){
		$('#firstDim').val(temp - step);	
	} else {
		$('#firstDim').val(minSlider);
	}
});

/*
	Manage the click on the first plus button.
*/
$(document).on('click', '#label_first_plus', function(){
	temp = parseInt($('#firstDim').val());
	if(temp + step > maxSlider){
		$('#firstDim').val(maxSlider);
	} else {
		$('#firstDim').val(temp + step);
	}
});

/*
	Manage the click on the second minus button.
*/
$(document).on('click', '#label_second_minus', function(){
	temp = parseInt($('#secondDim').val());
	if(temp - step >= minSlider){
		$('#secondDim').val(temp - step);	
	} else {
		$('#secondDim').val(minSlider);
	}
});

/*
	Manage the click on the second plus button.
*/
$(document).on('click', '#label_second_plus', function(){
	temp = parseInt($('#secondDim').val());
	if(temp + step > maxSlider){
		$('#secondDim').val(maxSlider);
	} else {
		$('#secondDim').val(temp + step);
	}
});

var pickQueryWords = ["", "", ""];
var searchQueryWords = [];

/*
	Add the searched word to the array.
*/
$(document).on('click', '#search-button', function() {
	searchQueryWords.push($('#search-keyword').val());
})

/*
	Add the query word of the pictures in the vision to the array. This is done so that if the user changes the pictures multiple times,
	we are able to distinguish between the ones they searched for and the ones they picked in the final vision.
*/
$(document).on('click', '.updatePic', function() {
	var posArr = $(slot).children().children().data("pos");
	pickQueryWords[posArr] = $('#search-keyword').val();
})

/*
	Before the submission, prepares the query words fields to be stored.
*/
$("#formVisionInfo").submit( function(eventObj) {
	
	var tmpPickQueryWords = "";
	
	for(var i = 0; i < pickQueryWords.length; i++) {
		tmpPickQueryWords += pickQueryWords[i] + ",";
	}
	
	var tmpSearchQueryWords = "";
	
	for(var i = 0; i < searchQueryWords.length; i++) {
		tmpSearchQueryWords += searchQueryWords[i] + ",";
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

/*
	Manage the toggle description of the scenario.
*/
$('#scenarioDesc').on('hidden.bs.collapse', function(){
	$('.chevron-scenario').html('<i class="fas fa-chevron-right" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the scenario.
*/
$('#scenarioDesc').on('shown.bs.collapse', function(){
	$('.chevron-scenario').html('<i class="fas fa-chevron-down" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the first dimension.
*/
$('#sliderDesc').on('hidden.bs.collapse', function(){
	$('.chevron-sliders').html('<i class="fas fa-chevron-right" aria-hidden="true"></i>');
});

/*
	Manage the toggle description of the second dimension.
*/
$('#sliderDesc').on('shown.bs.collapse', function(){
	$('.chevron-sliders').html('<i class="fas fa-chevron-down" aria-hidden="true"></i>');
});