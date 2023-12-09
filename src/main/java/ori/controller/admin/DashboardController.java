package ori.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ori.service.IOrderService;

@Controller

@RequestMapping("admin")

public class DashboardController {
	@Autowired(required = true)
	IOrderService orderService;
	@RequestMapping({"", "/"})
	public String dashboard(ModelMap model) {
		model.addAttribute("reMonth",orderService.reOnCurrentMonth());
		model.addAttribute("reYear",orderService.reOnCurrentYear());
		model.addAttribute("reQuarter",orderService.reOnCurrentQuarter());
		model.addAttribute("rateCom",orderService.rateCom());

		return "admin/dashboard";
	}	
}
