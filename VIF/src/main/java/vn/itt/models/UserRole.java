package vn.itt.models;

import java.util.ArrayList;
import java.util.List;

public enum UserRole {
	ROLE_ADMIN("ROLE_ADMIN", "Quản trị hệ thống", 0),
	/** Quản trị công ty */
	ROLE_COMPANY_ADMIN("ROLE_COMPANY_ADMIN", "Quản trị công ty", 1),
	/** Quản Trị Miền */
	ROLE_MANAGER("ROLE_AGENT", "Quản Trị Miền", 2),
	/** Quản Trị Khu Vực */
	ROLE_AREA_MANAGER("ROLE_MANAGER", "Quản Trị Khu Vực", 3),
	/** Giám Sát */
	ROLE_SUPERVISOR("ROLE_SUPERVISOR", "Giám Sát", 4),
	/** Tổ Trưởng */
	ROLE_LEADER("ROLE_LEADER", "Tổ Trưởng", 5),
	/** Nhân Viên BH */
	ROLE_SALESMAN("ROLE_SALESMAN", "Nhân Viên BH", 6);
	
	public final String id;
	public final String name;
	public final int level;
	
	private UserRole(String id, String name, int level) {
		this.id = id;
		this.name = name;
		this.level = level;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static UserRole getById(String id) {
		if (id.equals(ROLE_ADMIN.id)) {
			return ROLE_ADMIN;
		} else if (id.equals(ROLE_COMPANY_ADMIN.id)) {
			return ROLE_COMPANY_ADMIN;
		} else if (id.equals(ROLE_MANAGER.id)) {
			return ROLE_MANAGER;
		}else if (id.equals(ROLE_AREA_MANAGER.id)) {
			return ROLE_AREA_MANAGER;
		} else if (id.equals(ROLE_SUPERVISOR.id)) {
			return ROLE_SUPERVISOR;
		} else if (id.equals(ROLE_LEADER.id)) {
			return ROLE_LEADER;
		} else if (id.equals(ROLE_SALESMAN.id)) {
			return ROLE_SALESMAN;
		} else {
			return null;
		}
	}
	
	public static List<UserRole> getListUserRole(NguoiSuDung user){
		List<UserRole> list = new ArrayList<UserRole>();
		if(user.getVaiTro().equalsIgnoreCase(ROLE_ADMIN.id)){
			list.add(ROLE_ADMIN);
			list.add(ROLE_COMPANY_ADMIN);
			list.add(ROLE_MANAGER);
			list.add(ROLE_AREA_MANAGER);
			list.add(ROLE_SUPERVISOR);
			list.add(ROLE_LEADER);
			list.add(ROLE_SALESMAN);
		}else if(user.getVaiTro().equalsIgnoreCase(ROLE_COMPANY_ADMIN.id)){
			list.add(ROLE_MANAGER);
			list.add(ROLE_AREA_MANAGER);
			list.add(ROLE_SUPERVISOR);
			list.add(ROLE_LEADER);
			list.add(ROLE_SALESMAN);
		}else if(user.getVaiTro().equalsIgnoreCase(ROLE_MANAGER.id)){
			list.add(ROLE_AREA_MANAGER);
			list.add(ROLE_SUPERVISOR);
			list.add(ROLE_LEADER);
			list.add(ROLE_SALESMAN);
		}else if(user.getVaiTro().equalsIgnoreCase(ROLE_AREA_MANAGER.id)){
			list.add(ROLE_SUPERVISOR);
			list.add(ROLE_LEADER);
			list.add(ROLE_SALESMAN);
		}else{
			list.add(ROLE_SALESMAN);
		}
		return list;
	}
}
