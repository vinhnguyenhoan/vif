package vn.vif.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import vn.vif.services.OrderItemService;

@Entity
@Table(name = "ORDER_LIST")
public class OrderList implements OverrideableEntity, java.io.Serializable {

	private static final long serialVersionUID = -1268094244606364985L;

	private Long id;

	private Customer customer;
	@Transient
	private Customer customerEditing;
	
	private Boolean active;
	
	private String note;
	
	private List<OrderDetail> details;
	
	private String address;
	
	private Date createdDate;

	private Date orderedDate;
	
	private String code;
	
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
	
	@Transient
	private boolean override;
	
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ORDERED_DATE")
	public Date getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}

	@Column(name = "CODE", length = 100, unique = true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToMany(fetch= FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "DETAIL_ID")
	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	public void updateDetailsForView(OrderItemService orderItemService) {
		List<OrderLineDetail> orderListToday = new LinkedList<OrderLineDetail>();
		List<OrderLineDetail> orderListAllday = new LinkedList<OrderLineDetail>();
		
		int index = 0;
		for (OrderDetail detail : details) {
			OrderItem item = null;
			if (detail.getOrderItemId() != null) {
				item = orderItemService.find(detail.getOrderItemId());
			}
			OrderLineDetail lineDetail = new OrderLineDetail(index++, item, detail);
			if (item != null && item.getSpecItem() != null && item.getSpecItem()) {
				orderListAllday.add(lineDetail);
			} else {
				orderListToday.add(lineDetail);
			}
		}
		this.setTodayDetailLines(orderListToday);

		this.setAllDayDetailLines(orderListAllday);
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
	
	public void setTodayDetailLines(List<OrderLineDetail> orderListToday) {
		this.todayDetailLines = orderListToday;
		//		List<Long> listOrderItemId = new ArrayList<Long>(orderListToday.size());
		//		List<Integer> listOrderItemNumber = new ArrayList<Integer>(orderListToday.size());
		//		List<Integer> listOrderItemMiniNumber = new ArrayList<Integer>(orderListToday.size());
		//		List<String> listNote = new ArrayList<String>(orderListToday.size());
		//		for (OrderLineDetail oL : orderListToday) {
		//			listOrderItemId.add(oL.getOrderItem().getId());
		//			listOrderItemNumber.add(oL.getOrderDetail().getNumber());
		//			listOrderItemMiniNumber.add(oL.getOrderDetail().getMiniNumber());
		//			listNote.add(oL.getOrderDetail().getNote());
		//		}
		//		this.setListOrderItemId(listOrderItemId);
		//		this.setListNumber(listOrderItemNumber);
		//		this.setListMiniNumber(listOrderItemMiniNumber);
		//		this.setListNote(listNote);
		List<Long> listOrderItemId = new ArrayList<Long>(orderListToday.size());
		List<Integer> listOrderItemNumber = new ArrayList<Integer>(orderListToday.size());
		List<Integer> listOrderItemPrice = new ArrayList<Integer>(orderListToday.size());
		List<Integer> listOrderItemMiniNumber = new ArrayList<Integer>(orderListToday.size());
		List<Integer> listOrderItemMiniPrice = new ArrayList<Integer>(orderListToday.size());
		List<String> listNote = new ArrayList<String>(orderListToday.size());
		for (OrderLineDetail oL : orderListToday) {
			listOrderItemId.add(oL.getOrderItem().getId());
			listOrderItemNumber.add(oL.getOrderDetail().getNumber());
			listOrderItemPrice.add(oL.getOrderDetail().getPrice());
			listOrderItemMiniNumber.add(oL.getOrderDetail().getMiniNumber());
			listOrderItemMiniPrice.add(oL.getOrderDetail().getMiniPrice());
			listNote.add(oL.getOrderDetail().getNote());
		}
		this.setListOrderItemId(listOrderItemId);
		this.setListNumber(listOrderItemNumber);
		this.setListMiniNumber(listOrderItemMiniNumber);
		this.setListPrice(listOrderItemPrice);
		this.setListMiniPrice(listOrderItemMiniPrice);
		this.setListNote(listNote);
	}

	public void setTodayDetailLinesFromEditing(List<OrderLineDetail> orderListToday) {
		this.todayDetailLines = orderListToday;
		if (listOrderItemId == null) {
			return;
		}
		int index = 0;
		for (Long itemId : listOrderItemId) {
			OrderDetail detail = todayDetailLines.get(index).getOrderDetail();
			detail.setOrderItemId(itemId);
			detail.setMiniNumber(this.getListMiniNumber().get(index));
			detail.setNumber(this.getListNumber().get(index));
			detail.setMiniPrice(this.getListMiniPrice().get(index));
			detail.setPrice(this.getListPrice().get(index));
			detail.setNote(this.getListNote().get(index));
			++index;
		}
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

	public void setAllDayDetailLines(List<OrderLineDetail> orderListAllDay) {
		this.allDayDetailLines = orderListAllDay;
//		List<Long> listOrderItemAllDayId = new ArrayList<Long>(orderListAllday.size());
//		List<Integer> listOrderItemAllDayNumber = new ArrayList<Integer>(orderListAllday.size());
//		List<String> listNoteSpec = new ArrayList<String>(orderListToday.size());
//		for (OrderLineDetail oL : orderListAllday) {
//			listOrderItemAllDayId.add(oL.getOrderItem().getId());
//			listOrderItemAllDayNumber.add(oL.getOrderDetail().getNumber());
//			listNoteSpec.add(oL.getOrderDetail().getNote());
//		}
//		this.setListAllDaytOrderItemId(listOrderItemAllDayId);
//		this.setListAllDayNumber(listOrderItemAllDayNumber);
//		this.setListNoteSpec(listNoteSpec);

		List<Long> listOrderItemAllDayId = new ArrayList<Long>(orderListAllDay.size());
		List<Integer> listOrderItemAllDayNumber = new ArrayList<Integer>(orderListAllDay.size());
		List<Integer> listOrderItemAllDayPrice = new ArrayList<Integer>(orderListAllDay.size());
		List<String> listNoteSpec = new ArrayList<String>(orderListAllDay.size());
		for (OrderLineDetail oL : orderListAllDay) {
			listOrderItemAllDayId.add(oL.getOrderItem().getId());
			listOrderItemAllDayNumber.add(oL.getOrderDetail().getNumber());
			listOrderItemAllDayPrice.add(oL.getOrderDetail().getPrice());
			listNoteSpec.add(oL.getOrderDetail().getNote());
		}
		this.setListAllDaytOrderItemId(listOrderItemAllDayId);
		this.setListAllDayNumber(listOrderItemAllDayNumber);
		this.setListAllDayPrice(listOrderItemAllDayPrice);
		this.setListNoteSpec(listNoteSpec);
	}

	public void setAllDayDetailLinesFromEditing(List<OrderLineDetail> orderListAllDay) {
		this.allDayDetailLines = orderListAllDay;
		if (getListAllDaytOrderItemId() == null) {
			return;
		}
		int index = 0;
		for (Long itemId : getListAllDaytOrderItemId()) {
			OrderDetail detail = allDayDetailLines.get(index).getOrderDetail();
			detail.setOrderItemId(itemId);
			detail.setNumber(this.getListAllDayNumber().get(index));
			detail.setPrice(this.getListAllDayPrice().get(index));
			detail.setNote(this.getListAllDayNote().get(index));
			++index;
		}
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
	public List<String> getListAllDayNote() {
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

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	@Transient
	public boolean getOverride() {
		return this.override;
	}

}