function exportReport(detail){
	var url = '/channel/report';
	if (detail) {
		url = '/channel/reportDetail';
	}
	submitExportForm('dailyListForm', contextPath + url);
}
function exportInventoryReport(detail){
	var url = '/channel/inventory/report';
	if (detail) {
		url = '/channel/inventory/reportDetail';
	}
	submitExportForm('dailyListForm', contextPath + url);
}
function backLevel(level) {
	showLoading();
	var select = $('select[name="parentLevel"]');
	var cur = $('#level_' + level);
	for ( var i = select.index(cur); i < select.length; i++) {
		$(select[i]).remove();
	}
	$('#maVietTat').val('');
	$('#capDaiLy').val(level);
	resetAreaSelector();
	document.forms['dailyListForm'].action = contextPath + '/channel/list';
	document.forms['dailyListForm'].submit();
}
function selectLevel(level) {
	var levels = $('select[name="parentLevel"]');
	level = $(level);
	if (level.attr('id') == levels.last().attr('id')) {
		if (level.val()!='0') {
			showLoading();
			resetAreaSelector();
			document.forms['dailyListForm'].action = contextPath + '/channel/list';
			document.forms['dailyListForm'].submit();
		}
		return;
	}
	var companyId = $('#company').val();
	var levelId = level.attr('id').split('_')[1];
	var start = false;
	var loadPos = false;
	if (levels.length > 1 && levels[levels.length - 2].id == level.attr('id')) {
		loadPos = true;
	}
	for ( var i = 0; i < levels.length; i ++) {
		var item = levels[i];
		if ($(item).attr('id') == level.attr('id')) {
			start = true;
			continue;
		}
		if (start) {
			levelId = item.id.split('_')[1];
			break;
		}
	}
	var parent = level.val();
	var url = contextPath + "/channel/getMemberByCompany/"+companyId+"/"+levelId+"/"+parent+"/"+loadPos;
	$.ajax({
		type : 'GET',
		url : url,
		success : function(data) {
			var nextLevel = null, start;
			for ( var i = 0; i < levels.length; i ++) {
				var item = levels[i];
				if ($(item).attr('id') == level.attr('id')) {
					start = true;
					continue;
				}
				if (start && nextLevel == null) {
					nextLevel = item;
					continue;
				}
				if (nextLevel) {
					renderComboBoxEmpty(item, $(item)[0].firstChild.text);
				}
			}
			renderComboBox(nextLevel, data, nextLevel.firstChild.text);
			
		}
	});
}
function selectCompany(company) {
	var levels = $('select[name="parentLevel"]');
	if (levels.length == 0) {
		return;
	}
	var companyId = $(company).val();
	var level = levels.attr('id').split('_')[1];
	var parent = 0;
	var url = contextPath + "/channel/getMemberByCompany/"+companyId+"/"+level+"/"+parent+"/false";
	$.ajax({
		type : 'GET',
		url : url,
		success : function(data) {
			renderComboBox(levels[0], data, levels[0].firstChild.text);
			for ( var i = 1; i < levels.length; i++) {
				var lv = levels[i];
				renderComboBoxEmpty(lv, lv.firstChild.text);
			}
		}
	});
}
function loadDistrict(branch) {
	var url = contextPath + "/channel/getDistrictByBranch/"+branch.value;
	$.ajax({
		type : 'GET',
		url : url,
		success : function(data) {
			renderComboBox("#quanHuyen", data, "--- Quận/Huyện ---");
			renderComboBox("#s_dist", data, "--- Quận/Huyện ---");
			renderComboBoxEmpty("#phuongXa", "--- Phường/Xã ---");
			renderComboBoxEmpty("#s_ward", "--- Phường/Xã ---");
		}
	});
}
function selectCity(domCity, town, ward) {
	var $domCity = $(domCity);
	town = $(town);
	ward = $(ward);
	var cityId = $domCity.val();
	if (cityId != 0) {
		var url = contextPath + "/ajax/getDistricts?cityId=" + cityId;
		$.ajax({
			type : 'GET',
			url : url,
			dataType: 'json',
			success : function(data) {
				renderComboBox(town[0], data.data, town[0].firstChild.text);
			}
		});
	} else {
		renderComboBoxEmpty(town[0], town[0].firstChild.text);
	}
	renderComboBoxEmpty(ward[0], ward[0].firstChild.text);
}

function selectTown(domTown, ward) {
	var $domTown = $(domTown);
	war = $(ward);
	var townUid = $domTown.val();
	if (townUid != 0) {
		var url = contextPath + "/channel/findByTownUid/" + townUid;
		$.ajax({
			type : 'GET',
			url : url,
			dataType: 'json',
			success : function(data) {
				renderComboBox(war[0], data, war[0].firstChild.text);
			}
		});
	} else {
		
		renderComboBoxEmpty(war[0], war[0].firstChild.text);
	}
}

function selectWard(ward, street) {
	var $ward = $(ward);
	var street = $(street);
	var wardId = $ward.val();
	if (wardId != 0) {
		var url = contextPath + "/ajax/getStreets?wardId=" + wardId;
		$.ajax({
			type : 'GET',
			url : url,
			dataType: 'json',
			success : function(data) {
				data = data.data;
				renderComboBox(street[0], data, street[0].firstChild.text);
			}
		});
	} else {
		
		renderComboBoxEmpty(street[0], street[0].firstChild.text);
	}
}

function renderComboBox(comboBoxSelector, items, label) {
	var $domCbo = $(comboBoxSelector);
	renderComboBoxEmpty(comboBoxSelector, label);
	for ( var i = 0; i < items.length; i++) {
		var item = items[i];
		var $domOption = $("<option value = '" + item.id + "'>" + item.name
				+ "</option>");
		$domCbo.append($domOption);
	}
}
function renderComboBoxEmpty(comboBoxSelector, label) {
	var $domCbo = $(comboBoxSelector);
	$domCbo.empty();// remove old options
	var $domOption = $("<option value = '0'>" + label + "</option>");
	$domCbo.append($domOption);
	$domCbo.select2();
}

function timKiemDL(){
	if (!isSelectParent()) {
		return;
	}
	showLoading();
	document.forms['dailyListForm'].action = contextPath + '/channel/list';
	document.forms['dailyListForm'].submit();
}

function deleteDL(id, type) {
	var ans = confirm('Xóa thành viên này?');
	if (!ans) return;
	showLoading();
	var fm = $(document.forms['dailyListForm']);
	fm.append('<input type="hidden" name="deleteId" value="'+id+'"/>');
	fm.append('<input type="hidden" name="memberType" value="'+type+'"/>');
	fm.submit();
}

function XemDL(id) {
	showLoading();
	var url = contextPath + "/channel/list?id=" + id;
	resetAreaSelector();
	document.forms["dailyListForm"].action = url;
	document.forms["dailyListForm"].submit();
}
function resetAreaSelector() {
	$('#tinhThanh').val(0);
	$('#quanHuyen').val(0);
	$('#phuongXa').val(0);
}
function isSelectParent() {
	var levels = $('select[name="parentLevel"]');
	var member = ['vùng miền', 'khu vực', 'giám sát'];
	if (levels.length != 0 && levels[levels.length - 1].value == 0) {
		alert('Chưa chọn '+member[levels.length-1]+'!');
		return false;
	}
	return true;
}
function backToBrach() {
	showLoading();
	$('select[name="parentLevel"]').remove();
	$('#capDaiLy').val(1);
	$('#maVietTat').val('');
	document.forms['dailyListForm'].action = contextPath + '/channel/list';
	document.forms['dailyListForm'].submit();
}