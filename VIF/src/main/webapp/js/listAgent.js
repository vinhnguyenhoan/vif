
function createOwnerUser(type) {
	tb_show('Tạo người quản trị','TB_inline?height=550&amp;width=700&amp;inlineId=userDialog');
	// reset role selector
	var sel = $('#u_vaiTro');
	var ops = sel.children();
	for (var i = ops.length - 1; i >= 0; i--) {
		$(ops[i]).remove();
	}
	if (type == 2) { // select POS
		sel.append('<option value="ROLE_POS">ROLE_POS</option>');
	} else if (type == 1) { // select agent
		sel.append('<option value="ROLE_AGENT">Quản Trị Miền</option>');
	}
	sel.select2();
}

function selectDiscount(defType) {
	var $defType = $(defType);
	var typeId = $defType.val();

	if(typeId == 2){
		
		document.getElementById('conTractType').disabled = true;	
		document.getElementById('comLevel').disabled = true;
		document.getElementById('tienHH').disabled = true;
		document.getElementById('money').disabled = true;
		document.getElementById('goodsId').disabled = true;
		
		
	}else{
		document.getElementById('conTractType').disabled = false;	
		document.getElementById('comLevel').disabled = false;
		document.getElementById('tienHH').disabled = false;
		document.getElementById('money').disabled = false;
		document.getElementById('goodsId').disabled = false;
	}
	
	var url = contextPath + "/ajax/getGoodsCat?typeId=" + typeId;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox("#goodsCatId", data.data, "-- Loại Sản Phẩm/ Dịch Vụ --");
		}
	});
}


function selectBranch(domBranch) {
	var $domBranch = $(domBranch);
	var branchUid = $domBranch.val();
	var url = contextPath + "/ajax/getAgents?branchId=" + branchUid;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox("#daiLy", data.data, "-- Đại Lý --");
		}
	});
}

function selectLevel(domLevel, totalLevel) {
	var $domLevel = $(domLevel);
	var levelUid = $domLevel.val();
	
	for(var i=1; i<=totalLevel+2 ; i++){
		$('#levelValue'+i).val('');
		if(levelUid>=i){
			$('#levelValue'+i).prop( "disabled", false );
		}else {
			$('#levelValue'+i).prop( "disabled", true );
			$('#sl'+i).prop( "disabled", true );
		}
		
	}
	/*var url = contextPath + "/ajax/getLevels?levelId=" + levelUid;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			//renderComboBox("#levelCom", data.data, "-- Cấp Hoa Hồng Khác --");
			
			for(var i=1; i<totalLevel+2 ; i++){
				renderComboBox("#levelCom"+i, data.data, "-- Cấp Hoa Hồng Khác --");
			}
			
		}
	});
	*/
}

function selectCat(domCat) {
	var $domCat = $(domCat);
	var catUid = $domCat.val();
	var url = contextPath + "/ajax/getGoods?catId=" + catUid;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox("#goodsId", data.data, "-- Tên Sản Phẩm/ Dịch Vụ --");
		}
	});
}

function selectCity(domCity) {
	var $domCity = $(domCity);
	var cityUid = $domCity.val();
	var url = contextPath + "/ajax/getDistricts?cityId=" + cityUid;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox("#quanHuyen", data.data, "-- Quận/Huyện --");
		}
	});
	renderComboBoxEmpty("#phuongXa", '-- Phường/Xã --');
}
function selectDistrict(domTown) {
	var $domTown = $(domTown);
	var townUid = $domTown.val();

	var url = contextPath + "/ajax/getWards?districtId=" + townUid;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox("#phuongXa", data.data, "-- Phường/Xã --");
		}
	});
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