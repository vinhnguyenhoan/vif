/**
 * Parse HTML and find the element by id
 */
function parseHTML(html, idStr) {
	var root = document.createElement("div");
	root.innerHTML = html;
	return findElement(root, idStr);
}

/**
 * Find child element
 */
function findElement(parent, elementId) {
	var allChilds = parent.childNodes;
	for ( var i = 0; i < allChilds.length; i++) {
		var child = allChilds[i];
		if (child.id == elementId) {
			return child;
		} else {
			var temp = findElement(child, elementId);
			if (temp != null) {
				return temp;
			}
		}
	}
	return null;
}

/**
 * Submit ajax using get method
 */
function submitAjaxGet(url, resultId, listElements) {
	url = createUrlByUrlAndElement(url, listElements);
	// Submit ajax
	$.ajax({
		type : "GET",
		url : url,
		dataType : "html",
		// Action handle
		success : function(data) {
			fetchDataToElement(resultId, data);
		}
	});
}

/**
 * Submit ajax and refresh list element
 */
function submitAjaxGetReplaceListElement(url, listelementId, listElements, scrollElementId) {
	url = createUrlByUrlAndElement(url, listElements);
	// Submit ajax
	$.ajax({
		type : "GET",
		url : url,
		dataType : "html",
		// Success handle
		success : function(data) {
			// Refresh elements
			for ( var int = 0; int < listelementId.length; int++) {
				var elementId = listelementId[int];
				fetchDataToElement(elementId, data);
			}
			// Scroll
			if (scrollElementId != undefined) {
				$('html, body').animate({
					scrollTop : $(scrollElementId).offset().top - 10
				}, 0);
			}
		}
	});
}

/**
 * Submit ajax and refresh list element
 */
function submitAjaxPostReplaceListElement(url, listelementId, listElements, scrollElementId) {
	showLoading();
	var data = {};
	for ( var i = 0; i < listElements.length; i++) {
		var element = listElements[i];
		data[element.name] = element.value;
	}
	// Submit ajax
	$.ajax({
		type : "POST",
		url : url,
		data : data,
		// Success handle
		success : function(data) {
			// Refresh elements
			for ( var int = 0; int < listelementId.length; int++) {
				var elementId = listelementId[int];
				fetchDataToElement(elementId, data);
			}
			// Scroll
			if (scrollElementId != undefined) {
				$('html, body').animate({
					scrollTop : $(scrollElementId).offset().top - 10
				}, 0);
			}
			hideLoading();
		}
	});
}

/**
 * Add element data to url
 */
function createUrlByUrlAndElement(url, listElements) {
	for ( var i = 0; i < listElements.length; i++) {
		var element = listElements[i];
		if (element != null && element != undefined) {
			if (url.indexOf("?") == -1) {
				url = url + "?";
			} else {
				url = url + "&";
			}
			url = url + element.name + "=" + element.value;
		}
	}
	return url;
}

/**
 * Fetch loaded html data to element content by id
 */
function fetchDataToElement(elementId, data) {
	var gotcha = parseHTML(data, elementId);
	if (gotcha == undefined) {
		$('#' + elementId).html("");
	} else {
		$('#' + elementId).html(gotcha.innerHTML);
	}
}

/**
 * Reload Google Map
 */
function reloadMap(latitude, longitude, mapType, zoom) {
	var mapTypeId = google.maps.MapTypeId.ROADMAP;
	if (mapType == 'SATELLITE') {
		mapTypeId = google.maps.MapTypeId.SATELLITE;
	} else if (mapType == 'HYBRID') {
		mapTypeId = google.maps.MapTypeId.HYBRID;
	} else if (mapType == 'TERRAIN') {
		mapTypeId = google.maps.MapTypeId.TERRAIN;
	}
	var mapOptions = {
		zoom : parseInt(zoom),
		zoomControl: true,
		center : new google.maps.LatLng(latitude, longitude),
		mapTypeId : mapTypeId,
		disableDefaultUI : true,
		scrollwheel : false,
		disableDoubleClickZoom : true,
	};
	var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
	createOverlay(map);
}

/**
 * Submit Google Map option
 */
function submitMapOption() {
	var latitude = document.getElementById('latitude').value;
	var longitude = document.getElementById('longitude').value;
	var zoom = document.getElementById('zoom').value;
	var mapType = document.getElementById('mapType').value;
	reloadMap(latitude, longitude, mapType, zoom);
}

/**
 * Table's checkbox listener
 */
