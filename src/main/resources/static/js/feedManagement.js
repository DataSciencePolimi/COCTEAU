/**
 * Event listener for the updateTags utility function.
 * Triggered on resize.
 * @returns
 */
$(window).on('resize', function() {
	$("img").each(function(){
		updateTags(this);
	})
});

/**
 * Event listener used to save the window size when loaded.
 * Used to handle the menu buttons on smaller screens.
 * @returns
 */
$(window).on('load', function(){
	var w = window.innerWidth;
	
	if(w > 767) {
		if(newScSize != null) {
			oldScSize = newScSize;
		}
		
		newScSize = "Large"
	} else {
		if(newScSize != null) {
			oldScSize = newScSize;
		}
		
		newScSize = "Small"
	}
	
	var firstTime = $('#firstTime').val();
	var survey = $('#survey').val();
	if(firstTime == 0){
		$('#firstTimeInfoModal').modal('toggle');
	} else if(survey == "true") {
		$('#surveyModal').modal('toggle');
	}
});

/**
 * Event listener for the updateTags utility function.
 * Triggered when an image is loaded.
 * @returns
 */
$("img").one("load", function() {
 	updateTags(this);
	$(this).addClass("updated");
 }).each(function() {
 	if(this.complete && !$(this).hasClass("updated")) {
		$(this).trigger('load')
	}
 });

/**
 * Utility function used to resize the keywords' containers of a vision.
 * @param img HTML node containing the vision
 * @returns
 */
function updateTags(img) {
	var kwTags = $(img).parent().children('.kw-tags');
	var image = img;
	var image_width = $(image).width();
	
	$(kwTags).width(image_width)
	
	image_width = image_width - 30;
	
	var children = $(kwTags).children().children();
	
	$(children[0]).css("display", "block");
	$(children[1]).css("display", "block");
	$(children[2]).css("display", "block");
	
	var tag_width = $(children[0]).width() + $(children[1]).width() + $(children[2]).width() + parseFloat($(children[0]).css('padding-left')) * 6;
	
	if(tag_width > image_width) {
		for(var i = 0; i < 3; i++) {
			for(var j = 0; j < 3; j++) {
				var app = 0;
				if($(children[i]).width() < $(children[j]).width()) {
					app = children[i];
					children[i] = children[j];
					children[j] = app;
				}
			}
		}
	}
}

/**
 * Added ellipses to the end of a keyword if its too long.
 * @param keyword String with the keyword to trim.
 * @returns
 */
function fixKeywordLength(keyword){
	var max_length = 20;
	var new_keyword;
	var filler = "...";
	if (keyword.length > max_length){
		new_keyword = keyword.substring(0, max_length-3);
		new_keyword = new_keyword + filler;
	} else {
		new_keyword = keyword;
	}
	return new_keyword;
}

/**
 * Event listener for loading additional visions in a scenario-specific feed.
 * @returns
 */
$(document).on('click', '#load-more-button', function() {
	var token = $("meta[name='_csrf']").attr("content");
	
	 $.ajax({ type: "GET",
	     url: "/feed-fragment",
	     success : function(fragment)
	     {
	    	 if(typeof Cookies.get("errorMsg") !== 'undefined'){
	    		 $(location).attr('href','/');
		     } else {
		    	 $('#load-more-button').remove();
		         $('#main-container').append(fragment);
		     }
	     }
	 }).done(function(){
		 $("img").one("load", function() {
		 	updateTags(this);
			$(this).addClass("updated");
		 }).each(function() {
		 	if(this.complete && !$(this).hasClass("updated")) {
				$(this).trigger('load')
			}
		 });
	 })
});

/**
 * Handles the menu buttons in the Feed page.
 * On smaller screens it is collapsed by default.
 * @returns
 */
$(document).on('click', '#menu-button', function(){
	if($('#menu-button').children().hasClass('fa-caret-down')) {
		$('#menu-button').children().addClass('fa-caret-up')
		$('#menu-button').children().removeClass('fa-caret-down')
	} else {
		$('#menu-button').children().addClass('fa-caret-down')
		$('#menu-button').children().removeClass('fa-caret-up')
	}
	
	if($('#statisticsButton').css('display') == 'none' || $('#quickGameButton').css('display') == 'none' || $('#scenarioButton').css('display') == 'none' || $('#surveyButtonRIGHT').css('display') == 'none') {
		$('#statisticsButton').attr("style", "display: block !important");
		$('#quickGameButton').attr("style", "display: block !important");
		$('#scenarioButton').attr("style", "display: block !important");
		$('#surveyButtonRIGHT').attr("style", "display: block !important");
		$('#surveyButtonDescriptionRIGHT').attr("style", "display: block !important");
	} else {
		$('#statisticsButton').attr("style", "display: none !important");
		$('#quickGameButton').attr("style", "display: none !important");
		$('#scenarioButton').attr("style", "display: none !important");
		$('#surveyButtonRIGHT').attr("style", "display: none !important");
		$('#surveyButtonDescriptionRIGHT').attr("style", "display: none !important");
	}
});

/**
 * Variables for storing screen sizes.
 */
var newScSize = null, oldScSize = null;

/**
 * Event listener for the resize event. When such event is triggered the menu buttons are rearranged to fit the new screen size.
 * @returns
 */
$(window).resize(function() {
	var w = window.innerWidth;
	
	if(w > 767) {
		if(newScSize != null) {
			oldScSize = newScSize;
		}
		
		newScSize = "Large"
	} else {
		if(newScSize != null) {
			oldScSize = newScSize;
		}
		
		newScSize = "Small"
	}
	
	if(oldScSize == "Small" && newScSize == "Large") {

		$('#statisticsButton').attr("style", "display: block !important");
		$('#quickGameButton').attr("style", "display: block !important");
		$('#scenarioButton').attr("style", "display: block !important");
		$('#surveyButtonLEFT').attr("style", "display: block !important");
		$('#surveyButtonDescriptionLEFT').attr("style", "display: block !important");
		
		$('#surveyButtonRIGHT').attr("style", "display: none !important");
		$('#surveyButtonDescriptionRIGHT').attr("style", "display: none !important");
	} else if(oldScSize == "Large" && newScSize == "Small") {

		if($('#menu-button').children().hasClass('fa-caret-up')) {
			$('#surveyButtonRIGHT').attr("style", "display: block !important");
			$('#surveyButtonDescriptionRIGHT').attr("style", "display: block !important");
			$('#statisticsButton').attr("style", "display: block !important");
			$('#quickGameButton').attr("style", "display: block !important");
			$('#scenarioButton').attr("style", "display: block !important");
		} else {
			$('#surveyButtonRIGHT').attr("style", "display: none !important");
			$('#surveyButtonDescriptionRIGHT').attr("style", "display: none !important");
			$('#statisticsButton').attr("style", "display: none !important");
			$('#quickGameButton').attr("style", "display: none !important");
			$('#scenarioButton').attr("style", "display: none !important");
		}
		
		$('#surveyButtonLEFT').attr("style", "display: none !important");
		$('#surveyButtonDescriptionLEFT').attr("style", "display: none !important");	
	}
});