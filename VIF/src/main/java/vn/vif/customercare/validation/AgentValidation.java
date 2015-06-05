package vn.vif.customercare.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class AgentValidation  implements Validator{
	
	//Only validate for UserForm and its sub-class.
	public boolean supports(Class<?> clazz) {
		return true;//DaiLy.class.equals(clazz);		
	}

	
	public void validate(Object target, Errors errors) {
		/*DaiLy o = (DaiLy) target;
		
		String tenKeHoach=o.getTen();
		if (tenKeHoach ==null  || tenKeHoach.length()==0){
				errors.rejectValue("ten", "errTenDaiLy", "errCodeTenDaiLy");
		}*/
		//Ma Viet tat
		/*String maVietTat=o.getMaVietTat();
		if (maVietTat ==null  || maVietTat.length()==0){
				errors.rejectValue("maVietTat", "errMaVietTat", "errCodeMaVietTat");
		}
		
		if (o.getTinhThanhPho()==0){
			errors.rejectValue("tinhThanhPho", "errTinhThanhPho","errCodeTinhThanhPho");
		}
		if (o.getQuanHuyen()==null || o.getQuanHuyen()==0){
			errors.rejectValue("quanHuyen", "errQuanHuyen","errCodeQuanHuyen");
		}
		if (o.getPhuongXa()==null || o.getPhuongXa()==0){
			errors.rejectValue("phuongXa", "errPhuongXa","errCodePhuongXa");
		}
		String diaChi=o.getDiaChi();
		if (diaChi ==null  || diaChi.length()==0){
				errors.rejectValue("diaChi", "errDiaChi", "errCodeDiaChi");
		}
		String dienThoai=o.getDienThoai();
		if (dienThoai ==null  || dienThoai.length()==0){
				errors.rejectValue("dienThoai", "errDienThoai", "errCodeDienThoai");
		}
		String nguoiLienHe=o.getNguoiLienHe();
		if (nguoiLienHe ==null  || nguoiLienHe.length()==0){
				errors.rejectValue("nguoiLienHe", "errNguoiLienHe", "errCodeNguoiLienHe");
		}
		if (o.getLoaiDaiLy()==-1){
			errors.rejectValue("loaiDaiLy", "errLoaiDaiLy","errCodeLoaiDaiLy");
		}
		String cenCode=o.getsCenCode();
		if (cenCode ==null  || cenCode.length()==0){
				errors.rejectValue("sCenCode", "errCenCode", "errCodeCenCode");
		}
		if (cenCode !=null  &&  cenCode.length()>0 && !Utility.isNumeric(cenCode)){
			errors.rejectValue("sCenCode", "errCenCodeNum", "errCodeCenCodeNum");
		}
		
		String cf=o.getsCf();
		if (cf ==null  || cf.length()==0){
				errors.rejectValue("sCf", "errCf", "errCodeCf");
		}
		if (cf !=null  && cf.length()>0  && !Utility.isNumeric(cf)){
			errors.rejectValue("sCf", "errCfNum", "errCodeCfNum");
		}*/
		
	}

}
