(function($) {

var lang = $('#scenario-quiz').data('lang');
var starT = {1:"Strongly disagree",2:"Disagree",3:"Impartial",4:"Agree",5:"Strongly agree"};
var starC = {.5:"Half Star",1:"Strongly disagree",1.5:"One & Half Star",2:"Disagree",2.5:"Two & Half Stars",3:"Impartial",3.5:"Three & Half Stars",4:"Agree",4.5:"Four & Half Stars",5:"Strongly agree"};
var cC = "Not Rated";

if(lang == "it") {
	starT = {1:"In forte disaccordo",2:"In disaccordo",3:"Imparziale",4:"D'accordo",5:"Completamente d'accordo"};
	starC = {.5:"Half Star",1:"In forte disaccordo",1.5:"One & Half Star",2:"In disaccordo",2.5:"Two & Half Stars",3:"Imparziale",3.5:"Three & Half Stars",4:"D'accordo",4.5:"Four & Half Stars",5:"Completamente d'accordo"};
	cC = "Senza Voto";
}

// override stars with font awesome - ref js/components/star-rating.js
!function(t){"use strict";"function"==typeof define&&define.amd?define(["jquery"],t):"object"==typeof module&&module.exports?module.exports=t(require("jquery")):t(window.jQuery)}(function(t){"use strict";t.fn.ratingLocales={},t.fn.ratingThemes={};var e,a;e={NAMESPACE:".rating",DEFAULT_MIN:0,DEFAULT_MAX:5,DEFAULT_STEP:.5,isEmpty:function(e,a){return null===e||void 0===e||0===e.length||a&&""===t.trim(e)},getCss:function(t,e){return t?" "+e:""},addCss:function(t,e){t.removeClass(e).addClass(e)},getDecimalPlaces:function(t){var e=(""+t).match(/(?:\.(\d+))?(?:[eE]([+-]?\d+))?$/);return e?Math.max(0,(e[1]?e[1].length:0)-(e[2]?+e[2]:0)):0},applyPrecision:function(t,e){return parseFloat(t.toFixed(e))},handler:function(a,n,i,r,s){var o=s?n:n.split(" ").join(e.NAMESPACE+" ")+e.NAMESPACE;r||a.off(o),a.on(o,function(e){var a=t.proxy(i,self);a(e)})}},a=function(e,a){var n=this;n.$element=t(e),n._init(a)},a.prototype={constructor:a,_parseAttr:function(t,a){var n,i,r,s,o=this,l=o.$element,c=l.attr("type");if("range"===c||"number"===c){switch(i=a[t]||l.data(t)||l.attr(t),t){case"min":r=e.DEFAULT_MIN;break;case"max":r=e.DEFAULT_MAX;break;default:r=e.DEFAULT_STEP}n=e.isEmpty(i)?r:i,s=parseFloat(n)}else s=parseFloat(a[t]);return isNaN(s)?r:s},_parseValue:function(t){var e=this,a=parseFloat(t);return isNaN(a)&&(a=e.clearValue),!e.zeroAsNull||0!==a&&"0"!==a?a:null},_setDefault:function(t,a){var n=this;e.isEmpty(n[t])&&(n[t]=a)},_initSlider:function(t){var a=this,n=a.$element.val();a.initialValue=e.isEmpty(n)?0:n,a._setDefault("min",a._parseAttr("min",t)),a._setDefault("max",a._parseAttr("max",t)),a._setDefault("step",a._parseAttr("step",t)),(isNaN(a.min)||e.isEmpty(a.min))&&(a.min=e.DEFAULT_MIN),(isNaN(a.max)||e.isEmpty(a.max))&&(a.max=e.DEFAULT_MAX),(isNaN(a.step)||e.isEmpty(a.step)||0===a.step)&&(a.step=e.DEFAULT_STEP),a.diff=a.max-a.min,a._setDefault("minThreshold",a.min),a.minThreshold<a.min&&(a.minThreshold=a.min),a.minThreshold>a.max&&(a.minThreshold=a.max)},_initHighlight:function(t){var e,a=this,n=a._getCaption();t||(t=a.$element.val()),e=a.getWidthFromValue(t)+"%",a.$filledStars.width(e),a.cache={caption:n,width:e,val:t}},_getContainerCss:function(){var t=this;return"rating-container"+e.getCss(t.theme,"theme-"+t.theme)+e.getCss(t.rtl,"rating-rtl")+e.getCss(t.size,"rating-"+t.size)+e.getCss(t.animate,"rating-animate")+e.getCss(t.disabled||t.readonly,"rating-disabled")+e.getCss(t.containerClass,t.containerClass)+(t.displayOnly?" is-display-only":"")},_checkDisabled:function(){var t=this,e=t.$element,a=t.options;t.disabled=void 0===a.disabled?e.attr("disabled")||!1:a.disabled,t.readonly=void 0===a.readonly?e.attr("readonly")||!1:a.readonly,t.inactive=t.disabled||t.readonly,e.attr({disabled:t.disabled,readonly:t.readonly})},_addContent:function(t,e){var a=this,n=a.$container,i="clear"===t;return a.rtl?i?n.append(e):n.prepend(e):i?n.prepend(e):n.append(e)},_generateRating:function(){var a,n,i,r=this,s=r.$element;n=r.$container=t(document.createElement("div")).insertBefore(s),e.addCss(n,r._getContainerCss()),r.$rating=a=t(document.createElement("div")).attr("class","rating-stars").appendTo(n).append(r._getStars("empty")).append(r._getStars("filled")),r.keyboardEnabled&&r.$rating.attr("tabindex",r.tabindex),r.$emptyStars=a.find(".empty-stars"),r.$filledStars=a.find(".filled-stars"),r._renderCaption(),r._renderClear(),r._initHighlight(),r._initStarTitles();r.rtl&&(i=Math.max(r.$emptyStars.outerWidth(),r.$filledStars.outerWidth()),r.$emptyStars.width(i)),n.insertBefore(s)},_getCaption:function(){var t=this;return t.$caption&&t.$caption.length?t.$caption.html():t.defaultCaption},_setCaption:function(t){var e=this;e.$caption&&e.$caption.length&&e.$caption.html(t)},_renderCaption:function(){var a,n=this,i=n.$element.val(),r=n.captionElement?t(n.captionElement):"";if(n.showCaption){if(a=n.fetchCaption(i),r&&r.length)return e.addCss(r,"caption"),r.html(a),void(n.$caption=r);n._addContent("caption",'<div class="caption">'+a+"</div>"),n.$caption=n.$container.find(".caption")}},_renderClear:function(){var a,n=this,i=n.clearElement?t(n.clearElement):"";if(n.showClear){if(a=n._getClearClass(),i.length)return e.addCss(i,a),i.attr({title:n.clearButtonTitle}).html(n.clearButton),void(n.$clear=i);n._addContent("clear",'<div class="'+a+'" title="'+n.clearButtonTitle+'">'+n.clearButton+"</div>"),n.$clear=n.$container.find("."+n.clearButtonBaseClass)}},_getClearClass:function(){var t=this;return t.clearButtonBaseClass+" "+(t.inactive?"":t.clearButtonActiveClass)},_toggleHover:function(t){var e,a,n,i=this;t&&(i.hoverChangeStars&&(e=i.getWidthFromValue(i.clearValue),a=t.val<=i.clearValue?e+"%":t.width,i.$filledStars.css("width",a)),i.hoverChangeCaption&&(n=t.val<=i.clearValue?i.fetchCaption(i.clearValue):t.caption,n&&i._setCaption(n+"")))},_init:function(a){var n,i=this,r=i.$element.attr("tabindex",-1).addClass("rating-input"),s=i.minThreshold;return i.options=a,t.each(a,function(t,e){i[t]=e}),(i.rtl||"rtl"===r.attr("dir"))&&(i.rtl=!0,r.attr("dir","rtl")),i.starClicked=!1,i.clearClicked=!1,i._initSlider(a),i._checkDisabled(),i.displayOnly&&(i.inactive=!0,i.showClear=!1,i.hoverEnabled=!1,i.hoverChangeCaption=!1,i.hoverChangeStars=!1),i._generateRating(),i._initEvents(),i._listen(),!e.isEmpty(s)&&(e.isEmpty(r.val())||r.val()<s)&&r.val(s),n=i._parseValue(r.val()),r.val(n),r.removeClass("rating-loading")},_initCaptionTitle:function(){var e,a=this;e=a.fetchCaption(a.$element.val()),a.$rating.attr("title",t(e).text())},_trigChange:function(t){var e=this;e._initStarTitles(),e.$element.trigger("change").trigger("rating:change",t)},_initEvents:function(){var t=this;t.events={_getTouchPosition:function(a){var n=e.isEmpty(a.pageX)?a.originalEvent.touches[0].pageX:a.pageX;return n-t.$rating.offset().left},_listenClick:function(t,e){return t.stopPropagation(),t.preventDefault(),t.handled===!0?!1:(e(t),void(t.handled=!0))},_noMouseAction:function(e){return!t.mouseEnabled||!t.hoverEnabled||t.inactive||e&&e.isDefaultPrevented()},initTouch:function(a){var n,i,r,s,o,l,c,u,d=t.clearValue||0,h="ontouchstart"in window||window.DocumentTouch&&document instanceof window.DocumentTouch;h&&!t.inactive&&(n=a.originalEvent,i=e.isEmpty(n.touches)?n.changedTouches:n.touches,r=t.events._getTouchPosition(i[0]),"touchend"===a.type?(t._setStars(r),u=[t.$element.val(),t._getCaption()],t._trigChange(u),t.starClicked=!0):(s=t.calculate(r),o=s.val<=d?t.fetchCaption(d):s.caption,l=t.getWidthFromValue(d),c=s.val<=d?l+"%":s.width,t._setCaption(o),t.$filledStars.css("width",c)))},starClick:function(e){var a,n;t.events._listenClick(e,function(e){return t.inactive?!1:(a=t.events._getTouchPosition(e),t._setStars(a),n=[t.$element.val(),t._getCaption()],t._trigChange(n),void(t.starClicked=!0))})},clearClick:function(e){t.events._listenClick(e,function(){t.inactive||(t.clear(),t.clearClicked=!0)})},starMouseMove:function(e){var a,n;t.events._noMouseAction(e)||(t.starClicked=!1,a=t.events._getTouchPosition(e),n=t.calculate(a),t._toggleHover(n),t.$element.trigger("rating:hover",[n.val,n.caption,"stars"]))},starMouseLeave:function(e){var a;t.events._noMouseAction(e)||t.starClicked||(a=t.cache,t._toggleHover(a),t.$element.trigger("rating:hoverleave",["stars"]))},clearMouseMove:function(e){var a,n,i,r;!t.events._noMouseAction(e)&&t.hoverOnClear&&(t.clearClicked=!1,a='<span class="'+t.clearCaptionClass+'">'+t.clearCaption+"</span>",n=t.clearValue,i=t.getWidthFromValue(n)||0,r={caption:a,width:i,val:n},t._toggleHover(r),t.$element.trigger("rating:hover",[n,a,"clear"]))},clearMouseLeave:function(e){var a;t.events._noMouseAction(e)||t.clearClicked||!t.hoverOnClear||(a=t.cache,t._toggleHover(a),t.$element.trigger("rating:hoverleave",["clear"]))},resetForm:function(e){e&&e.isDefaultPrevented()||t.inactive||t.reset()},focus:function(){t.$rating.focus(),t.$element.trigger("rating:focus")},blur:function(){t.$element.trigger("rating:blur")},keydown:function(a){if(!t.inactive&&t.keyboardEnabled){var n=t.$element,i=n.val(),r=!1,s=parseFloat(t.step),o=e.getDecimalPlaces(s),l=t.rtl?37:39,c=t.rtl?39:37,u=i?parseFloat(i):0,d=parseInt(a.which||a.keyCode||0,10);d===l&&u<t.max&&(u+=s,r=!0),d===c&&u>t.minThreshold&&(u-=s,r=!0),r&&(u=e.applyPrecision(u,o),n.val(u),t._trigChange([u,t._getCaption()]),t.showStars(n.val()),t.$rating.focus()),37!==d&&39!==d||n.trigger("rating:keydown",[u,t._getCaption()])}}}},_listen:function(){var a=this,n=a.$element,i=n.closest("form"),r=a.$rating,s=a.$clear,o=a.events,l=e.NAMESPACE,c="mousenter"+l+" mouseleave"+l,u=a.$rating.find(".star");return e.handler(r,"touchstart touchmove touchend",o.initTouch),e.handler(r,"click touchstart",o.starClick),e.handler(r,"mousemove",o.starMouseMove),e.handler(r,"mouseleave",o.starMouseLeave),e.handler(r,"keydown",o.keydown),e.handler(r,"blur",o.blur),a.showClear&&s.length&&(e.handler(s,"click touchstart",o.clearClick),e.handler(s,"mousemove",o.clearMouseMove),e.handler(s,"mouseleave",o.clearMouseLeave)),i.length&&e.handler(i,"reset",o.resetForm,!0),u.off(c).on(c,function(e){var n=t(this),i=n.index(),r=n.parent().attr("class").slice(0,-1);a.$element.trigger("rating:"+e.type,[i+1,r,n])}),e.handler(a.$container,"click",o.focus),n},_getStars:function(t){var e,a=this,n='<span class="'+t+'-stars">';for(e=1;e<=a.stars;e++)n+='<span class="star">'+a[t+"Star"]+"</span>";return n+"</span>"},_initStarTitles:function(e){var a=this;if(a.showCaptionAsTitle)return void a._initCaptionTitle();var n,i=a.starTitles;n=function(a){var n=1;a.each(function(){var a,r,s=t(this);"function"==typeof i?(a=n===Math.floor(e)?e:n,r=i(a)):r=i[n],r&&s.attr({title:r}),n++})},n(a.$emptyStars.find(".star")),n(a.$filledStars.find(".star"))},_setStars:function(t){var e=this,a=arguments.length?e.calculate(t):e.calculate(),n=e.$element,i=e._parseValue(a.val);return n.val(i),e.$filledStars.css("width",a.width),e._setCaption(a.caption),e.cache=a,e._initStarTitles(i),n},showStars:function(t){var e=this,a=e._parseValue(t);return e.$element.val(a),e._setStars()},calculate:function(t){var a=this,n=e.isEmpty(a.$element.val())?0:a.$element.val(),i=arguments.length?a.getValueFromPosition(t):n,r=a.fetchCaption(i),s=a.getWidthFromValue(i);return s+="%",{caption:r,width:s,val:i}},getValueFromPosition:function(t){var a,n,i=this,r=e.getDecimalPlaces(i.step),s=i.$rating.width();return n=i.diff*t/(s*i.step),n=i.rtl?Math.floor(n):Math.ceil(n),a=e.applyPrecision(parseFloat(i.min+n*i.step),r),a=Math.max(Math.min(a,i.max),i.minThreshold),i.rtl?i.max-a:a},getWidthFromValue:function(t){var e,a,n=this,i=n.min,r=n.max,s=n.$emptyStars;return!t||t<=n.min||i===r?0:(t=Math.max(t,n.minThreshold),a=s.outerWidth(),e=a?s.width()/a:1,t>=r?100:(t-i)*e*100/(r-i))},fetchCaption:function(t){var a,n,i,r,s,o=this,l=parseFloat(t)||o.clearValue,c=o.starCaptions,u=o.starCaptionClasses,d=o.getWidthFromValue(l);return l&&l!==o.clearValue&&(l=e.applyPrecision(l,e.getDecimalPlaces(o.step))),r="function"==typeof u?u(l,d):u[l],i="function"==typeof c?c(l,d):c[l],n=e.isEmpty(i)?o.defaultCaption.replace(/\{rating}/g,l):i,a=e.isEmpty(r)?o.clearCaptionClass:r,s=l===o.clearValue?o.clearCaption:n,'<span class="'+a+'">'+s+"</span>"},destroy:function(){var a=this,n=a.$element;return e.isEmpty(a.$container)||a.$container.before(n).remove(),t.removeData(n.get(0)),n.off("rating").removeClass("rating rating-input")},create:function(t){var e=this,a=t||e.options||{};return e.destroy().rating(a)},clear:function(){var t=this,e='<span class="'+t.clearCaptionClass+'">'+t.clearCaption+"</span>";return t.inactive||t._setCaption(e),t.showStars(t.clearValue).trigger("change").trigger("rating:clear")},reset:function(){var t=this;return t.showStars(t.initialValue).trigger("rating:reset")},update:function(t){var e=this;return arguments.length?e.showStars(t):e.$element},refresh:function(e){var a=this,n=a.$element;return e?a.destroy().rating(t.extend(!0,a.options,e)).trigger("rating:refresh"):n}},t.fn.rating=function(n){var i=Array.apply(null,arguments),r=[];switch(i.shift(),this.each(function(){var s,o=t(this),l=o.data("rating"),c="object"==typeof n&&n,u=c.theme||o.data("theme"),d=c.language||o.data("language")||"en",h={},p={};l||(u&&(h=t.fn.ratingThemes[u]||{}),"en"===d||e.isEmpty(t.fn.ratingLocales[d])||(p=t.fn.ratingLocales[d]),s=t.extend(!0,{},t.fn.rating.defaults,h,t.fn.ratingLocales.en,p,c,o.data()),l=new a(this,s),o.data("rating",l)),"string"==typeof n&&r.push(l[n].apply(l,i))}),r.length){case 0:return this;case 1:return void 0===r[0]?this:r[0];default:return r}},t.fn.rating.defaults={theme:"krajee-svg",language:"en",stars:5,tabindex:0,keyboardEnabled:!0,mouseEnabled:!0,containerClass:"",size:"md",animate:!0,displayOnly:!1,rtl:!1,showClear:!0,showCaption:!0,starCaptionClasses:{.5:"caption-badge caption-danger",1:"caption-badge caption-danger",1.5:"caption-badge caption-warning",2:"caption-badge caption-warning",2.5:"caption-badge caption-info",3:"caption-badge caption-info",3.5:"caption-badge caption-primary",4:"caption-badge caption-primary",4.5:"caption-badge caption-success",5:"caption-badge caption-success"},filledStar:'<span class="fa-solid fa-star"></span>',emptyStar:'<span class="fa-regular fa-star"></span>',clearButton:'<span class="icon-minus-sign"></span>',clearButtonBaseClass:"clear-rating",clearButtonActiveClass:"clear-rating-active",clearCaptionClass:"caption-badge caption-secondary",clearValue:null,captionElement:null,clearElement:null,showCaptionAsTitle:!1,hoverEnabled:!0,hoverChangeCaption:!0,hoverChangeStars:!0,hoverOnClear:!0,zeroAsNull:!0},t.fn.ratingLocales.en={defaultCaption:"{rating} Stars",starCaptions:starC,starTitles:starT,clearButtonTitle:"Clear",clearCaption:cC},t.fn.rating.Constructor=a,t(document).ready(function(){var e=t("input.rating");e.length&&e.removeClass("rating-loading").addClass("rating-loading").rating()})});

$(document).ready(function() {
	if ($('.grid-filter[data-container="#scenarios"]').length) {
		$('#scenarios').isotope({ filter: $('.grid-filter[data-container="#scenarios"] > li.activeFilter > a').attr('data-filter') });
	}
	
	if ($('.grid-filter[data-container="#best-users"]').length) {
		$('#best-users').isotope({ filter: $('.grid-filter[data-container="#best-users"] > li.activeFilter > a').attr('data-filter') });
	}
	
	if ($('.grid-filter[data-container="#best-visions"]').length) {
		$('#best-visions').isotope({ filter: $('.grid-filter[data-container="#best-visions"] > li.activeFilter > a').attr('data-filter') });
	}
	
	if ($('.grid-filter[data-container="#leaderboard"]').length) {
		$('#leaderboard').isotope({ filter: $('.grid-filter[data-container="#leaderboard"] > li.activeFilter > a').attr('data-filter') });
	}
	
	if ($('.grid-filter[data-container="#trends"]').length) {
		$('#trends').isotope({ filter: $('.grid-filter[data-container="#trends"] > li.activeFilter > a').attr('data-filter') });
	}
	
	$('#scenario-tabs .scenario-post-vision').click(function() {
	  	$('#scenario-tabs .sticky-tabs .tab-nav li a[href="#scenario-your-vision"]').click();
	})
	
	$('#scenario-tabs .challenge-vision').click(function() {
	  	$('#scenario-tabs .sticky-tabs .tab-nav li a[href="#scenario-challenge"]').click();
	})
	
	$('.challenge-vision').click(function() {
		var playable = $('#scenario-challenge').data('playable');
		
		if(playable) {
			var id_vision = $(this).attr('id').substring(17);
			
			$('#challenge-vision-id').val(id_vision);		
			$('#challenge-picture').attr("src", $('#vision-picture-' + id_vision).attr('src'));
			
			$('#challenge-keyword1').text($('#vision-keyword1-' + id_vision).text());
			$('#challenge-keyword2').text($('#vision-keyword2-' + id_vision).text());
			$('#challenge-keyword3').text($('#vision-keyword3-' + id_vision).text());
			$('#challenge-keyword1').prop('title', $('#vision-keyword1-' + id_vision).text());
			$('#challenge-keyword2').prop('title', $('#vision-keyword2-' + id_vision).text());
			$('#challenge-keyword3').prop('title', $('#vision-keyword3-' + id_vision).text());
			
			$('#challenge-description').text($('#vision-picture-' + id_vision).data('desc'));
			
			var wheel = document.querySelector('#wheel-of-emotions').getBBox();
			delta_y = $('#challenge-picture').height() - wheel.y;
			
			var pos_x = wheel.x - 80;
			var pos_y = wheel.y - delta_y/5;
			var width = wheel.width + 160;
			var height = wheel.height + delta_y;
			
			if(pos_x < 0 || pos_y < 0) {
				pos_x = wheel.x - 80;
				pos_y = wheel.y + 20;
				width = wheel.width + 160;
				height = wheel.height - 40;
			}
			
			$("#wheel-svg").attr("viewBox", pos_x + " " + pos_y + " " + width + " " + height);
		}
	})
	
	$('a[href="#scenario-your-vision"]').click(function() {
		var wheel = document.querySelector('#wheel-of-emotions-vis').getBBox();
		
		pos_x = wheel.x - 80;
		pos_y = wheel.y - 20;
		width = wheel.width + 160;
		height = wheel.height + 40;
		
		$("#wheel-svg-vis").attr("viewBox", pos_x + " " + pos_y + " " + width + " " + height);
	})
	
	$('a[href="#scenario-challenge"]').click(function() {
		var playable = $('#scenario-challenge').data('playable');
		
		if(playable) {
			var wheel = document.querySelector('#wheel-of-emotions').getBBox();
			delta_y = $('#challenge-picture').height() - wheel.y;
			
			console.log($('#challenge-picture').height());
			console.log(wheel.y);
			
			var pos_x = wheel.x - 80;
			var pos_y = wheel.y - delta_y/5;
			var width = wheel.width + 160;
			var height = wheel.height + delta_y;
			
			if(pos_x < 0 || pos_y < 0) {
				pos_x = wheel.x - 80;
				pos_y = wheel.y + 20;
				width = wheel.width + 160;
				height = wheel.height - 40;
			}
			
			$("#wheel-svg").attr("viewBox", pos_x + " " + pos_y + " " + width + " " + height);
		}
	});
	
	$('#scenario-tabs .sticky-tabs .tab-nav li').off( 'click' ).on( 'click', function() {
		$('body,html').stop(true).animate({
			'scrollTop': 0
		}, 300, 'easeOutQuad' );
		if ($(this).find('a').attr('href') == '#scenario-feed') {
			$("#scenarios").isotope( 'layout' );
		}
		return false;
	});
	
	$('.vote').on('click', function(){
		if($('.vote').children('path').hasClass('blink')){
			$('.vote').children('path').removeClass('blink');
		}
	
		$(this).children('path').addClass('blink');
		var vote = $(this).data('vote');
	
		$('#challenge-answer').val(vote);
		$('#challenge-submit-button').prop("disabled", false);
	});
	
	$('#profilePicture').click(function() {
		$('#newProfilePicture').trigger('click');
	})
	
	$('[id^=back-avatar-user-]').click(function() {
		$('#profile-user-' + $(this).attr('id').substr(17)).submit();
	})
	
	$('[id^=submit-scenario-]').click(function() {
		$('#form-scenario-' + $(this).attr('id').substr(16)).submit();
	})
	
	$(document).on('change', '#newProfilePicture', function(e) {
		e.preventDefault();
		var token = $("meta[name='_csrf']").attr("content");
		if($('#newProfilePicture').get(0).files.length > 0) {
			
			var form = $('#updateProfilePict')[0]; 
			var formData = new FormData(form);
			
			$.ajax({
				 type: "POST",
			     url: "/updateProfilePicture",
			     contentType: false,
			     processData: false,
			     data: formData,
			     success : function(fragment)
			     {
			    	 location.reload();
			     }
			 })
		}
	})
	
	$('a[href="#scenario-your-vision"]').click(function() {
		showGrid0();
	})
	
	$(window).scroll(function(){
	  	if($('#about').length && $('#about').is(":visible")) {
	  		$("#about").css({display: "none"});
	  	}
	  	
	  	if($('#points').length && $('#points').is(":visible")) {
	  		$("#points").css({display: "none"});
	  	}
	});
});

$(window).on('load', function() {
	
});

})(jQuery);
