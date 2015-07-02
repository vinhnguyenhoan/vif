package vn.vif.models;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem implements java.io.Serializable {

	private static final long serialVersionUID = -7473088239616243125L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@GenericGenerator(name="gen",strategy="increment")
//	@GeneratedValue(generator="gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Long id;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "DESC")
	private String desc;

	@Column(name = "PRICE")
	private Integer price;

	@Column(name = "MINI_PRICE")
	private Integer miniPrice;
	
	@Column(name = "IMAGE")
	private String image;

	@Transient
	private MultipartFile logoFile;
	
	@Column(name = "MO")
	private Integer mo;
	@Column(name = "TU")
	private Integer tu;
	@Column(name = "WE")
	private Integer we;
	@Column(name = "TH")
	private Integer th;
	@Column(name = "FR")
	private Integer fr;
	@Column(name = "SA")
	private Integer sa;
	@Column(name = "SU")
	private Integer su;
	
	@Transient
	private static final Map<Integer, OptionData> dateList = new TreeMap<>();
	private static final Map<Integer, String> columnDateMap = new TreeMap<>();
	
	static {
		addDataData(-1, "Tất cả", null);
		addDataData(MONDAY, "T2", "mo");
		addDataData(TUESDAY, "T3", "tu");
		addDataData(WEDNESDAY, "T4", "we");
		addDataData(THURSDAY, "T5", "th");
		addDataData(FRIDAY, "T6", "fr");
		addDataData(SATURDAY, "T7", "sa");
		addDataData(SUNDAY, "CN", "su");
	}
	
	private static final void addDataData(int date, String text, String columnName) {
		OptionData dateData = new OptionData();
		dateData.setId(date);
		dateData.setName(text);
		dateList.put(date, dateData);
		columnDateMap.put(date, columnName);
	}
	
	public static final List<OptionData> getDataList() {
		return new LinkedList<>(dateList.values());
	}
	
	public static final String getDateColName(int date) {
		return columnDateMap.get(date);
	}
	
	public OrderItem() {
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getMiniPrice() {
		return miniPrice;
	}

	public void setMiniPrice(Integer miniPrice) {
		this.miniPrice = miniPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public MultipartFile getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(MultipartFile logoFile) {
		this.logoFile = logoFile;
	}

	public Integer getMo() {
		return mo;
	}

	public void setMo(Integer mo) {
		this.mo = mo;
	}

	public Integer getTu() {
		return tu;
	}

	public void setTu(Integer tu) {
		this.tu = tu;
	}

	public Integer getWe() {
		return we;
	}

	public void setWe(Integer we) {
		this.we = we;
	}

	public Integer getTh() {
		return th;
	}

	public void setTh(Integer th) {
		this.th = th;
	}

	public Integer getFr() {
		return fr;
	}

	public void setFr(Integer fr) {
		this.fr = fr;
	}

	public Integer getSa() {
		return sa;
	}

	public void setSa(Integer sa) {
		this.sa = sa;
	}

	public Integer getSu() {
		return su;
	}

	public void setSu(Integer su) {
		this.su = su;
	}
	
	public String getDateText() {
		String result = "";
		if (mo != null && MONDAY == mo) {
			result = getChanningText(result, dateList.get(MONDAY).getName());
		}
		if (tu != null && TUESDAY == tu) {
			result = getChanningText(result, dateList.get(TUESDAY).getName());
		}
		if (we != null && WEDNESDAY == we) {
			result = getChanningText(result, dateList.get(WEDNESDAY).getName());
		}
		if (th != null && THURSDAY == th) {
			result = getChanningText(result, dateList.get(THURSDAY).getName());
		}
		if (fr != null && FRIDAY == fr) {
			result = getChanningText(result, dateList.get(FRIDAY).getName());
		}
		if (sa != null && SATURDAY == sa) {
			result = getChanningText(result, dateList.get(SATURDAY).getName());
		}
		if (su != null && SUNDAY == su) {
			result = getChanningText(result, dateList.get(SUNDAY).getName());
		}
		if (result.isEmpty()) {
			return dateList.get(-1).getName();
		}
		return result;
	}

	private static final String getChanningText(String result, String text) {
		if (StringUtils.isEmpty(result)) {
			return text;
		}
		return result + ", " + text;
	}
}