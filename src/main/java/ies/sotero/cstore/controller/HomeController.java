package ies.sotero.cstore.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ies.sotero.cstore.model.CustomUser;
import ies.sotero.cstore.model.Order;
import ies.sotero.cstore.model.OrderDetail;
import ies.sotero.cstore.model.Product;
import ies.sotero.cstore.service.IOrderDetailService;
import ies.sotero.cstore.service.IOrderService;
import ies.sotero.cstore.service.IProductService;
import ies.sotero.cstore.service.IUserService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IProductService productService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderDetailService orderDetailService;

	List<OrderDetail> details = new ArrayList<>();

	Order order = new Order();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		LOGGER.info("User session {}", session.getAttribute("userId"));

		model.addAttribute("products", productService.findAll());

		// session
		model.addAttribute("sessionHome", session.getAttribute("userId"));

		return "user/home";
	}

	@GetMapping("/kids")
	public String homeKids(Model model, HttpSession session) {
		LOGGER.info("User session {}", session.getAttribute("userId"));

		model.addAttribute("products", productService.findAll());

		// session
		model.addAttribute("sessionHome", session.getAttribute("userId"));

		return "user/kids";
	}

	@GetMapping("/men")
	public String homeMen(Model model, HttpSession session) {
		LOGGER.info("User session {}", session.getAttribute("userId"));

		model.addAttribute("products", productService.findAll());

		// session
		model.addAttribute("sessionHome", session.getAttribute("userId"));

		return "user/men";
	}

	@GetMapping("/women")
	public String homeWomen(Model model, HttpSession session) {
		LOGGER.info("User session {}", session.getAttribute("userId"));

		model.addAttribute("products", productService.findAll());

		// session
		model.addAttribute("sessionHome", session.getAttribute("userId"));

		return "user/women";
	}

	@GetMapping("/product_home/{id}")
	public String productHome(@PathVariable Integer id, Model model) {
		LOGGER.info("Id product sent as param {}", id);

		Product product = new Product();

		Optional<Product> optionalProduct = productService.get(id);

		product = optionalProduct.get();

		model.addAttribute("product", product);

		return "user/product_home";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model) {

		OrderDetail orderDetail = new OrderDetail();

		Product product = new Product();

		double sumTotal = 0;

		Optional<Product> optionalProduct = productService.get(id);

		LOGGER.info("Product added: {}", optionalProduct.get());

		LOGGER.info("Quantity: {}", quantity);

		product = optionalProduct.get();

		orderDetail.setQuantity(quantity);
		orderDetail.setPrice(product.getPrice());
		orderDetail.setName(product.getName());
		orderDetail.setTotal(product.getPrice() * quantity);
		orderDetail.setProduct(product);

		Integer productId = product.getId();

		boolean existing = details.stream().anyMatch(p -> p.getProduct().getId() == productId);

		if (!existing) {
			details.add(orderDetail);
		}

		sumTotal = details.stream().mapToDouble(dt -> dt.getTotal()).sum();

		order.setTotal(sumTotal);

		model.addAttribute("cart", details);
		model.addAttribute("order", order);

		return "user/cart";
	}

	@GetMapping("/delete/cart/{id}")
	public String deleteProductCart(@PathVariable Integer id, Model model) {
		List<OrderDetail> newOrders = new ArrayList<>();

		for (OrderDetail orderDetail : details) {
			if (orderDetail.getProduct().getId() != id) {
				newOrders.add(orderDetail);
			}
		}

		details = newOrders;

		double sumTotal = 0;

		sumTotal = details.stream().mapToDouble(dt -> dt.getTotal()).sum();

		order.setTotal(sumTotal);

		model.addAttribute("cart", details);
		model.addAttribute("order", order);

		return "user/cart";
	}

	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		model.addAttribute("cart", details);
		model.addAttribute("order", order);

		// session
		model.addAttribute("session", session.getAttribute("userId"));

		return "user/cart";
	}

	@PostMapping("/order")
	public String order(@RequestParam("delivery") String delivery, Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		CustomUser user = userService.findbyId(Integer.parseInt(session.getAttribute("userId").toString())).get();
		LOGGER.info(delivery);
		order.setDelivery(delivery.equalsIgnoreCase("true"));
		LOGGER.info("delivery" + order.isDelivery());

		model.addAttribute("cart", details);
		model.addAttribute("order", order);
		model.addAttribute("user", user);

		return "user/order_summary";
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date creationDate = new Date();

		order.setCreationDate(creationDate);

		order.setNumber(orderService.generateOrderNumber());

		CustomUser user = userService.findbyId(Integer.parseInt(session.getAttribute("userId").toString())).get();

		order.setUser(user);
		orderService.save(order);

		for (OrderDetail dt : details) {
			dt.setOrder(order);

			orderDetailService.save(dt);
		}

		order = new Order();

		details.clear();

		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProduct(@RequestParam String name, Model model, HttpSession session) {
		LOGGER.info("Product Name: {}", name);

		List<Product> products = productService.findAll().stream().filter(p -> p.getName().contains(name))
				.collect(Collectors.toList());

		model.addAttribute("products", products);
		LOGGER.info("User session {}", session.getAttribute("userId"));
		model.addAttribute("sessionSearch", session.getAttribute("userId"));

		return "user/show_all";
	}
}
