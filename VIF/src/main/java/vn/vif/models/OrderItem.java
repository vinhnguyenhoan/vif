package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem implements java.io.Serializable {

	private static final long serialVersionUID = -7473088239616243125L;

	private long id;
	
	private String name;

	private String desc;

	private Integer price;

	private Integer miniPrice;
	
	private String image;

	private MultipartFile logoFile;
	
	public OrderItem() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@GenericGenerator(name="gen",strategy="increment")
//	@GeneratedValue(generator="gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESC")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "PRICE")
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Column(name = "MINI_PRICE")
	public Integer getMiniPrice() {
		return miniPrice;
	}

	public void setMiniPrice(Integer miniPrice) {
		this.miniPrice = miniPrice;
	}

	@Column(name = "IMAGE")
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
	
}
