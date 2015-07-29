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
	private Long id;
	
	private String name;

	private String desc;

	private Integer price;

	private Integer miniPrice;
	
	private Boolean specItem;
	
	private String image;

	private MultipartFile logoFile;
	
	private Integer mo;
	private Integer tu;
	private Integer we;
	private Integer th;
	private Integer fr;
	private Integer sa;
	private Integer su;

	private List<Integer> moveToDate;
	private boolean selectedToMoveSellDate;
	
	private static final Map<Integer, OptionData> dateList = new TreeMap<Integer, OptionData>();
	private static final Map<Integer, String> columnDateMap = new TreeMap<Integer, String>();
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
		return new LinkedList<OptionData>(dateList.values());
	}
	
	public static final String getDateColName(int date) {
		return columnDateMap.get(date);
	}
	
	public OrderItem() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 200)
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "PRICE", precision = 9)
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Column(name = "MINI_PRICE", precision = 9)
	public Integer getMiniPrice() {
		return miniPrice;
	}

	public void setMiniPrice(Integer miniPrice) {
		this.miniPrice = miniPrice;
	}

	@Column(name = "SPEC_ITEM")
	public Boolean getSpecItem() {
		return specItem;
	}

	public void setSpecItem(Boolean isSpecItem) {
		this.specItem = isSpecItem;
	}
	
	@Column(name = "IMAGE", precision = 200)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Transient
	public MultipartFile getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(MultipartFile logoFile) {
		this.logoFile = logoFile;
	}

	@Column(name = "MO", precision = 2)
	public Integer getMo() {
		return mo;
	}

	public void setMo(Integer mo) {
		this.mo = mo;
	}

	@Column(name = "TU", precision = 2)
	public Integer getTu() {
		return tu;
	}

	public void setTu(Integer tu) {
		this.tu = tu;
	}

	@Column(name = "WE", precision = 2)
	public Integer getWe() {
		return we;
	}

	public void setWe(Integer we) {
		this.we = we;
	}

	@Column(name = "TH", precision = 2)
	public Integer getTh() {
		return th;
	}

	public void setTh(Integer th) {
		this.th = th;
	}

	@Column(name = "FR", precision = 2)
	public Integer getFr() {
		return fr;
	}

	public void setFr(Integer fr) {
		this.fr = fr;
	}

	@Column(name = "SA", precision = 2)
	public Integer getSa() {
		return sa;
	}

	public void setSa(Integer sa) {
		this.sa = sa;
	}

	@Column(name = "SU", precision = 2)
	public Integer getSu() {
		return su;
	}

	public void setSu(Integer su) {
		this.su = su;
	}
	
	@Transient
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

	@Transient
	public List<Integer> getMoveToDate() {
		moveToDate = new LinkedList<Integer>();
		if (mo != null && MONDAY == mo) {
			moveToDate.add(mo);
		}
		if (tu != null && TUESDAY == tu) {
			moveToDate.add(tu);
		}
		if (we != null && WEDNESDAY == we) {
			moveToDate.add(we);
		}
		if (th != null && THURSDAY == th) {
			moveToDate.add(th);
		}
		if (fr != null && FRIDAY == fr) {
			moveToDate.add(fr);
		}
		if (sa != null && SATURDAY == sa) {
			moveToDate.add(sa);
		}
		if (su != null && SUNDAY == su) {
			moveToDate.add(su);
		}
		if (moveToDate.isEmpty()) {
			moveToDate.add(-1);
		}
		return moveToDate;
	}

	public void setMoveToDate(List<Integer> moveToDate) {
		this.moveToDate = moveToDate;
		if (moveToDate == null || moveToDate.isEmpty()) {
			mo = tu = we = th = fr = sa = su = null;
			return;
		}
		for (Integer date : moveToDate) {
			if (-1 == date) {
				mo = tu = we = th = fr = sa = su = null;
				this.moveToDate = new LinkedList<Integer>();
				return;
			} else if (MONDAY == date) {
				mo = date;
			} else if (TUESDAY == date) {
				tu = date;
			} else if (WEDNESDAY == date) {
				we = date;
			} else if (THURSDAY == date) {
				th = date;
			} else if (FRIDAY == date) {
				fr = date;
			} else if (SATURDAY == date) {
				sa = date;
			} else if (SUNDAY == date) {
				su = date;
			}
		}
	}

	@Transient
	public boolean getSelectedToMoveSellDate() {
		return selectedToMoveSellDate;
	}

	public void setSelectedToMoveSellDate(boolean selectedToMoveSellDate) {
		this.selectedToMoveSellDate = selectedToMoveSellDate;
	}
	
}