package vn.vif.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.vif.models.OrderItem;
import vn.vif.services.OrderItemService;
import vn.vif.utils.VIFUtils;
@Controller
public class HomeController {

	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private OrderItemService orderItemService;

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
		
		Map<Integer, List<OrderItem>>[] data = orderItemService.getOrderItemData();
		uiModel.addAttribute("orderItemData", data[0]);
		uiModel.addAttribute("orderItemSpec", data[1]);
		
		/*Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MONTH, Calendar.JANUARY);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		for (int i = 0; i < 40; i++) {
			int currentWeek = ca.get(Calendar.WEEK_OF_YEAR);
			System.out.println(currentWeek + " " + ca.getTime());
			ca.add(Calendar.DAY_OF_MONTH, 1);
		}*/
		
		return "web";
	}


	@RequestMapping(value = "/web", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String homeWeb(HttpServletRequest request, Model uiModel) {
		
		
		return home(request, uiModel);
	}
}
