package vn.vif.models;

public enum District {

	Q1(1, "Q1", "Quận 1"),
	Q2(2, "Q2", "Quận 2"),
	Q3(3, "Q3", "Quận 3"),
	Q4(4, "Q4", "Quận 4"),
	Q5(5, "Q5", "Quận 5"),
	Q6(6, "Q6", "Quận 6"),
	Q7(7, "Q7", "Quận 7"),
	Q8(8, "Q8", "Quận 8"),
	Q9(9, "Q9", "Quận 9"),
	Q10(10, "Q10", "Quận 10"),
	QTB(11, "QTB", "Tân Bình"),
	QPN(12, "Q12", "Phú Nhuận"),
	QBT(13, "Q1", "Bình Thạnh"),
	GV(14, "Q1", "Gò Vấp");
	
	public final int id;
	public final String name;
	public final String fullName;
	
	private District(int id, String name, String fullName) {
		this.id = id;
		this.name = name;
		this.fullName = fullName;
	}

	public static District getById(Long districtId) {
		for (District dis : values()) {
			if (dis.id == districtId) {
				return dis;
			}
		}
		return null;
	}
	
}
