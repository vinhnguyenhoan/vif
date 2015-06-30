package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem implements java.io.Serializable {

	private static final long serialVersionUID = -7473088239616243125L;

	private Long id;
	
	private String name;

	private String desc;

	private Integer price;

	private Integer miniPrice;
	
	// TODO field for image
	
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
	
}
