package vn.itt.utils.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Đây là class dùng để hiện thị trong các option của Listbox hay Combobox Sử
 * dụng class này để cast các đối tượng sau khi query có chỉ định column sẽ giúp
 * giảm bộ nhớ sử dụng và tránh được việc phải chuyển đổi cũng như sắp xếp lại
 * list- là những thao tác làm chậm các ajax query
 * 
 * @author admin
 */
public class OptionItem {
	private long id;
	private String name;// tên
	private String code;// mã viết tắt nếu cần

	/**
	 * Constructor có dùng mã viết tắt
	 * 
	 * @param id
	 * @param name
	 * @param code
	 */
	public OptionItem() {
	};

	public OptionItem(long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	/**
	 * Constructor không dùng mã viết tắt
	 * 
	 * @param id
	 * @param name
	 */
	public OptionItem(long id, String name) {
		this.id = id;
		this.name = name;
		this.code = "";// dùng dòng trắng để tránh nullpointer khi không có mã
						// viết tắt
	}

	/**
	 * Constructor ma
	 * 
	 * @param id
	 * @param name
	 */
	public OptionItem(String code, String name) {
		this.name = name;
		this.code = code;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Tạo ra 1 list chỉ có 1 phần tử duy nhất do đó người dùng chỉ có 1 chọn
	 * lựa.
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public static List<OptionItem> NoOptionList(long id, String name) {
		List<OptionItem> list = new ArrayList<OptionItem>(1);
		list.add(new OptionItem(id, name));
		return list;
	}
}
