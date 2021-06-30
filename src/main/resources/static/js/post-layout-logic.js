// Custom layout functions to resize divs and center them around the tiles
var grid1, grid2, grid3;

var layout = 1;

/*
	Manages the creation of the 1st layout with three vertical pictures.
*/
function createLayout1() {
	grid1 = new Muuri('.grid-1', {
		dragEnabled: false,
		layoutOnResize: false,
		layout: function (items, gridWidth, gridHeight) {
		    // The layout data object. Muuri will read this data and position the items based on it.
			var layout = {
		    	// The layout item slots (left/top coordinates).
		      	slots: [],
			    // The layout's total width.
			    width: 0,
			    // The layout's total height.
			    height: 0,
			    // Should Muuri set the grid's width after layout
			    setWidth: true,
			    // Should Muuri set the grid's height after layout
			    setHeight: true
		    };

		    // Calculate the slots
		    var item;
		    var m;
		    var x = 0;
		    var y = 0;
		    var w = 0;
		    var h = 0;
		    for (var i = 0; i < items.length; i++) {
				item = items[i];
				x += w;
				m = item.getMargin();
				w = item.getWidth() + m.left + m.right;
				h = item.getHeight() + m.top + m.bottom;
				layout.slots.push(x, y);
		    }

		    // Calculate the layout's total width and height
		    layout.width = x + w;
		    layout.height = y + h;

		    return layout;
			}
	});
}

/*
	Creates and display the 1st layout.
*/
window.addEventListener('load', function(){
	$('.grid-1').css("display", "block");
	createLayout1();
	grid1.refreshItems().layout();
});

var nextLayout = 0;

/*
	Manages the layout buttons associated with the 1st layout.
*/
$(document).on('click', '#layout1_btn', function(){
	// Visual changes
	
	if(layout != 1 && Object.keys(selectedImages).length != 0) {
		$('#change-layout-modal').modal('toggle');
		nextLayout = 1;
	} else if(layout != 1) {
		showGrid1();
	}
	
});

/*
	Manages the modal to ask whether the user wants to change the grid and lose all the chosen pictures.
*/
$(document).on('click', '#change-layout-yes', function() {
	if(nextLayout == 1) {
		showGrid1();
	} else if(nextLayout == 2) {
		showGrid2();
	} else if(nextLayout == 3) {
		showGrid3();
	}
	
	$('#change-layout-modal').modal('toggle');
})

/*
	Manages the layout buttons associated with the 2nd layout.
*/
$(document).on('click', '#layout2_btn', function(){
	
	if(layout != 2 && Object.keys(selectedImages).length != 0) {
		$('#change-layout-modal').modal('toggle');
		nextLayout = 2;
	} else if(layout != 2) {
		showGrid2();
	}
	
});

/*
	Manages the layout buttons associated with the 3rd layout.
*/
$(document).on('click', '#layout3_btn', function(){
	
	if(layout != 3 && Object.keys(selectedImages).length != 0) {
		$('#change-layout-modal').modal('toggle');
		nextLayout = 3;
	} else if(layout != 3) {
		showGrid3();
	}
	
});

/*
	Creates and display the 1st layout.
*/
function showGrid1() {
	$('#layout1_btn').removeClass('btn-outline-secondary');
	$('#layout1_btn').addClass('btn-secondary');
	
	$('#layout2_btn').removeClass('btn-secondary');
	$('#layout2_btn').addClass('btn-outline-secondary');
	$('#layout3_btn').removeClass('btn-secondary');
	$('#layout3_btn').addClass('btn-outline-secondary');
	
	// Load default grid layout
	$('.grid-1').css("display", "block");
	$('.grid-2').css("display", "none");
	$('.grid-3').css("display", "none");
	
	if(layout != 1) {
		selectedImages = {};
		selectedImagesPositions = {};
		checkPictures();
	}
	
	layout = 1;
	$('#layoutToUse').val(layout);
	cleanCanvas(layout);
	grid1.refreshItems().layout();
}

/*
	Creates and display the 2nd layout.
*/
function showGrid2() {
	$('#layout2_btn').removeClass('btn-outline-secondary');
	$('#layout2_btn').addClass('btn-secondary');
	
	$('#layout1_btn').removeClass('btn-secondary');
	$('#layout1_btn').addClass('btn-outline-secondary');
	$('#layout3_btn').removeClass('btn-secondary');
	$('#layout3_btn').addClass('btn-outline-secondary');
	
	// Load second grid layout
	$('.grid-1').css("display", "none");
	$('.grid-2').css("display", "block");
	if (grid2 == null){
		getGrid2();
	}
	$('.grid-3').css("display", "none");
	
	if(layout != 2) {
		selectedImages = {};
		selectedImagesPositions = {};
		checkPictures();
	}
	
	layout = 2;
	cleanCanvas(layout);
	$('#layoutToUse').val(layout);
	grid2.refreshItems().layout();
}