$(document).ready(function() {
	// Select all checkbox listener
	var selectAllCheckBox = $('.select_all_checkbox');
	selectAllCheckBox.click(function() {
		var table = selectAllCheckBox.parent().parent().parent().parent();
		var arrayCheckBox = table.find('td > .select_checkbox');
		var checked = selectAllCheckBox.is(':checked');
		for ( var int = 0; int < arrayCheckBox.length; int++) {
			jQuery(arrayCheckBox[int]).attr('checked', checked);
		}
	});
	// Select checkbox listener
	var selectCheckBox = $('.select_checkbox');
	selectCheckBox.click(function() {
		var table = selectAllCheckBox.parent().parent().parent().parent();
		var selectAll = table.find('th > .select_all_checkbox');
		var arrayCheckBox = table.find('td > .select_checkbox');
		var checked = true;
		for ( var int = 0; int < arrayCheckBox.length; int++) {
			checked = checked && jQuery(arrayCheckBox[int]).attr('checked');
		}
		if (checked == undefined) {
			jQuery(selectAll).attr('checked', false);
		} else {
			jQuery(selectAll).attr('checked', checked);
		}
	});
	$('a[href^="/'+location.pathname.split('/')[1]+'/"]').click(function (){showLoading();});
	if (typeof(hideLoading) != "undefined")
		hideLoading();
});

/**
 * Paging click
 */
function getListElementArrays(listElement) {
	var elementReplacedArray = getListElementIdArrays(listElement);
	var result = new Array();
	for ( var i = 0; i < elementReplacedArray.length; i++) {
		var elementId = elementReplacedArray[i];
		result[i] = document.getElementById(elementId);
	}
	return result;
}

/**
 * Paging click
 */
function getListElementIdArrays(listElement) {
	var elementReplacedArray = listElement.split(',');
	var result = new Array();
	for ( var i = 0; i < elementReplacedArray.length; i++) {
		var elementId = elementReplacedArray[i];
		result[i] = jQuery.trim(elementId);
	}
	return result;
}

/**
 * Paging size select
 */
function pagingSizeSelected(select, listRefreshElement, sizeParamName, sizeSelectedLink, listAdditionElement, method) {
	var selectedValue = select.options[select.selectedIndex].value;
	var submitLink = sizeSelectedLink + "&" + sizeParamName + "=" + selectedValue;
	if (method == 'Get') {
		submitAjaxGetReplaceListElement(submitLink, getListElementIdArrays(listRefreshElement), listAdditionElement);
	} else {
		submitAjaxPostReplaceListElement(submitLink, getListElementIdArrays(listRefreshElement), listAdditionElement);
	}
}

function openPopup(link, title) {
	jQuery("#popupContainer > .popupDisplay > .header > .title").html(title);
	// Add loading image
	var loadingImg = jQuery('<img style="position: relative;" width="60px" height="60px"/>');
	loadingImg.attr('src', '../images/loading.gif');
	var popupContent = jQuery('#popupContent');
	popupContent.html(loadingImg);
	// Show popup
	var popup = jQuery("#popupContainer");
	popup.css('height', jQuery(document).height());
	popup.show();
	// Popup display
	var popupDisplay = jQuery("#popupContainer > .popupDisplay");
	var popupDisplayTop = $(window).height() / 2 - popupDisplay.height() / 2 + $(window).scrollTop();
	popupDisplay.css('marginTop', popupDisplayTop + "px");
	popupDisplay.css('marginLeft', ($(window).width() / 2 - popupDisplay.width() / 2) + "px");
	// Loading image position
	loadingImg.css('left', popupContent.width() / 2 - loadingImg.width() / 2);
	loadingImg.css('top', (popupDisplay.height() - jQuery("#popupContainer > .popupDisplay > .header").height()) / 2
			- loadingImg.height() / 2);
	submitAjaxGetReplaceListElementWithDifferentelement(link, "rightcol", getListElementIdArrays('popupContent'),
			new Array(), function() {
				popupDisplayTop = $(window).height() / 2 + $(window).scrollTop() - popupDisplay.height() / 2;
				popupDisplay.css('marginTop', popupDisplayTop + "px");
				popupDisplay.css('marginLeft', ($(window).width() / 2 - popupDisplay.width() / 2) + "px");
			});
}

function closePopup() {
	var popup = jQuery("#popupContainer");
	popup.hide();
}

/**
 * Submit ajax url with parameters receive from listElements and refresh
 * elements with id in listelementId. If scrollElementId not empty, scroll to
 * element with id scrollElementId.
 */
