package vn.vif.report.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.vif.models.MenuItem;
import vn.vif.models.OrderDetail;
import vn.vif.models.OrderList;
import vn.vif.report.models.ReportFilter;
import vn.vif.report.models.RptOrder;
import vn.vif.report.services.ReportsService;
import vn.vif.services.MenuItemService;
import vn.vif.services.OrderService;
import vn.vif.services.SettingService;
import vn.vif.utils.VIFUtils;

@Controller
@RequestMapping("/admin")
public class RPTOrderController {
	
	@Autowired
	private ReportsService downloadService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MenuItemService menuItemService;
	@Autowired
	private SettingService settingService;

	@RequestMapping(value = "/report/order/{id}")
	public void exportReport(@PathVariable("id") Long orderId, HttpServletResponse response) {
		String token = downloadService.generate();
		OrderList order = orderService.find(orderId);
		if (order == null) {
			return;
		}
		List<MenuItem> menuList = menuItemService.getOrderList(order.getOrderedDate());
		Long totalQuantity = 0l, totalPrice = 0l, additionQuantity = 0l;
		List<RptOrder> itemList = new ArrayList<RptOrder>();
		List<RptOrder> specItemList = new ArrayList<RptOrder>();
		for (MenuItem it : menuList) {
			RptOrder ro = new RptOrder();
			ro.setName(it.getOrderItem().getName());
			ro.setPrice(it.getOrderItem().getPrice());
			ro.setMiniPrice(it.getOrderItem().getMiniPrice());
			OrderDetail existsItem = null;
			for (OrderDetail o : order.getDetails()) {
				if (o.getOrderItemId() == it.getOrderItem().getId()) {
					existsItem = o;
					break;
				}
			}
			if (existsItem != null) {
				ro.setPrice(existsItem.getPrice());
				ro.setMiniPrice(existsItem.getMiniPrice());
				ro.setQuantity(existsItem.getNumber());
				ro.setMiniQuantity(existsItem.getMiniNumber());
				totalQuantity += ro.getQuantity();
				totalQuantity += ro.getMiniQuantity();
				totalPrice += ro.getQuantity() * ro.getPrice();
				if (ro.getMiniPrice() != null) {
					totalPrice += ro.getMiniQuantity() * ro.getMiniPrice();
				}
			}
			if (it.getOrderItem().getSpecItem() != null && it.getOrderItem().getSpecItem()) {
				specItemList.add(ro);
			} else {
				itemList.add(ro);
			}
		}
		JRDataSource jrDataSource = null;
		if (itemList.size() > 0)
			jrDataSource = new JRBeanCollectionDataSource(itemList);
		else
			jrDataSource = new JREmptyDataSource();
		ReportFilter filter = new ReportFilter();
		filter.setExportType(ReportsService.EXTENSION_TYPE_EXCEL);
		filter.setTemplate("/reports/order.jasper");
		filter.setFileName("Order");
		Calendar ca = Calendar.getInstance();
		ca.setTime(order.getOrderedDate());
		// get current week
		int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
		final int day = ca.get(Calendar.DAY_OF_WEEK);
		// get start week in setting
		int startWeek = settingService.getSetting().getStartWeek();
		// calculate the week
		final int week = (Math.abs(currentWeek - startWeek) % 4) + 1;
		filter.putParamsMap("day", day);
		filter.putParamsMap("week", week);
		filter.putParamsMap("createdDate", VIFUtils.formatDateTime(order.getCreatedDate()));
		filter.putParamsMap("orderedDate", VIFUtils.formatDate(order.getCreatedDate()));
		filter.putParamsMap("customer", order.getCustomer().getName());
		filter.putParamsMap("address", order.getCustomer().getAddressFull());
		filter.putParamsMap("email", order.getCustomer().getEmail());
		filter.putParamsMap("phone", order.getCustomer().getPhone());
		filter.putParamsMap("specList", specItemList);
		filter.putParamsMap("totalQuantity", totalQuantity);
		filter.putParamsMap("totalPrice", totalPrice);
		filter.putParamsMap("additionQuantity", additionQuantity);
		downloadService.download(filter, token, jrDataSource, response);
	}

}
