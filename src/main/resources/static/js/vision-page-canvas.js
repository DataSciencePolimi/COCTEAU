var slot;
var captureSize = 1;
var pageNum = 1;

/*
	Manages the capture size of the picture (1x) and updates the canvas.
*/
$(document).on('click', '#multRadio1x', function() {
	captureSize = 1;
	updateCanvas();
})

/*
	Manages the capture size of the picture (1.5x) and updates the canvas.
*/
$(document).on('click', '#multRadio15x', function() {
	captureSize = 1.5;
	updateCanvas();
})

/*
	Manages the capture size of the picture (2x) and updates the canvas.
*/
$(document).on('click', '#multRadio2x', function() {
	captureSize = 2;
	updateCanvas();
})

/*
	Update the slot selected by the User.
*/
$(document).on('click', '.layout-element', function(event){

	slot = $(this);
	
	$('#image-mask').hide();
	$('#exampleModal').modal('toggle');
});

/*
	Manages the Unsplash search query.
*/
$('#search-button').on('click', function(){
	pageNum = 1;
	$('#m-body').html('');
	$('#load-more-unsplash').hide();
	$('.modal-body').removeClass('images-loaded');
	unsplashAjaxCall();
});

/*
	Manages the pression of the "Enter" button when searching on Unsplash.
*/
$(document).keypress(function (e){
	if(e.which == 13 && $('#search-keyword').is(':focus')){
		$('#search-button').click();
	}
});

/* 
	Perform an additional search to increase the amount of pictures displayed.
*/
$('#load-more-unsplash').on('click', function (){
	$('.modal-body').removeClass('images-loaded');
	pageNum += 1;
	unsplashAjaxCall();
});

var ajaxCallCompleted = false;
var results = null;

/*
	Perform a call to Unsplash.
*/
function unsplashAjaxCall(){
	
	var keyword = $('#search-keyword').val();
	var lang = $('#lang').text();
	var token = $("meta[name='_csrf']").attr("content");
	if (keyword != null && keyword != "") {
		$.ajax({
			type: "GET",
			url: "/getUnsplashPics",
			data: {"keyword": keyword,
				   "pageNum": pageNum,
				   "lang": lang},
			success: function(data) {
				results = JSON.parse(data);
				ajaxCallCompleted = true;
				
				if(results.length != 0) {
					showUnsplashResults();
				}
				
			}
		});
	}
}

var grid = null;

/*
	Refresh the content of the grid containing the visions.
*/
function refreshGrid() {
	$('.img-pick').on('click', drawCanvasPict);
	grid.refreshItems().layout(true, function (){
		$('.modal-body').addClass('images-loaded');
	});
}

/*
	Display the pictures retrieved from Unsplash to allow the User to pick among them to build their Vision.
*/
function showUnsplashResults(){
	
	if($('.modal-body').children('.loading').length == 0) {
		var onload = '<div class="loading">Loading images...</div>';
		$('.modal-body').append(onload);
	}
	
	var divs = '';
	var i = 0;
	var gridItems = [];
	var divItem;
		
	for (; i < results.length - 1; i++){
		divItem = document.createElement('div');
		divs += '<div class="item">';
		divs += '<div class="item-content">';
		
		divs += '<img class="img-fluid img-pick" src="'+results[i].urls.small+'" id="'+results[i].picID+'" onload="refreshGrid()" data-canvas="'+results[i].urls.regular+'">';
		
		divs += '<div class="photo-credits text-center py-1 px-2 ellipsis">Photo by <a href="'+results[i].userInfo.profileURL+'" target="_blank" rel="noopener noreferrer">'+results[i].userInfo.name+'</a></div>';
		divs += '</div></div>';
		divItem.innerHTML = divs;
		gridItems.push(divItem.firstChild);
		divs = '';
	}
	
	if (grid == null){
		grid = new Muuri('.grid', {
			items: gridItems,
			dragEnabled: false,
			layout: {
				fillGaps: true
			}
		});
		grid.on('layoutEnd', function(){
			if(ajaxCallCompleted) {
				$('.photo-credits').show();
				if(results[results.length - 1].remaining > 0) {
					$('#load-more-unsplash').show();
				} else {
					$('#load-more-unsplash').hide();
				}
				ajaxCallCompleted = false;
			}
		})
	} else {
		grid.add(gridItems, {layout: false});
	}
	
	$('.results-container').css('position', 'relative');
	$('.photo-credits').css('position', 'absolute');
	$('.photo-credits').css('bottom', '0px');
	$('.photo-credits').css('width', '100%');
	$('.photo-credits').css('background-color', 'rgba(255,255,255,0.8)');
}

