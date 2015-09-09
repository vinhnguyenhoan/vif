package vn.vif.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.vif.models.Customer;
import vn.vif.models.District;
import vn.vif.models.MenuItem;
import vn.vif.models.OrderDetail;
import vn.vif.models.OrderInfor;
import vn.vif.models.OrderItem;
import vn.vif.models.OrderList;
import vn.vif.models.Setting;
import vn.vif.services.CustomerService;
import vn.vif.services.MenuItemService;
import vn.vif.services.OrderItemService;
import vn.vif.services.OrderService;
import vn.vif.services.SettingService;
import vn.vif.utils.VIFUtils;
@Controller
public class HomeController {

	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private MenuItemService menuItemService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SettingService settingService;

	@RequestMapping(value = "/admin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String index(HttpServletRequest request, Model uiModel) {

		return "index";
	}

	/*
	@RequestMapping("/")
	public String home(HttpServletRequest request, Model uiModel) {
		// latitude
		double latitude = 10.785250547433419;
		try {
			latitude = Double.parseDouble(request.getParameter("latitude"));
		} catch (Exception e) {
		}
		// longitude
		double longitude = 106.6841983795166;
		try {
			longitude = Double.parseDouble(request.getParameter("longitude"));
		} catch (Exception e) {
		}
		// zoom
		String zoom = request.getParameter("zoom");
		if (zoom == null) {
			zoom = "13";
		}
		uiModel.addAttribute("zoom", zoom);
		// mapType
		String mapType = request.getParameter("mapType");
		if (mapType == null) {
			mapType = "ROADMAP";
		}
		uiModel.addAttribute("mapType", mapType);
		uiModel.addAttribute("latitude", latitude);
		uiModel.addAttribute("longitude", longitude);
		// Navigation display
		uiModel.addAttribute("navTabPos", 1);
		
		
		
		// return
		return "index";
	}
	*/
	
