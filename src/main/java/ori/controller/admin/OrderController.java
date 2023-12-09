package ori.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

import ori.entity.Order;

import ori.model.OrderModel;
import ori.service.IOrderService;

@Controller

@RequestMapping("admin/orders")
public class OrderController {
	@Autowired(required = true)
	IOrderService orderSer;
	@RequestMapping("")
	public String list(ModelMap model) {
		List<Order> list = orderSer.findAll();
		model.addAttribute("order", list);
		return "admin/orders/list.html";
	}
	@GetMapping("edit/{orderId}")
	public ModelAndView edit(ModelMap model, @PathVariable("orderId") Integer orderId) {
		Optional<Order> optOrder = orderSer.findById(orderId);
		OrderModel OrderModel = new OrderModel();
		if (optOrder.isPresent()) {
			Order entity = optOrder.get();
			BeanUtils.copyProperties(entity, OrderModel);
			OrderModel.setIsEdit(true);
			model.addAttribute("order", OrderModel);
			return new ModelAndView("admin/orders/edit.html", model);
		}
		model.addAttribute("message", "Order is not existed!!!!");
		return new ModelAndView("forward:/admin/orders", model);
	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("order") OrderModel orderModel, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/orders/edit");
		}
		Order entity = new Order();
		//copy từ Model sang Entity
		BeanUtils.copyProperties(orderModel, entity);
		//gọi hàm save trong service
		orderSer.save(entity);
		//đưa thông báo về cho biến message
		String message = "";
		if (orderModel.getIsEdit() == true) {
			message = "Order is Edited!!!!!!!!";
		} else {
			message = "Order is saved!!!!!!!!";
		}
		model.addAttribute("message", message);
		//redirect về URL controller
		return new ModelAndView("forward:/admin/orders", model);
	}
	@GetMapping("delete/{orderId}")
	public ModelAndView delet(ModelMap model, @PathVariable("orderId") Integer orderId) {
		orderSer.deleteById(orderId);
		model.addAttribute("message", "Order is deleted!!!!");
		return new ModelAndView("redirect:/admin/orders", model);
	}
}
