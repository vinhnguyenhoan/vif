package vn.vif.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "MENU_ITEM")
public class MenuItem {
	private OrderItem orderItem;
	private Integer day;
	private int week = WeekOfMonth.WEEK_1.getId();
	private Long id;
	private Date createdDate;

	public MenuItem() {}
	
	public MenuItem(Long id) {
		this.id = id;
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
	@JoinColumn(name = "ORDER_ITEM_ID")
	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	@Column(name = "DAY", precision = 10, scale = 0)
	public Integer getDay() {
		return day;
	}
	
	@Transient
	public String getDayString() {
		return OrderItem.getDateColName(day);
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", length = 7)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "WEEK", precision = 10, scale = 0)
	public int getWeek() {
		return week;
	}
	
	@Transient
	public String getWeekString() {
		WeekOfMonth w = WeekOfMonth.getById(week);
		if (w != null) {
			return w.getName();
		}
		return "";
	}

	public void setWeek(int week) {
		this.week = week;
	}
}
