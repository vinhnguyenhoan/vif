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

import org.apache.commons.lang3.StringUtils;

import vn.vif.services.OrderItemService;
import vn.vif.utils.CannotFindByIdException;
import vn.vif.utils.VIFUtils;

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

	@Transient
	public String getCreatedDateAsText() {
		return VIFUtils.formatDateTime(createdDate);
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

	public void updateDetailsForView(List<OrderLineDetail>[] itemsToday, OrderItemService orderItemService) {
		List<OrderLineDetail> orderListToday = new LinkedList<OrderLineDetail>();
		List<OrderLineDetail> orderListAllday = new LinkedList<OrderLineDetail>();
		if (!isActive()) {
			// If not active show another today items 
			orderListToday = itemsToday[0];
			orderListAllday = itemsToday[1];
		}
		
		int index = -1;
		for (OrderDetail detail : details) {
			index = updateDetail(orderListToday, false, detail, index, orderItemService);
			index = updateDetail(orderListAllday, true, detail, index, orderItemService);
		}
		this.setTodayDetailLines(orderListToday);
		this.setAllDayDetailLines(orderListAllday);
	}
	
	private int updateDetail(List<OrderLineDetail> orderListToday, boolean isSpecList, OrderDetail detail, int index, OrderItemService orderItemService) {
		if (!isActive()) {
			// If not active -> check is today item let update current order detail
			for (OrderLineDetail line : orderListToday) {
				if (line.getOrderItem().getId() == detail.getOrderItemId()) {
					line.setOrderDetail(detail);
					return index;
				}
			}
			//return index;
		}
		// This detail order not exist at today list but it still need to show
		OrderItem item = null;
		if (detail.getOrderItemId() != null) {
			item  = orderItemService.find(detail.getOrderItemId());
		}
		boolean itemIsSpec = item == null || item.getSpecItem() == null || !item.getSpecItem();
		if (itemIsSpec == isSpecList) {
			OrderLineDetail lineDetail = new OrderLineDetail(index, item, detail);
			orderListToday.add(lineDetail);
		}
		return index - 1;
	}

	private boolean isActive() {
		return active != null && active;
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
	
	@Transient
	public String getStatusText() {
		if (isActive()) {
			return "Duyệt";
		}
		return "Mới";
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

	public void updateDetailsFromUIModel(OrderItemService orderItemService) {
		this.details = new LinkedList<OrderDetail>();
		
		List<Long> orderItemIdToday = this.getListOrderItemId();
		if (orderItemIdToday != null) {
			int index = 0;
			for (Long itemId : orderItemIdToday) {
				OrderItem item = orderItemService.find(itemId);
				if (item == null) {
					throw new CannotFindByIdException();
				}
				OrderDetail detail = new OrderDetail();
				detail.setOrderItemId(itemId);
				detail.setMiniNumber(this.getListIntAt(this.getListMiniNumber(), index));
				detail.setNumber(this.getListIntAt(this.getListNumber(), index));
				detail.setMiniPrice(this.getListIntAt(this.getListMiniPrice(), index));
				detail.setPrice(this.getListIntAt(this.getListPrice(), index));
				detail.setNote(this.getListStringAt(this.getListNote(), index));
				details.add(detail);
				index++;
			}
		}
		
		List<Long> orderItemIdAllDay = this.getListAllDaytOrderItemId();
		if (orderItemIdAllDay != null) {
			int index = 0;
			for (Long itemId : orderItemIdAllDay) {
				OrderItem item = orderItemService.find(itemId);
				if (item == null) {
					throw new CannotFindByIdException();
				}
				OrderDetail detail = new OrderDetail();
				detail.setOrderItemId(itemId);
				detail.setNumber(this.getListIntAt(this.getListAllDayNumber(), index));
				detail.setPrice(this.getListIntAt(this.getListAllDayPrice(), index));
				detail.setNote(this.getListStringAt(this.getListAllDayNote(), index));
				details.add(detail);
				index++;
			}
		}
	}

	private Integer getListIntAt(List<Integer> listData, int index) {
		if (listData == null || listData.isEmpty() || index < 0) {
			return 0;
		}
		return listData.get(index);
	}
	
	private String getListStringAt(List<String> listData, int index) {
		if (listData == null || listData.isEmpty() || index < 0) {
			return StringUtils.EMPTY;
		}
		return listData.get(index);
	}

}
