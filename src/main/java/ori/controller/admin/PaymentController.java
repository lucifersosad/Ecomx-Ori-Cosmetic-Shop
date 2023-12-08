package ori.controller.admin;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import ori.config.VNPAYConfig;
import ori.entity.Cart;
import ori.entity.Order;
import ori.entity.OrderDetail;
import ori.entity.OrderDetailKey;
import ori.entity.Product;
import ori.entity.User;
import ori.service.ICartService;
import ori.service.IOrderDetailService;
import ori.service.IOrderService;
import ori.service.IProductService;
import ori.service.IUserService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	IOrderService orderService;
	@Autowired
	IUserService userService;
	@Autowired
	IOrderDetailService orderDetailService;
	@Autowired
	ICartService cartService;
	@Autowired
	IProductService productService;
	@GetMapping("/index")
	
	
	public String index() {
		return "web/payment/index";
	}

	@GetMapping("/option")
	public String list(ModelMap model) {
		int amount = 20000;
		model.addAttribute("amount", amount);
		VNPAYConfig.userid = "1";
		VNPAYConfig.shipping_method = "Standard";
		return "web/payment/vnpay_pay";
	}

	@GetMapping("/ipn")
	public String  transaction(@RequestParam Map<String, String> queryParams, RedirectAttributes redirectAttributes) {
		Map<String, String> fields = new HashMap<>();
		
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			String fieldName = entry.getKey();
			String fieldValue = entry.getValue();
			
			try {
				fieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
				fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());

				if (fieldValue != null && !fieldValue.isEmpty()) {
					fields.put(fieldName, fieldValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String vnp_SecureHash = queryParams.get("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}
		String signValue = VNPAYConfig.hashAllFields(fields);
		String payStatus = "", message = "";
		if (signValue.equals(vnp_SecureHash)) {
			boolean checkOrderId = true; // Kiểm tra có đơn hàng này hay không
			boolean checkAmount = true; // Kiểm tra số tiền tài khoản so với tiền đơn hàng
			boolean checkOrderStatus = true; // Kiểm tra đơn hàng đã thanh toán hay chưa
			if (checkOrderId) {
				if (checkAmount) {
					if (checkOrderStatus) {
						if ("00".equals(queryParams.get("vnp_ResponseCode"))) {
							Order order = new Order();
							Optional<User> optUser = userService.findById(Integer.parseInt(VNPAYConfig.userid));
							User user = new User();
							if (optUser.isPresent()) {
								 user = optUser.get();
							}
							order.setUserId(user);
							order.setDate(queryParams.get("vnp_PayDate"));
							order.setPayment_method("VNPAY");
							order.setShipping_method(VNPAYConfig.shipping_method);
							order.setStatus(1);
							order.setTotal(Double.parseDouble(queryParams.get("vnp_Amount")));
							order.setCurrency("VND");
							payStatus = "1";
							message = queryParams.get("vnp_Amount");
							orderService.save(order);
							List<Order> orders = orderService.findAll();
							Order lastOrder = orders.get(orders.size()-1);
							List<Cart> carts = cartService.findByUserId(Integer.parseInt(VNPAYConfig.userid));
							for (Cart cart : carts) {
								OrderDetailKey orderDetailKey = new OrderDetailKey();
								orderDetailKey.setOrderId(lastOrder.getOrderId()); // Set the appropriate orderId
								orderDetailKey.setProId(cart.getProduct().getProId()); // Set the appropriate proId

								OrderDetail orderDetail = new OrderDetail();
								orderDetail.setId(orderDetailKey);
								orderDetail.setOrder(lastOrder);
								orderDetail.setProduct(cart.getProduct());
								orderDetail.setQuantity(cart.getQuantity());
								orderDetailService.save(orderDetail);
								Optional<Product> optPro = productService.findById(cart.getProduct().getProId());
								Product pro = new Product();
								if (optPro.isPresent()) {
									pro = optPro.get();
								}
								pro.setStock(pro.getStock()-cart.getQuantity());
								productService.save(pro);
								cartService.delete(cart);
							}
						
						} else {
							payStatus = "0";
							message = "Thanh toán không thành công";
						}
					} else {
						payStatus = "0";
						message = "Đơn hàng đã thanh toán";
					}
				} else {
					payStatus = "0";
					message = "Số tiền không hợp lệ";
				}
			} else {
				payStatus = "0";
				message = "Đơn hàng không được tìm thấy";
			}
		} else {
			payStatus = "0";
			message = "Checksum không hợp lệ";
		}
		redirectAttributes.addAttribute("payStatus", payStatus);
		redirectAttributes.addAttribute("message", message);
        return "redirect:/payment/return";
	}
	
	@GetMapping("/return")
	public String info(@RequestParam("payStatus") String payStatus, @RequestParam("message") String message, Model model) {
	    model.addAttribute("payStatus", payStatus);
	    model.addAttribute("message", message);
	    return "web/payment/vnpay_return";
	}
}
