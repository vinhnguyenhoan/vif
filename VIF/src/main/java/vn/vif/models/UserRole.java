package vn.vif.models;

import java.util.ArrayList;
import java.util.List;

public enum UserRole {
	ADMIN("ADMIN", "Quản trị hệ thống", 0),
	SUPERVISOR("ROLE_SUPERVISOR", "Giám Sát", 1),
	ORDER_MAN("ORDER_MAN", "Nhân Viên Đặt Hàng", 2);
	
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
		if (id.equals(ADMIN.id)) {
			return ADMIN;
		} else if (id.equals(ORDER_MAN.id)) {
			return ORDER_MAN;
		} else if (id.equals(SUPERVISOR.id)) {
			return SUPERVISOR;
		} else {
			return null;
		}
	}
	
	public static List<UserRole> getListUserRole(VIFUser user){
		List<UserRole> list = new ArrayList<UserRole>();
		if(user.getRole().equalsIgnoreCase(ADMIN.id)){
			list.add(ADMIN);
			list.add(ORDER_MAN);
			list.add(SUPERVISOR);
		}else if(user.getRole().equalsIgnoreCase(SUPERVISOR.id)){
			list.add(ORDER_MAN);
		}
		return list;
	}
}
