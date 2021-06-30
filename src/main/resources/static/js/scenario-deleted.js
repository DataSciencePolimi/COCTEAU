/*
	Manages the Administrator panel for the Scenario for a deleted Scenario.
*/
$('.scenario-btn').on('click', function(){
	var idScenario = $(this).data('sid');
	var title = $(this).data('title');
	var desc = $(this).data('desc');
	var token = $("meta[name='_csrf']").attr("content");
	
	$('#selected-download').val(idScenario);	
	
	$.ajax({
		type: "GET",
		url: "/getDeletedScenarioDetails",
		data: {"idScenario": idScenario},
	}).done(function(data){
		results = JSON.parse(data);
		
		$('#scenario-title').html(title);
		$('#scenario-description').html(desc);
		if(!results.hasOwnProperty('noQuiz')){
			$('#scenario-statements-title').show();
			var questionsList = "<ul>";
			for(var i = 0; i<results.length; i++) {
				questionsList += "<li>" + results[i].text + "</li><ul>";
				questionsList += "<li>" + results[i].a1;
				questionsList += ", " + results[i].a2;
				questionsList += ", " + results[i].a3;
				questionsList += ", " + results[i].a4;
				questionsList += ", " + results[i].a5 + "</li></ul>";
			}
			questionsList += "</ul>";
			$('#scenario-statements').html(questionsList);	
			$('#scenario-statements').show();
		} else {
			$('#scenario-statements-title').hide();
			$('#scenario-statements').hide();
		}
		
		if(status){
			$('#s-hide').show();
			$('#s-publish').hide();
			$('#s-edit').hide();
		} else {
			$('#s-publish').show();
			$('#s-edit').show();
			$('#s-hide').hide();
		}
		
		$('#scenario-details').show();
		
		$('#quiz-creation-div').html("");
		
		if(results.length < 3) {
			if($("#lang").text() == "it") {
				$('#quiz-creation-div').append('<form action="/quiz-management" method="get" id="quiz-creation-form">' +
						'<input type="hidden" name="selectedScenario" id="selected-quiz" value="' + idScenario + '">' +
						'<input type="submit" class="btn btn-info col-12 my-2" value="Crea Quiz">' +
					'</form>');
			} else if($("#lang").text() == "en"){
				$('#quiz-creation-div').append('<form action="/quiz-management" method="get" id="quiz-creation-form">' +
						'<input type="hidden" name="selectedScenario" id="selected-quiz" value="' + idScenario + '">' +
						'<input type="submit" class="btn btn-info col-12 my-2" value="Create Quiz">' +
					'</form>');
			}
			$("#publish-scenario-div").html("")
		}
		
		$.ajax({
			type: "GET",
			url: "/getScenarioRefCode",
			data: {"idScenario": idScenario},
		}).done(function(data){
			results = JSON.parse(data);
			
			$("#ref-codes-div").html("");
			
			for(var i = 0; i < results.length; i++) {
				$("#ref-codes-div").append('<a href=' + results[i].url + '>' + results[i].url + '</a>');
			}
			
			if(results.length == 0) {
				$("#sc-ref-codes-h5").hide()
			} else {
				$("#sc-ref-codes-h5").show()
			}
		});
		
		$("body,html").animate(
	      {
	        scrollTop: $("#scenario-statements").offset().top
	      },
	      800
	    );
	});
	
});