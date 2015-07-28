package vn.vif.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import vn.vif.models.AddressNote;
import vn.vif.models.District;
import vn.vif.models.filter.AddressNoteListFilter;
import vn.vif.services.AddressNoteService;
import vn.vif.utils.PaginationInfo;
import vn.vif.utils.PaginationUtil;
import vn.vif.utils.VIFUtils;
import vn.vif.utils.converter.OptionItem;

@Controller
@RequestMapping("/admin")
public class AddressNoteController {
	
	@Autowired
	private AddressNoteService addressNoteService;
	
	@Autowired
	private View jsonView_i;
	
	@Autowired
	private ServletContext context;

	@RequestMapping(value = "/addressNote/list", method = {RequestMethod.GET, RequestMethod.POST })
	public String viewListAddressNotes(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("frmAddressNoteList") @Valid AddressNoteListFilter addressNoteFilter,
			Model uiModel) {
		long count = addressNoteService.count(addressNoteFilter);
		PaginationInfo paginationInfo = PaginationUtil.calculatePage(count, page, size, uiModel);
		List<AddressNote> addressNoteList = addressNoteService.list(addressNoteFilter, 
				paginationInfo.getStart(), paginationInfo.getSize());
		
		uiModel.addAttribute("addressNoteList", addressNoteList);
		uiModel.addAttribute("districtList", convertDistricyListToOptionItem());

		return "addressNoteList";
		// return "redirect:/admin/orderItem/list";
	}

	@RequestMapping(value = "/addressNote/detail/{id}")
	public String addressNoteDetail(@PathVariable("id") Long addressNoteId, HttpServletRequest request, Model uiModel) {
		try {
			AddressNote addressNote = addressNoteService.find(addressNoteId);
			if (addressNote == null) {
				return "notFoundError";
			}
			uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
			uiModel.addAttribute("addressNote", addressNote);	
			return "addressNoteDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return "orderItemError";
		}
	}
	
	@RequestMapping(value = "/addressNote/detail", params = { "update" })
	public String updateAddressNote(@ModelAttribute("addressNote") @Valid AddressNote addressNote,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateAddressNote(addressNote, uiModel, bindingResult);
	}

	@RequestMapping(value = "/addressNote/add", params = { "new" })
	public String addNewAddressNote(@ModelAttribute("addressNote") @Valid AddressNote addressNote,
			HttpServletRequest request, Model uiModel,
			BindingResult bindingResult) {
		return addUpdateAddressNote(addressNote, uiModel, bindingResult);
	}
	
	private String addUpdateAddressNote(AddressNote addressNote, Model uiModel, BindingResult bindingResult) {
		validateInputData(addressNote, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				if (addressNote.getId() != null) {
					AddressNote aN = addressNoteService.find(addressNote.getId());
					aN.setAddress(addressNote.getAddress());
					aN.setDistrictId(addressNote.getDistrictId());
					aN.setLevel(addressNote.getLevel());
					aN.setOfficeName(addressNote.getOfficeName());
					aN.setStreet(addressNote.getStreet());
					addressNoteService.update(aN);
				} else {
					addressNoteService.add(addressNote);
				}
				uiModel.addAttribute("success", true);
				uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
				return "redirect:/admin/addressNote/detail/" + addressNote.getId();
			} catch (Exception e) {
				e.printStackTrace();
				uiModel.addAttribute("success", false);
			}
		} else {
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				bindingResult.rejectValue(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			
		}
		// Handle for exception
		uiModel.addAttribute("addressNote", addressNote);	
		return "addressNoteDetail";
	}
	
	@RequestMapping(value = "/addressNote/add")
	public String addAddressNote(HttpServletRequest request, Model uiModel) {
		AddressNote addressNote = new AddressNote();
		uiModel.addAttribute("addressNote", addressNote);
		uiModel.addAttribute("districtList", convertDistricyListToOptionItem());
		return "addressNoteAdd";
	}

	private void validateInputData(AddressNote addressNote, BindingResult bindingResult) {
		if (addressNote.getDistrictId() == null || addressNote.getDistrictId() <= 0) {
			bindingResult.rejectValue("districtId", "app_field_empty", new String[]{"Quận"},
					"empty_error_code");
		}
		if (StringUtils.isEmpty(addressNote.getStreet())) {
			bindingResult.rejectValue("street", "app_field_empty",
					new Object[]{"Đường"}, "empty_error_code");
		}
	}
	
	private static final List<OptionItem> convertDistricyListToOptionItem() {
		List<OptionItem> result = new LinkedList<OptionItem>();
		for (District dis : District.values()) {
			result.add(new OptionItem(dis.id, dis.fullName, dis.name));
		}
		return result;
	}
	
	@ResponseBody // add to return JSON
	@RequestMapping(value = "/addressNote/getAddressNoteListFromDistinctId")
	public List<OptionItem> getAddressNoteListFromDistinctId(@RequestParam(value = "dist", required = false) Long distId) {
		if (!VIFUtils.isValid(distId)) {
			return new LinkedList<OptionItem>();
		}
		return addressNoteService.listByDistrictIdAsOptionItems(distId);
	}
}