var imgSrc = "";
var imgID = "";
var selectedImages = {};
var selectedImagesPositions = {};
var isDragging = false;
var moveXAmount = 0;
var moveYAmount = 0;
var rectWidth = 0;
var rectHeight = 0;

/*
	Draw the selected picture in the chosen section of the Vision.
*/
function drawCanvasPict(){

	$('#load-more-unsplash').hide();
	$("#multRadio1x").prop("checked", true);
 	$("#multRadio15x").prop("checked", false);
 	$("#multRadio2x").prop("checked", false);
 	$("#m-body").hide();
 	$(".loading").remove();
 	captureSize = 1;
 	
	// Get canvas object (unwrapped from jquery)
	var canvas = $('#mask-canvas').get(0);
	var ctx = canvas.getContext("2d");
	ctx.clearRect(0, 0, canvas.width, canvas.height); // Clean canvas
	
	// Load and draw image
	var img = new Image();
	imgSrc = $(this).data('canvas');
	imgID = $(this).attr('id');
	img.src = imgSrc;
	img.containerHeight = $(this).height(); // Image preview height
	img.containerWidth = $(this).width(); // Image preview width
	img.layoutType = 1;
	img.onload = function(){
		
		canvas.width = this.naturalWidth;
	  	canvas.height = this.naturalHeight;
		
		// Draw on the canvas the 'squeezed' image
		ctx.drawImage(img, 0, 0, img.width, img.height,
						   0, 0, canvas.width, canvas.height);
		
		// TO-DO: use layout type to make different masks
		ctx.globalAlpha = 0.2;
		ctx.fillStyle = "blue";
		
		switch(layout){
			case 1:
				ctx.fillRect(moveXAmount, moveYAmount, 200, 600);
				break;
			case 2:
				ctx.fillRect(moveXAmount, moveYAmount, 600, 200);
				break;
			case 3:
				ctx.fillRect(moveXAmount, moveYAmount, 300, 300);
				break;
		}
		
		ctx.globalAlpha = 1;
		
		$('#multRadio1x').attr('disabled', false);
	 	$('#multRadio15x').attr('disabled', false);
	 	$('#multRadio2x').attr('disabled', false);
		
		switch(layout) {
			case 1:
				var captureSizeAvailable = 2;
				while(200 * captureSizeAvailable > canvas.width && captureSizeAvailable > 1) {
					if(captureSizeAvailable != 1.5) {
						$('#multRadio' + captureSizeAvailable + 'x').attr('disabled', true);
					} else {
						$('#multRadio15x').attr('disabled', true);
					}
					captureSizeAvailable -= 0.5;
				}
				
				captureSizeAvailable = 2;
				while(600 * captureSizeAvailable > canvas.height && captureSizeAvailable > 1) {
					if(captureSizeAvailable != 1.5) {
						$('#multRadio' + captureSizeAvailable + 'x').attr('disabled', true);
					} else {
						$('#multRadio15x').attr('disabled', true);
					}
					captureSizeAvailable -= 0.5;
				}
				
				break;
			case 2:
				var captureSizeAvailable = 2;
				while(600 * captureSizeAvailable > canvas.width && captureSizeAvailable > 1) {
					if(captureSizeAvailable != 1.5) {
						$('#multRadio' + captureSizeAvailable + 'x').attr('disabled', true);
					} else {
						$('#multRadio15x').attr('disabled', true);
					}
					captureSizeAvailable -= 0.5;
				}
				
				captureSizeAvailable = 2;
				while(200 * captureSizeAvailable > canvas.height && captureSizeAvailable > 1) {
					if(captureSizeAvailable != 1.5) {
						$('#multRadio' + captureSizeAvailable + 'x').attr('disabled', true);
					} else {
						$('#multRadio15x').attr('disabled', true);
					}
					captureSizeAvailable -= 0.5;
				}
				
				break;
			case 3:
				var captureSizeAvailable = 2;
				while(300 * captureSizeAvailable > canvas.width && captureSizeAvailable > 1) {
					if(captureSizeAvailable != 1.5) {
						$('#multRadio' + captureSizeAvailable + 'x').attr('disabled', true);
					} else {
						$('#multRadio15x').attr('disabled', true);
					}
					captureSizeAvailable -= 0.5;
				}
				
				captureSizeAvailable = 2;
				while(300 * captureSizeAvailable > canvas.height && captureSizeAvailable > 1) {
					if(captureSizeAvailable != 1.5) {
						$('#multRadio' + captureSizeAvailable + 'x').attr('disabled', true);
					} else {
						$('#multRadio15x').attr('disabled', true);
					}
					captureSizeAvailable -= 0.5;
				}
				
				break;
		}
	}
	
	// Show mask and scroll down to it
	$(".search-btn").show();
	$('#image-mask').show();
	$('.updatePic').show();
	
	$('#exampleModal').animate({
		scrollTop: $('#image-modal-footer').offset().top
	}, 1);
}

