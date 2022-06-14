package ies.sotero.cstore.controller;

import java.io.IOException;
import java.util.Optional;

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
import org.springframework.web.multipart.MultipartFile;

import ies.sotero.cstore.model.CustomUser;
import ies.sotero.cstore.model.Product;
import ies.sotero.cstore.service.IProductService;
import ies.sotero.cstore.service.IUserService;
import ies.sotero.cstore.service.UploadFileService;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private IProductService productService;

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private IUserService userService;

	@GetMapping("")
	public String show(Model model, HttpSession session) {

		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		model.addAttribute("products", productService.findAll());
		return "products/show";
	}

	@GetMapping("/create")
	public String create() {
		return "products/create";
	}

	@PostMapping("/save")
	public String save(Product product, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		LOGGER.info("Object Product {}", product);

		CustomUser u = userService.findbyId(Integer.parseInt(session.getAttribute("userId").toString())).get();

		product.setUser(u);

		if (product.getId() == null) {
			String imageName = uploadFileService.saveImage(file);

			product.setImage(imageName);
		}

		productService.save(product);

		return "redirect:/products";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		Product product = new Product();

		Optional<Product> optionalProduct = productService.get(id);

		product = optionalProduct.get();

		LOGGER.info("Product searched: {}", product);

		model.addAttribute("product", product);

		return "products/edit";
	}

	@PostMapping("/update")
	public String update(Product product, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}
		Product p = new Product();

		p = productService.get(product.getId()).get();

		if (file.isEmpty()) {
			product.setImage(p.getImage());
		} else {
			if (!p.getImage().equals("default.jpg")) {
				uploadFileService.deleteImage(p.getImage());
			}

			String imageName = uploadFileService.saveImage(file);

			product.setImage(imageName);
		}
		LOGGER.info(p.getName());
		product.setUser(p.getUser());

		productService.update(product);

		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		Product p = new Product();
		p = productService.get(id).get();

		if (!p.getImage().equals("default.jpg")) {
			uploadFileService.deleteImage(p.getImage());
		}

		productService.delete(id);

		return "redirect:/products";
	}

	@PostMapping("/discount")
	public String discount(@RequestParam("discount") String discount, HttpSession session) throws IOException {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		LOGGER.info("Object Product {}", discount);

		productService.applyDiscount(Double.parseDouble(discount));

		return "redirect:/products";
	}

	@PostMapping("/undo_discount")
	public String undoDiscount(@RequestParam("undoDiscount") String undoDiscount, HttpSession session)
			throws IOException {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		LOGGER.info("Object Product {}", undoDiscount);

		productService.undoDiscountApplied(Double.parseDouble(undoDiscount));

		return "redirect:/products";
	}

}
