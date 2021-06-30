/*
	Add a question to a Quiz during its creation.
*/
$(document).on("click", "#newQuestion", function() {
	
	if($("#lang").text() == "it") {
		question_text = "Domanda"
		remove_button_text = "Rimuovi Domanda"
	} else if($("#lang").text() == "en") {
		question_text = "Question"
		remove_button_text = "Remove Question"
	}
	
	var count = 1;
	
	$(".question").each(function() {
		count++;
	})
	
	if(count <= 5) {
		$("#questionForm").append('<label class="mt-3" for="question' + count + '"  id="labelquestion' + count + '">' + question_text + ' ' + count + '</label><input type="text" name="question' + count + '" class="form-control question" id="question' + count + '" placeholder="Question ' + count + '">')
	}
	
	if(count == 5 && $("#new-question-button").children().length > 0) {
		$("#new-question-button").html("");
	}
	
	if(count >= 4 && $("#remove-question-button").children().length == 0) {
		$("#remove-question-button").append('<button type="button" class="btn btn-primary col-12 mb-3" id="removeQuestion">' + remove_button_text +'</button>');
	}
})

/*
	Remove a question from a Quiz during its creation.
*/
$(document).on("click", "#removeQuestion", function() {
	
	if($("#lang").text() == "it") {
		new_button_text = "Nuova Domanda"
	} else if($("#lang").text() == "en") {
		new_button_text = "New Question"
	}
	
	var count = 0;
	
	$(".question").each(function() {
		count++;
	})
	
	$('#question' + count).remove();
	$('#labelquestion' + count).remove();
	
	if(count == 4 && $("#remove-question-button").children().length > 0) {
		$("#remove-question-button").html("");
	}
	
	if(count <= 5 && $("#new-question-button").children().length == 0) {
		$("#new-question-button").append('<button type="button" class="btn btn-primary col-12 mb-3" id="newQuestion">' + new_button_text + '</button>');
	}
})