/*
	Update the size of the area used to clip the picture.
*/
function updateCanvas() {
	var canvas = $('#mask-canvas').get(0);
	var ctx = canvas.getContext("2d");
	
	// Load and draw image
	var img = new Image();
	img.src = imgSrc;
	img.containerHeight = $(this).height(); // Image preview height
	img.containerWidth = $(this).width(); // Image preview width
	img.layoutType = 1;
	img.onload = function(){
		
		// Draw on the canvas the 'squeezed' image
		ctx.drawImage(img, 0, 0, img.width, img.height,
						   0, 0, canvas.width, canvas.height);
		// TO-DO: use layout type to make different masks
		ctx.globalAlpha = 0.2;
		ctx.fillStyle = "blue";
		
		rectHeight = 0;
		rectWidth = 0;
		
		switch(layout){
			case 1:
				rectWidth = 200;
				rectHeight = 600;
				break;
			case 2:
				rectWidth = 600;
				rectHeight = 200;
				break;
			case 3:
				rectWidth = 300;
				rectHeight = 300;
				break;
		}
		
		rectWidth = rectWidth * captureSize;
		rectHeight = rectHeight * captureSize;
		
		if(moveXAmount < rectWidth/2)
		{
			moveXAmount = rectWidth/2;
		}
		
		if(moveXAmount > canvas.width - 1/2 * rectWidth) {
			moveXAmount = canvas.width - 1/2 * rectWidth;
		}
		
		if(moveYAmount < rectHeight/2) {
			moveYAmount = rectHeight/2;
		}
		
		if(moveYAmount > canvas.height - 1/2 * rectHeight) {
			moveYAmount = canvas.height - 1/2 * rectHeight;
		}
		
		ctx.fillRect(moveXAmount - rectWidth/2, moveYAmount - rectHeight/2, rectWidth, rectHeight);
		ctx.globalAlpha = 1;
	}
}

/*
	Update the canvas when the User click the button to pick the picture.
*/
$(document).on('click', '.updatePic', function(){
	
	var slotCanvas = $(slot).find('.img-content canvas').get(0);
	var slotCtx = slotCanvas.getContext("2d");
	
	slotCanvas.height = $(slot).height();
	slotCanvas.width = $(slot).width();
	
	var img = new Image();
	img.src = imgSrc;
	
	img.onload = function() {
		
		slotCtx.drawImage(img, moveXAmount - rectWidth/2, moveYAmount - rectHeight/2, slotCanvas.width * 2 * captureSize, slotCanvas.height * 2 * captureSize,
							   0, 0, slotCanvas.width, slotCanvas.height);
	}
	
	$(slot).find('.img-content canvas').data('path', imgSrc);
	// Update selected images
	updateSelectedImages(slot);
	checkPictures();
	
	$('.updatePic').hide();
	$('#exampleModal').modal('toggle');
});

