/*
	Manages the tutorial modal of the Scenario. If the page is opened for the first time, the tutorial is displayed.
*/
$(document).ready(function(){
	var firstTime = $('#firstTime').val();
	
	if(firstTime == 0){
		if($('#scenarioModal').hasClass('show')){
			$('#scenarioModal').modal('hide');
		}
		$('#firstTimeInfoModal').modal('toggle');
	}
});

/*
	Manages the Scenario modal displayed when the "Explore" button is pressed.
*/
$('#scenarioModal').on('show.bs.modal', function (event) {
	
	var button = $(event.relatedTarget);
	var scenarioId = button.data('sid');
  	var title = button.data('title');
	var description = button.data('descr');
	var challenge = button.data('chall');
	
	var feel = button.data('feel');
	var quiz = button.data('quiz');
	var played = button.data('played');
	var vision = button.data('vision');
	var sec_feel = button.data('secondfeel');
	
	var modal = $(this);
	modal.find('.modal-title').text(challenge +": "+ title);
	modal.find('.description').text(description);
	
	if(!quiz){
		var form = modal.find('#quiz');
		form.find('#scenarioIdQuiz').val(scenarioId);
		modal.find('.next-page').attr('form', 'quiz');
		
		if($('#langModalMsg').val() == 'it'){
			modal.find('.next-page').text('Gioca');	
		} else {
			modal.find('.next-page').text('Play');
		}
		
	} else if(!feel){
		var form = modal.find('#feelings');
		form.find('#scenarioIdFeel').val(scenarioId);
		modal.find('.next-page').attr('form', 'feelings');
		
		if($('#langModalMsg').val() == 'it'){
			modal.find('.next-page').text('Gioca');	
		} else {
			modal.find('.next-page').text('Play');
		}
	} else if(!vision) {
		var form = modal.find('#vision');
		form.find('#scenarioIdVision').val(scenarioId);
		modal.find('.next-page').attr('form', 'vision');
		
		if($('#langModalMsg').val() == 'it'){
			modal.find('.next-page').text('Crea');	
		} else {
			modal.find('.next-page').text('Create');
		}
	} else if(!played) {
		var form = modal.find('#match');
		form.find('#scenarioIdMatch').val(scenarioId);
		modal.find('.next-page').attr('form', 'match');
		
		if($('#langModalMsg').val() == 'it'){
			modal.find('.next-page').text('Gioca');	
		} else {
			modal.find('.next-page').text('Play');
		}
	} else if(!sec_feel) {
		var form = modal.find('#feelings');
		form.find('#scenarioIdFeel').val(scenarioId);
		modal.find('.next-page').attr('form', 'feelings');
		
		if($('#langModalMsg').val() == 'it'){
			modal.find('.next-page').text('Gioca');	
		} else {
			modal.find('.next-page').text('Play');
		}
	} else {
		var form = modal.find('#feed');
		form.find('#scenarioIdFeed').val(scenarioId);
		modal.find('.next-page').attr('form', 'feed');
		modal.find('.next-page').text('Feed');
	}
});

/*
	Manages the Carousel to choose the Narrative.
*/
$('#scenarioCarousel').on('slid.bs.carousel', function (event) {
			
	var active_narr = $('.active').data('narr');
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		 type: "GET",
	     url: "/getNarrativeScenarios",
	     data: {
	    	 narrative: active_narr
	     },
	     success: function(fragment) {
	    	$('#scenarioList').empty();
	    	$('#scenarioList').append(fragment);
	     },
	})
})