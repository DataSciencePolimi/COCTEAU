/*
	Manages the submission of the edit form.
*/
$('#submitEdits').on('click', function(){
	$('#editsForm').submit();
});

/*
	Manages the submission of the revert form.
*/
$('#revertEdits').on('click', function(){
	$('#revertForm').submit();
})