function submitAjaxGetReplaceListElementWithDifferentelement(url, dataElementId, listelementId, listElements,
		actionAfterSucess) {
	url = createUrlByUrlAndElement(url, listElements);
	jQuery.ajax({
		type : "GET",
		url : url,
		dataType : "html",
		success : function(data) {
			for ( var int = 0; int < listelementId.length; int++) {
				var elementId = listelementId[int];
				refreshElementWithDifferentElement(data, dataElementId, elementId);
			}
			if (actionAfterSucess != undefined) {
				actionAfterSucess();
			}
		}
	});
}

/**
 * Refresh element with new html data
 */
function refreshElementWithDifferentElement(data, dataElementId, elementId) {
	var gotcha = parseHTML(data, dataElementId);
	if (gotcha == undefined) {
		jQuery('#' + elementId).html("");
	} else {
		jQuery('#' + elementId).html(gotcha.innerHTML);
	}
}

function restrictInputDoubleOnly(event, sourceElement) {
	var value = sourceElement.value;
	var editedValue = "";
	for ( var int = 0; int < value.length; int++) {
		var currentChar = value[int];
		if (currentChar == '.') {
			if (editedValue.indexOf('.') < 0) {
				editedValue = editedValue + currentChar;
			}
		} else {
			if (isNumber(currentChar)) {
				editedValue = editedValue + currentChar;
			}
		}
	}
	sourceElement.value = editedValue;
}

function isNumber(character) {
	try {
		var number = Number(character);
		return number >= 0 && number <= 9;
	} catch (e) {
		return false;
	}
}

function testIPAddress(ipElementId, emptyMessage) {
	var value = jQuery.trim($('#' + ipElementId).val());
	if (value == '') {
		alert(emptyMessage);
		return;
	}
	window.open("http://" + value);
	return true;
}

function showPopup(element, popupId, event) {
	var popup = $('#' + popupId);
	var percentHeight = $(window).height() / $(document).height();
	popup.css('top', event.clientY);
	popup.css('left', event.clientX);
	popup.show();
	if (popup.offset().top + popup.width() > $(document).height()) {
		$(document).find('body').height(popup.offset().top + popup.width());
	}
}

function hidePopup(popupId) {
	var popup = $('#' + popupId);
	popup.hide();
}

//Load list District due to selected city
function citySelected(citySelect, districtSelect) {
	var url = location.protocol+"//"+ location.host + location.pathname + "/getDistrict";
	submitSelectSelection(citySelect,districtSelect, url, "cityId");
}

//Load list District due to selected city
function districtSelected(districtSelect, wardSelect) {
	var url = location.protocol+"//"+ location.host + location.pathname + "/getWard";
	submitSelectSelection(districtSelect, wardSelect, url, "districtId");
}

//Load list District due to selected district
function submitSelectSelection(sourceSelect, destSelect, url, paramName) {
	// Empty ward
	destSelect.empty();
	var option = $(document.createElement("option"));
	option.val(-1);
	option.appendTo(destSelect);
	// Get selected value
	var selectedValues = sourceSelect.find('option:selected');
	if (selectedValues!=undefined && selectedValues.length > 0){
		var selectedValue = selectedValues[0].value;
		if (selectedValue >= 0){
			// Create url
			var location = window.location;
			if (url.indexOf('?')>=0){
				url += "&";
			}else{
				url += "?";
			}
			url += paramName+"="+selectedValue;
			// Submit ajax
			$.ajax({
				url:url,
				type: 'POST',
				dataType : "json",
				success: function(data){
					//Parser JSON
					for ( var int = 0; int < data.length; int++) {
						var ward = data[int];
						// create option
						var option = $(document.createElement("option"));
						option.val(ward.id);
						option.html(ward.ten);
						if (int==0){
							option.attr('selected', 'selected');
						}
						// add to ward select
						option.appendTo(destSelect);
					}
					destSelect.select2();
				}
			});
		}
	}
}

function doPagination(submitURL, additionalParams, submitMethod, formSubmit) {
	var finalURL = submitURL;
	showLoading();
	if (additionalParams != '' && additionalParams != undefined) {
	// Get all HTML elements which will be passed server side to as parameters
		var arrElements = getListElementArrays(additionalParams);
		// Build the final submit URL in which parameters are encoded as pairs of key=value
		finalURL = createUrlByUrlAndElement(submitURL, arrElements);
	}
	if (submitMethod != null && submitMethod != undefined) {
		submitMethod = submitMethod.toUpperCase();
		if (submitMethod == 'GET') {
			window.location = finalURL;
		}
		else {
			document.forms[formSubmit].action = finalURL;
			document.forms[formSubmit].method = 'POST';
			document.forms[formSubmit].submit();
		}
	}
}
