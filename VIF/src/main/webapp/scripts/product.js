function deleteProduct(id) {
	var url = getAppRootPath() + "/product/delete/" + id;
	var ans = confirm('Xóa hàng hóa này?');
	if (!ans) return;
	window.location.href = url;
}
function selectCompany(company) {
	var url = getAppRootPath() + "/product/getCatByCompany/" + $(company).val();
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox($('#goodsCatId'), data, $('#goodsCatId option:first').text());
			if ($('#parentGoodsId').length != 0) {
				changeCat($('#parentGoodsId'), $('#goodsCatId'));
			}
		}
	});
}
function changeCat(parentGoods, cat) {
	var catId = cat.val();
	var url = getAppRootPath() + "/product/goodsparentbycat/" + $('#companyID').val() + "/" + catId;
	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : url,
		success : function(data) {
			renderComboBox(parentGoods, data, "--- Nhóm hàng ---");
		}
	});
	checkVasField(cat);
}
// kiem tra neu hang hoa thuoc loai vas thi hien thi ma dich vu va goi dich vu
function checkVasField(cat) {
	var catId = cat.val();
	if (catId == '6') {
		$("tr#tr_service").attr('style', '');
		$("tr#tr_shortCode").attr('style', '');
	} else {
		$("tr#tr_service").attr('style', 'display:none');
		$("tr#tr_shortCode").attr('style', 'display:none');
	}
}
//thay het cac option
function renderComboBox($domCbo, items, msg) {
	renderComboBoxEmpty($domCbo, msg);// remove old options
	for ( var i = 0; i < items.length; i++) {
		var item = items[i];
		var $domOption = $("<option value = '" + item.id + "'>" + item.name
				+ "</option>");
		$domCbo.append($domOption);
	}
}
//xoa cac option
function renderComboBoxEmpty($domCbo, msg) {
	if ($domCbo.find('option').length > 1) {
		// chi xoa neu danh sach co tu 2 option tro len
		$domCbo.empty();
		var $domOption = $("<option value = '0' selected='selected'>" + msg
				+ "</option>");
		$domCbo.append($domOption);
	}
}