/*
	Function used to update the content of one of the frames of the Vision.
*/
function updateSelectedImages(slot){
	var slotCanvas = $(slot).find('.img-content canvas');
	var pos = slotCanvas.data('pos');
	
	var canvas = $('#mask-canvas').get(0);
	var ctxCan = canvas.getContext("2d");
	
	selectedImages[pos] = imgID;
	selectedImagesPositions[pos] = String(moveXAmount - rectWidth/2) + ":" + String(moveYAmount - rectHeight/2) + ":" + String(captureSize);
	
	$('#picturesList').val(convertToString(selectedImages, selectedImagesPositions));
}

/*
	Prepares the string to be forwarded to the back end.
*/
function convertToString(imgList, selectedImagesPositions){
	var s = "";
	
	for (var idx in imgList) {
		s = s + String(idx) + ':' + String(imgList[idx]) + ':' + selectedImagesPositions[idx] + ';';
	}
	
	return s;
}

/*
	Check if all the pictures in the Vision are set. If not, the submission keys are disabled.
*/
function checkPictures(){
// Checking if all the pictures are set
	var expectedImages = 0;
	
	if (layout == 1 || layout == 2){
		expectedImages = 3;
	} else {
		expectedImages = 4;
	}
	
	if (allKeywordsSet){
		for (var i=0; i<expectedImages; i++){
			if (selectedImages[i] == null){
				$('#submitKey').prop('disabled', true);
				break;
			} else {
				$('#submitKey').prop('disabled', false);
			}
		}	
	}
}

/*
	Manages whether the User is moving the selection area in the picture.
*/
$(document).on("mousedown", "#mask-canvas", function(){
    isDragging = true;
});

/*
	Manages whether the User is moving the selection area in the picture.
*/
$(window).on("mouseup", function(){
    isDragging = false;
});

/*
	Manages the updates and the position of the selection area in the picture.
*/
$(document).on("mousemove touchmove", "#mask-canvas", function(event) {
	
	var event = window.event;

	var canvas = $('#mask-canvas').get(0);
	
	if(event.type === "touchmove") {
		moveXAmount = (event.touches[0].pageX - $("#mask-canvas").offset().left) * (canvas.width / $('#mask-canvas').width());
        moveYAmount = (event.touches[0].pageY - $("#mask-canvas").offset().top) * (canvas.height / $('#mask-canvas').height());
       	updateCanvas();
	} else if(isDragging == true) {
		moveXAmount = (event.clientX - $("#mask-canvas").offset().left) * (canvas.width / $('#mask-canvas').width());
        moveYAmount = (event.clientY - $("#mask-canvas").offset().top) * (canvas.height / $('#mask-canvas').height());
       	updateCanvas();
    }
});

/*
	Reset the modal on click.
*/
$(document).on('click', '.search-btn', function(){
	resetModal();
})

/*
	Reset the modal when it became hidden.
*/
$(document).on('hidden.bs.modal', '#exampleModal', function () {
	resetModal();
});


/*
	The function that resets the modal.
*/
function resetModal() {
	moveXAmount = 0;
 	moveYAmount = 0;
 	
 	$('#multRadio1x').attr('disabled', false);
 	$('#multRadio15x').attr('disabled', false);
 	$('#multRadio2x').attr('disabled', false);
 	
 	$("#multRadio1x").prop("checked", true);
 	$("#multRadio15x").prop("checked", false);
 	$("#multRadio2x").prop("checked", false);
 	captureSize = 1;
 	
 	$("#m-body").height(0);
 	$("#m-body").show();
	$('.modal-body').removeClass('images-loaded');
	
	$('#search-div').attr('style','display: block !important');
	$('.modal-body').attr('style','padding: 16px !important');
 	
 	$(".search-btn").hide();
	$('#image-mask').hide();
	$('.updatePic').hide();
	
	$('#m-body').html('');
	$(".loading").remove();
	$('#search-keyword').val('');
	$('#load-more-unsplash').hide();
	pageNum = 1;
	grid = null;
}

/*
	Hides the found pictures when one of them is picked.
*/
$(document).on('click', '.img-pick', function() {
	$('#search-div').attr('style','display: none !important');
	$('.modal-body').attr('style','padding: 8px !important');
})

/*
	Display the found pictures when the search button is pressed.
*/
$(document).on('click', '.search-btn', function() {
	$('#search-div').attr('style','display: block !important');
	$('.modal-body').attr('style','padding: 16px !important');
})