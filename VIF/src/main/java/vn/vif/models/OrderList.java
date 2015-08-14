package vn.vif.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ORDER_LIST")
public class OrderList implements java.io.Serializable {

	private static final long serialVersionUID = -1268094244606364985L;

	private Long id;

	private Customer customer;
	@Transient
	private Customer customerEditing;
	
	private Boolean active;
	
	private String note;
	
	private List<OrderDetail> details;
	
	@Transient
	private List<OrderLineDetail> todayDetailLines;
	@Transient
	private List<Long> listOrderItemId;
	@Transient
	private List<Integer> listPrice;
	@Transient
	private List<Integer> listNumber;
	@Transient
	private List<Integer> listMiniPrice;
	@Transient
	private List<Integer> listMiniNumber;
	@Transient
	private List<String> listNote;
	
	@Transient
	private List<OrderLineDetail> allDayDetailLines;
	@Transient
	private List<Long> listAllDaytOrderItemId;
	@Transient
	private List<Integer> listAllDayPrice;
	@Transient
	private List<Integer> listAllDayNumber;
	@Transient
	private List<String> listNoteSpec;
	
	public OrderList() {
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

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "CUSTOMER_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch= FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "DETAIL_ID")
	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	@Transient
	public Customer getCustomerEditing() {
		if (customerEditing == null) {
			customerEditing = new Customer();
		}
		return customerEditing;
	}

	public void setCustomerEditing(Customer customerNew) {
		this.customerEditing = customerNew;
	}

	@Transient
	public List<OrderLineDetail> getTodayDetailLines() {
		return todayDetailLines;
	}
	
	public void setTodayDetailLines(List<OrderLineDetail> todayDetailLines) {
		this.todayDetailLines = todayDetailLines;
	}

	@Transient
	public List<Integer> getListNumber() {
		return listNumber;
	}

	public void setListNumber(List<Integer> listNumber) {
		this.listNumber = listNumber;
	}

	@Transient
	public List<Integer> getListMiniNumber() {
		return listMiniNumber;
	}
	
	public void setListMiniNumber(List<Integer> listOrderItemMiniNumber) {
		this.listMiniNumber = listOrderItemMiniNumber;
	}
	
	@Transient
	public List<Long> getListOrderItemId() {
		return listOrderItemId;
	}

	public void setListOrderItemId(List<Long> listOrderItemId) {
		this.listOrderItemId = listOrderItemId;
	}

	@Transient
	public List<OrderLineDetail> getAllDayDetailLines() {
		return allDayDetailLines;
	}

	public void setAllDayDetailLines(List<OrderLineDetail> allDayDetailLines) {
		this.allDayDetailLines = allDayDetailLines;
	}

	@Transient
	public List<Integer> getListAllDayNumber() {
		return listAllDayNumber;
	}

	public void setListAllDayNumber(List<Integer> listAllDayNumber) {
		this.listAllDayNumber = listAllDayNumber;
	}

	@Transient
	public List<Long> getListAllDaytOrderItemId() {
		return listAllDaytOrderItemId;
	}

	public void setListAllDaytOrderItemId(List<Long> listAllDaytOrderItemId) {
		this.listAllDaytOrderItemId = listAllDaytOrderItemId;
	}

	@Transient
	public List<String> getListNote() {
		return listNote;
	}

	public void setListNote(List<String> listNote) {
		this.listNote = listNote;
	}

	@Transient
	public List<String> getListNoteSpec() {
		return listNoteSpec;
	}

	public void setListNoteSpec(List<String> listNoteSpec) {
		this.listNoteSpec = listNoteSpec;
	}

	@Transient
	public List<Integer> getListPrice() {
		return listPrice;
	}

	public void setListPrice(List<Integer> listPrice) {
		this.listPrice = listPrice;
	}

	@Transient
	public List<Integer> getListMiniPrice() {
		return listMiniPrice;
	}

	public void setListMiniPrice(List<Integer> listMiniPrice) {
		this.listMiniPrice = listMiniPrice;
	}

	@Transient
	public List<Integer> getListAllDayPrice() {
		return listAllDayPrice;
	}

	public void setListAllDayPrice(List<Integer> listAllDayPrice) {
		this.listAllDayPrice = listAllDayPrice;
	}

	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}