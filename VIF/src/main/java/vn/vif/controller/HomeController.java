package vn.vif.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.validator.util.privilegedactions.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import vn.vif.services.CustomerService;
import vn.vif.services.MenuItemService;
import vn.vif.services.OrderItemService;
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
				content += line;
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
