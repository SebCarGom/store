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

import ies.sotero.cstore.model.Category;
import ies.sotero.cstore.service.ICategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	private final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

	// Categories
	@Autowired
	private ICategoryService categoryService;

	@GetMapping("")
	public String show(Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		model.addAttribute("categories", categoryService.findAll());
		return "products/show_category";
	}

	@GetMapping("/create")
	public String create(HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		return "products/create_category";
	}

	@PostMapping("/save")
	public String save(Category category, HttpSession session) throws IOException {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		LOGGER.info("Object Product {}", category);

		categoryService.save(category);

		return "redirect:/categories";
	}

	@PostMapping("/create")
	public String create(Category category, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		categoryService.save(category);

		return "redirect:/categories";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		Category category = new Category();

		Optional<Category> optionalCategory = categoryService.get(id);

		category = optionalCategory.get();

		LOGGER.info("Category searched: {}", category);

		model.addAttribute("category", category);

		return "products/edit_category";
	}

	@PostMapping("/update")
	public String update(Category category, HttpSession session) throws IOException {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}
		categoryService.update(category);

		return "redirect:/categories";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "user/home";
		}

		categoryService.delete(id);

		return "redirect:/categories";
	}

}
