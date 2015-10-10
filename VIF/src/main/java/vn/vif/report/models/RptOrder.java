package vn.vif.report.models;

public class RptOrder {
	private String name;
	private Integer quantity;
	private Integer price;
	private Integer miniQuantity;
	private Integer miniPrice;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getMiniQuantity() {
		return miniQuantity;
	}
	public void setMiniQuantity(Integer miniQuantity) {
		this.miniQuantity = miniQuantity;
	}
	public Integer getMiniPrice() {
		return miniPrice;
	}
	public void setMiniPrice(Integer integer) {
		this.miniPrice = integer;
	}
}