/*
	Creates and display the 3rd layout.
*/
function showGrid3() {
	$('#layout3_btn').removeClass('btn-outline-secondary');
	$('#layout3_btn').addClass('btn-secondary');
	
	$('#layout1_btn').removeClass('btn-secondary');
	$('#layout1_btn').addClass('btn-outline-secondary');
	$('#layout2_btn').removeClass('btn-secondary');
	$('#layout2_btn').addClass('btn-outline-secondary');
	
	// Load third grid layout
	$('.grid-1').css("display", "none");
	$('.grid-2').css("display", "none");
	$('.grid-3').css("display", "block");
	if (grid3 == null){
		getGrid3();
	}
	
	if(layout != 3) {
		selectedImages = {};
		selectedImagesPositions = {};
		checkPictures();
	}
	
	layout = 3;
	cleanCanvas(layout);
	$('#layoutToUse').val(layout);
	grid3.refreshItems().layout();
}

/*
	Remove the content of all canvases.
*/
function cleanCanvas(layout){
	var l = 1;
	var grid = '.grid-';
	var item = '.item-layout';
	while (l <= 3) {
		if (layout != l){
			grid = grid + l;
			item = item + l + ' canvas';
			var slots = $(grid).find(item);
			for (var i = 0; i < slots.length; i++){
				var s_canvas = slots[i];
				var s_ctx = s_canvas.getContext('2d');
				s_ctx.clearRect(0, 0, s_canvas.width, s_canvas.height);
			}
			
			grid = '.grid-';
			item = '.item-layout';
		}
		l++;
	}
	
	// Reset pictures ids to transmit to server
	selectedImages = {};
}

/*
	Manages the creation of the 1st layout with three horizontal pictures.
*/
function getGrid2() {
	grid2 = new Muuri('.grid-2', {
		dragEnabled: false,
		layoutOnResize: false,
		layout: function (items, gridWidth, gridHeight) {
		    // The layout data object. Muuri will read this data and position the items based on it.
		    var layout = {
		    	// The layout item slots (left/top coordinates).
		      	slots: [],
			    // The layout's total width.
			    width: 0,
			    // The layout's total height.
			    height: 0,
			    // Should Muuri set the grid's width after layout
			    setWidth: true,
			    // Should Muuri set the grid's height after layout
			    setHeight: true
		    };
		    
		 	// Calculate the slots
		    var item;
		    var m;
		    var x = 0;
		    var y = 0;
		    var w = 0;
		    var h = 0;
		    for (var i = 0; i < items.length; i++) {
				item = items[i];
				y += h;
				m = item.getMargin();
				w = item.getWidth() + m.left + m.right;
				h = item.getHeight() + m.top + m.bottom;
				layout.slots.push(x, y);
		    }

		    // Calculate the layout's total width and height
		    layout.width = x + w;
		    layout.height = y + h;

		    return layout;
		  }
	});
}

/*
	Manages the creation of the 1st layout with four pictures.
*/
function getGrid3(){
	grid3 = new Muuri('.grid-3', {
		dragEnabled: false,
		layoutOnResize: false,
		layout: function (items, gridWidth, gridHeight) {
		    // The layout data object. Muuri will read this data and position the items based on it.
		    var layout = {
		    	// The layout item slots (left/top coordinates).
		      	slots: [],
			    // The layout's total width.
			    width: 0,
			    // The layout's total height.
			    height: 0,
			    // Should Muuri set the grid's width after layout
			    setWidth: true,
			    // Should Muuri set the grid's height after layout
			    setHeight: true
		    };

		    layout.slots.push(0, 0); // Up-left
		    layout.slots.push(items[0].getWidth(), 0); //Up-right
		    layout.slots.push(0, items[0].getHeight()); //Bottom-left
		    layout.slots.push(items[0].getWidth(), items[0].getHeight()); //Bottom-right
		    
		    // Calculate the layout's total width and height
		    layout.width = items[0].getWidth()*2;
		    layout.height = items[0].getHeight()*2;

		    return layout;
		  }
	});
}