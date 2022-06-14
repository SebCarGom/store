package ies.sotero.cstore.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ies.sotero.cstore.model.CustomUser;
import ies.sotero.cstore.model.Order;
import ies.sotero.cstore.model.Product;
import ies.sotero.cstore.service.IOrderService;
import ies.sotero.cstore.service.IProductService;
import ies.sotero.cstore.service.IUserService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController {

	@Autowired
	private IProductService productService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IOrderService orderService;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private final Logger LOGGER = LoggerFactory.getLogger(AdministratorController.class);

	@GetMapping("")
	public String home(Model model, HttpSession session) {

		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		List<Product> products = productService.findAll();

		model.addAttribute("products", products);

		return "administrator/home";
	}

	@GetMapping("/register")
	public String create(HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		return "administrator/register";
	}

	@PostMapping("/save")
	public String save(CustomUser user, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		LOGGER.info("User register: {}", user);

		user.setType("ADMIN");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.save(user);

		return "redirect:/";
	}

	@GetMapping("/users")
	public String users(Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "/user/home";
		}

		model.addAttribute("users", userService.findAll());
		return "administrator/users";
	}

	@GetMapping("/orders")
	public String orders(Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}
		model.addAttribute("orders", orderService.findAll());
		return "administrator/orders";
	}

	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable Integer id, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		LOGGER.info("Order id  {}", id);

		Order order = orderService.findById(id).get();

		model.addAttribute("details", order.getOrderDetail());

		return "administrator/order_detail";
	}
}
