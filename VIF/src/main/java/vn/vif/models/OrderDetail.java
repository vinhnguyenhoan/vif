package vn.vif.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER_DETAIL")
public class OrderDetail implements java.io.Serializable {

	private static final long serialVersionUID = -5478098263186652439L;

	private Long id;

	private Long orderItemId;
	
	private Long orderId;
	
	private Integer price;
	
	private Integer number;
	
	private Integer miniPrice;
	
	private Integer miniNumber;
	
	private String note;
	
	public OrderDetail() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ORDER_ITEM_ID", precision = 10, scale = 0)
	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	@Column(name = "ORDER_ID", precision = 10, scale = 0)
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "PRICE", precision = 9)
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Column(name = "NUMBER", precision = 3)
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Column(name = "MINI_PRICE", precision = 9)
	public Integer getMiniPrice() {
		return miniPrice;
	}

	public void setMiniPrice(Integer price) {
		this.miniPrice = price;
	}

	@Column(name = "MINI_NUMBER", precision = 3)
	public Integer getMiniNumber() {
		return miniNumber;
	}

	public void setMiniNumber(Integer number) {
		this.miniNumber = number;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