	@RequestMapping(value = "/", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String home(HttpServletRequest request, Model uiModel) {
		
		VIFUtils.fillDate(uiModel);
		
		Map<Integer, List<MenuItem>>[] data = menuItemService.getMenuItemData();
		uiModel.addAttribute("orderItemData", data[0]);
		uiModel.addAttribute("orderItemSpec", data[1]);
		uiModel.addAttribute("districtList", District.values());
		uiModel.addAttribute("login", customerService.getLogin());
		
		/*Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MONTH, Calendar.JANUARY);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		for (int i = 0; i < 40; i++) {
			int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
			System.out.println(currentWeek + " " + ca.getTime());
			ca.add(Calendar.DAY_OF_MONTH, 1);
		}*/
		
//		System.out.println(VIFUtils.formatPhoneNumber("+849876543"));
		
		return "web";
	}


	@RequestMapping(value = "/web", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String homeWeb(HttpServletRequest request, Model uiModel) {
		
		
		return home(request, uiModel);
	}
	
	@RequestMapping(value = "/register")
	@ResponseBody
	public Map<String, Object> register(@ModelAttribute("customer") @Valid Customer customer) {
		Map<String, Object> data = new HashMap<String, Object>();
		String error = null;
		customer.setPhone(VIFUtils.formatPhoneNumber(customer.getPhone()));
		if (!VIFUtils.isValid(customer.getName())) {
			error = "Chưa nhập tên";
		} else if (!VIFUtils.isValid(customer.getPhone())) {
			error = "Chưa nhập số điện thoại";
		} else if (customerService.list(new String[]{"phone"}, 
				new Object[]{customer.getPhone()}, true, null, -1, -1).size() > 0) {
			error = "Số điện thoại này đã được đăng ký";
		} else if (!VIFUtils.isValid(customer.getAddress())) {
			error = "Chưa nhập số địa chỉ";
		} else if (!VIFUtils.isValid(customer.getDistrictId())) {
			error = "Chưa chọn quận";
		} else if (!VIFUtils.isValid(customer.getEmail())) {
			error = "Chưa nhập email";
		} else if (customerService.list(new String[]{"email"}, 
				new Object[]{customer.getEmail()}, true, null, -1, -1).size() > 0) {
			error = "Email này đã được đăng ký";
		}
		if (error != null) {
			data.put("error", error);
		} else {
			customerService.add(customer);
			data.put("success", "Quý khách đã đăng ký thành công. Chúng tôi sẽ liên hệ để kích hoạt tài khoản của quý khách. Xin cám ơn!");
		}
		return data;
	}
	
	@RequestMapping(value = "/loginCus")
	@ResponseBody
	public Map<String, Object> login(@RequestParam(value = "code", required = true) String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		String error = null;
		if (!VIFUtils.isValid(code)) {
			error = "Mã khách hàng không được rỗng";
		} else {
			List<Customer> list = customerService.list(new String[]{"phone"}, new Object[]{code}, true, null, -1, -1);
			if (list.isEmpty()) {
				error = "Mã khách hàng không tồn tại";
			} else {
				Customer cus = list.get(0);
				if (!cus.getActive()) {
					error = "Mã khách hàng chưa được kích hoạt!";
				} else {
					customerService.login(cus);
				}
			}
		}
		if (error != null) {
			data.put("error", error);
		} else {
			data.put("success", "Đăng nhập thành công!");
		}
		return data;
	}
	
	@RequestMapping(value = "/doOrder")
	@ResponseBody
	public Map<String, Object> doOrder(@ModelAttribute("order") @Valid OrderInfor order) {
		Map<String, Object> data = new HashMap<String, Object>();
		Customer customer = customerService.getLogin();
		String error = null;
		Setting setting = settingService.getSetting();
		Calendar ca = Calendar.getInstance();
		int time = ca.get(Calendar.HOUR_OF_DAY) * 1000 + ca.get(Calendar.MINUTE);
		if (customer == null) {
			error = "Vui lòng truy cập vào web và đăng nhập lại";
		} if (time > setting.getEndTime()) {
			error = "Đã hết thời gian đặt món online. Vui lòng liên hệ theo hotline.";
		} else if (order.ids == null || order.ids.isEmpty()) {
			error = "Chưa chọn món ăn";
		} else if (order.quantity == null || order.miniQuantity == null || (order.quantity.isEmpty() && order.miniQuantity.isEmpty())) {
			error = "Chưa nhập số số lượng món ăn";
		} else if (order.quantity.size() != order.miniQuantity.size()) {
			error = "Dữ liệu đặt món không đồng bộ";
		}
		if (error != null) {
			data.put("error", error);
		} else {
			// prevent many submit
			if (orderService.findByCode(order.code) == null) {
				OrderList orderList = new OrderList();
				orderList.setCustomer(customer);
				List<OrderDetail> details = new ArrayList<OrderDetail>();
				orderList.setDetails(details);
				for (int i = 0; i < order.ids.size(); i++) {
					OrderDetail detail = new OrderDetail();
					OrderItem orIt = orderItemService.find(order.ids.get(i));
					if (orIt == null) {
						continue;
					}
					detail.setOrderItemId(orIt.getId());
					detail.setNumber(order.quantity.get(i));
					if (order.miniQuantity != null && !order.miniQuantity.isEmpty()) {
						detail.setMiniNumber(order.miniQuantity.get(i));
					}
					detail.setPrice(orIt.getPrice());
					detail.setMiniNumber(orIt.getMiniPrice());
					if (order.description != null && !order.description.isEmpty()) {
						detail.setNote(order.description.get(i));
					}
					detail.setOrder(orderList);
					details.add(detail);
				}
				orderList.setCreatedDate(new Date());
				if (VIFUtils.isValid(order.getDate())) {
					orderList.setOrderedDate(VIFUtils.convertStringToDate(order.getDate(), "dd/MM/yyyy"));
				}
				orderService.add(orderList);
			}
			data.put("success", "Quý khách đã đặt món thành công. Chúng tôi sẽ liên hệ để xác nhận cho quý khách sớm nhất có thể. Xin cám ơn!");
		}
		return data;
	}	
	@ResponseBody
	@RequestMapping(value = "/ip")
	public String test() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpUriRequest request = new HttpGet("https://www.iplocation.net/find-ip-address");
			HttpResponse res = client.execute(request );
			BufferedReader br = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
			String line, content = "";
			while ((line = br.readLine()) != null) {
				content += line.replaceAll("src=\"", "src=\"https://www.iplocation.net/").replaceAll("href=\"", "href=\"https://www.iplocation.net/");
			}
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "IP not found!";
	}
}
