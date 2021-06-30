/*
	Not used
*/

var element = document.getElementById('graph-div');
		
var layout = {
 	bargap: 0.25,
 	xaxis: {
 		range: [0, 6],
 		tickmode: "linear",
 		tick0: 0,
 	    dtick: 1,
 	    fixedrange: true,
 	    title: "Average Answer"
	},
	yaxis: {
		fixedrange: true,
		title: "Count"
	},
  	title: "Community Quiz Answers",
  	showlegend: true,
  	legend: {
  	    x: 0,
  	    y: 1.1,
  	    traceorder: 'normal'
  	},
  	autosize: true
};

var trace = null;
var plotData = null;

$(document).ready(function () {
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		type: "GET",
		url: "/statisticsStat",
		success: function(data) {
			
			if(typeof Cookies.get("errorMsg") !== 'undefined'){
	    		$(location).attr('href','/');
		    } else {
				results = JSON.parse(data);
				
				trace = {
				    x: results.x,
				    y: results.y,
				    marker: {
				    	color: results.marker
				    },
				    name: 'Your Group',
				    type: 'bar'
			 	};
				
				plotData = [trace];
				
				Plotly.newPlot(element, plotData, layout, {displayModeBar: false});
				
				var user_bef = results.user_bef_feel;
				var user_aft = results.user_aft_feel;
				var comm_bef = results.comm_bef_feel;
				var comm_aft = results.comm_aft_feel;
				
				switch(user_bef){
				case 1:
					$('#super_bad_user_bef').addClass('selected');
					break;
				case 2:
					$('#bad_user_bef').addClass('selected');
					break;
				case 3:
					$('#neutral_user_bef').addClass('selected');
					break;
				case 4:
					$('#good_user_bef').addClass('selected');
					break;
				default:
					$('#good_user_bef').addClass('selected');
				}
				
				switch(user_aft){
				case 1:
					$('#super_bad_user_aft').addClass('selected');
					break;
				case 2:
					$('#bad_user_aft').addClass('selected');
					break;
				case 3:
					$('#neutral_user_aft').addClass('selected');
					break;
				case 4:
					$('#good_user_aft').addClass('selected');
					break;
				default:
					$('#good_user_aft').addClass('selected');
				}
				
				switch(comm_bef){
				case 1:
					$('#super_bad_comm_bef').addClass('selected');
					break;
				case 2:
					$('#bad_comm_bef').addClass('selected');
					break;
				case 3:
					$('#neutral_comm_bef').addClass('selected');
					break;
				case 4:
					$('#good_comm_bef').addClass('selected');
					break;
				default:
					$('#good_comm_bef').addClass('selected');
				}
				
				switch(user_bef){
				case 1:
					$('#super_bad_comm_aft').addClass('selected');
					break;
				case 2:
					$('#bad_comm_aft').addClass('selected');
					break;
				case 3:
					$('#neutral_comm_aft').addClass('selected');
					break;
				case 4:
					$('#good_comm_aft').addClass('selected');
					break;
				default:
					$('#super_good_comm_aft').addClass('selected');
				}
		    }
		}
	}).done(function() {
		$('.legendundefined').addClass("your-group-color");
	});
	
});

$(window).on('resize', function() {
	Plotly.newPlot(element, plotData, layout, {displayModeBar: false, staticPlot: true});
	$('.legendundefined').addClass("your-group-color");
});