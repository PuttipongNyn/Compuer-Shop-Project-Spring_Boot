package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;


@Controller
public class ProductController {

	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping(value = { "/", "/store"})
	String store(Model map) {
		List<Product> product_list = productService.getAllActiveImages();
		map.addAttribute("product", product_list);
		return "store/shop";
	}

	
	@GetMapping(value = { "/add_product" })
	public String addProductPage() {
		return "add_product";
	}

	@PostMapping("/product/saveImageDetails")
	public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("name") String name,
			@RequestParam("price") double price, @RequestParam("quantity") int quantity,
			@RequestParam("description") String description, @RequestParam("category") String category,
			Model model, HttpServletRequest request,
			final @RequestParam("image") MultipartFile file) {
		try {
			// String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName,
						HttpStatus.BAD_REQUEST);
			}
			String[] names = name.split(",");
			String[] categorys = category.split(",");
			String[] descriptions = description.split(",");
			Date createDate = new Date();
			log.info("Name: " + names[0] + " " + filePath);
			log.info("description: " + descriptions[0]);
			log.info("price: " + price);
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
			Product product = new Product();
			product.setName(names[0]);
			product.setImage(imageData);
			product.setPrice(price);
			product.setQuantity(quantity);
			product.setDescription(descriptions[0]);
			product.setCategory(categorys[0]);
			product.setCreateDate(createDate);
			productService.saveImage(product);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/product_list/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Product> product)
			throws ServletException, IOException {
		log.info("Id :: " + id);
		product = productService.getImageById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(product.get().getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/product_list_productDetails")
	String showProductDetails(@RequestParam("id") Long id, Optional<Product> product, Model model) {
		try {
			log.info("Id :: " + id);
			if (id != 0) {
				product = productService.getImageById(id);

				log.info("products :: " + product);
				if (product.isPresent()) {
					model.addAttribute("id", product.get().getId());
					model.addAttribute("description", product.get().getDescription());
					model.addAttribute("category", product.get().getCategory());
					model.addAttribute("name", product.get().getName());
					model.addAttribute("price", product.get().getPrice());
					model.addAttribute("quantity", product.get().getQuantity());
					return "imagedetails";
				}
				return "redirect:/add_product";
			}
			return "redirect:/add_product";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/add_product";
		}
	}

	@GetMapping("/product_list")
	String show(Model map) {
		List<Product> product_list = productService.getAllActiveImages();
		map.addAttribute("product", product_list);
		return "product_list";
	}


	@GetMapping("/delete_product/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		productRepository.delete(product);
		return "redirect:/product_list";
	}

	@GetMapping("/update_product_{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));

		model.addAttribute("imageGallery", product);
		return "update_product";
	}

	@PostMapping("/update2_product/{id}")
	public String updateProduct(@PathVariable("id") long id, @Validated Product product, BindingResult result,
			Model model) {
//			System.out.println(id);
//			System.out.println(imageGallery.getId() + imageGallery.getName());
		if (result.hasErrors()) {
//				imageGallery.setId(id);
			return "product_list";
		}
		Date createDate = new Date();
//		byte[] imageData = file.getBytes();
//		product.setImage(imageData);
		product.setCreateDate(createDate);
		productRepository.save(product);
		return "redirect:/product_list";
	}

}