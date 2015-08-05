package vn.vif.models;

import java.util.Arrays;
import java.util.List;

public enum WeekOfMonth {
	WEEK_1(1, "Tuần 1"), 
	WEEK_2(2, "Tuần 2"), 
	WEEK_3(3, "Tuần 3"), 
	WEEK_4(4, "Tuần 4");

	private final int id;
	private final String name;

	private WeekOfMonth(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static WeekOfMonth getById(int id){
		for (WeekOfMonth w : values()) {
			if (w.id == id) {
				return w;
			}
		}
		return WEEK_4;
	}
	
	public static List<WeekOfMonth> getList() {
		return Arrays.asList(values());
	}
}
