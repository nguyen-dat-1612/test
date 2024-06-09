package ptithcm.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ptithcm.DAO.ICategoryDAO;
import ptithcm.DAO.ICouponDAO;
import ptithcm.DAO.IProductDAO;
import ptithcm.bean.ProductBean;
import ptithcm.bean.UploadFile;
import ptithcm.entity.Account;
import ptithcm.entity.Category;
import ptithcm.entity.Coupon;
import ptithcm.entity.Product;
import ptithcm.utility.Constants;
import ptithcm.utility.DefineAttribute;

@Controller
@RequestMapping("/admin/products")
public class AdminControllerProduct {

	@Autowired
	private ICategoryDAO categoryDAO;

	@Autowired
	private IProductDAO productDAO;

	@Autowired
	@Qualifier("productImgDir")
	private UploadFile productImgDir;

	@Autowired
	private ICouponDAO couponDAO;
	
	//Lấy thông tin sản phẩm
	@RequestMapping()
	public String gProductList(ModelMap model, HttpSession session, @RequestParam(value = "crrPage", required = false, defaultValue = "1") int crrPage) {

	    // Tạo danh sách ProductBean để chứa thông tin sản phẩm sau khi chuyển đổi từ Product
	    List<ProductBean> products = new ArrayList<>();
	    
	    // Lấy danh sách sản phẩm từ cơ sở dữ liệu thông qua productDAO
	    List<Product> listProducts = productDAO.listProducts();
	    
	    // Duyệt qua từng sản phẩm trong danh sách
	    for (Product product : listProducts) {
	        // Chuyển đổi từ Product sang ProductBean
	        ProductBean bean = new ProductBean(product);
	        // Thêm ProductBean vào danh sách products
	        products.add(bean);
	    }

	    // Tính chỉ số bắt đầu cho trang hiện tại
	    int startIndex = (crrPage - 1) * Constants.PRODUCT_PER_PAGE_IN_HOME;
	    
	    // Khởi tạo tổng số trang là 1
	    int totalPage = 1;
	    
	    // Kiểm tra số lượng sản phẩm để tính toán tổng số trang
	    if (products.size() <= Constants.PRODUCT_PER_PAGE_IN_HOME)
	        totalPage = 1;
	    else {
	        // Tính tổng số trang dựa trên số lượng sản phẩm và số sản phẩm trên mỗi trang
	        totalPage = products.size() / Constants.PRODUCT_PER_PAGE_IN_HOME;
	        // Nếu số sản phẩm không chia hết cho số sản phẩm trên mỗi trang, tăng thêm một trang
	        if (products.size() % Constants.PRODUCT_PER_PAGE_IN_HOME != 0) {
	            totalPage++;
	        }
	    }

	    // Thêm tổng số trang vào model
	    model.addAttribute("totalPage", totalPage);
	    // Thêm trang hiện tại vào model
	    model.addAttribute("crrPage", crrPage);
	    
	    // Thêm danh sách sản phẩm cho trang hiện tại vào model
	    model.addAttribute("products", products.subList(startIndex, Math.min(startIndex + Constants.PRODUCT_PER_PAGE_IN_HOME, products.size())));
	    
	    // Trả về tên view "admin/admin-product" để hiển thị danh sách sản phẩm
	    return "admin/admin-product";
	}

	
	// Cập nhật sản phẩm
	@RequestMapping(value = "update-product{id}.htm", method = RequestMethod.POST)
	public String pUpdateProduct(ModelMap model, @PathVariable("id") int id,
			@ModelAttribute("productForm") ProductBean product) {
		Product findProduct = productDAO.getProduct(id);

		if (findProduct != null) {
			// 1
			Category category = categoryDAO.getCategory(product.getCategoryId());
			if (category != null) {
				findProduct.setCategory(category);
			}

			// 2
			if (product.getDiscountId() != -1) {
				Coupon coupon = couponDAO.getCoupon(product.getDiscountId());
				if (coupon != null) {
					findProduct.setCoupon(coupon);
				}
			}

			// 3
			if (product.getImageFile() != null) {
				File file = new File(productImgDir.getPath() + product.getImageFile());
				if (file.exists())
					file.delete();

				String avatarPath = productImgDir.getPath() + product.getImageFile();
				findProduct.setImage(product.getImageFile().getOriginalFilename());

				try {
					product.getImageFile().transferTo(new File(avatarPath));
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("message", 1);
					model.addAttribute("productBean", product);
					return "admin/admin-createProduct";
				}
			}

			// 4
			findProduct.setProductName(product.getProductName());
			// 5
			findProduct.setPrice(product.getPrice());
			// 6
			findProduct.setQuantity(product.getQuantity());
			// 7
			findProduct.setDetail(product.getDetail());
			// 8
			findProduct.setPostingDate(product.getPostingDate());

			if (!productDAO.updateProduct(findProduct)) {
				System.out.println("checking error");
			}
		}

		return String.format("redirect:/admin/products.htm");
	}
	
	// Tìm kiếm sản phẩm
	@RequestMapping("searchProduct")
	public String search(@RequestParam(required = false, value = "search") String search,
	                     @RequestParam(required = false, value = "crrPage", defaultValue = "1") int crrPage, 
	                     ModelMap modelMap) {

	    // Tạo danh sách ProductBean để chứa thông tin sản phẩm sau khi chuyển đổi từ Product
	    List<ProductBean> products = new ArrayList<>();
	    
	    // Lấy danh sách sản phẩm từ cơ sở dữ liệu thông qua productDAO, lọc theo tên sản phẩm
	    List<Product> listProducts = productDAO.filterProductByName(search);
	    
	    // Duyệt qua từng sản phẩm trong danh sách
	    for (Product p : listProducts) {
	        // Chuyển đổi từ Product sang ProductBean
	        ProductBean bean = new ProductBean(p);
	        // Thêm ProductBean vào danh sách products
	        products.add(bean);
	    }

	    // Tính chỉ số bắt đầu cho trang hiện tại
	    int startIndex = (crrPage - 1) * Constants.PRODUCT_PER_PAGE_IN_HOME;
	    
	    // Khởi tạo tổng số trang là 1
	    int totalPage = 1;
	    
	    // Kiểm tra số lượng sản phẩm để tính toán tổng số trang
	    if (products.size() <= Constants.PRODUCT_PER_PAGE_IN_HOME) {
	        totalPage = 1;
	    } else {
	        // Tính tổng số trang dựa trên số lượng sản phẩm và số sản phẩm trên mỗi trang
	        totalPage = products.size() / Constants.PRODUCT_PER_PAGE_IN_HOME;
	        // Nếu số sản phẩm không chia hết cho số sản phẩm trên mỗi trang, tăng thêm một trang
	        if (products.size() % Constants.PRODUCT_PER_PAGE_IN_HOME != 0) {
	            totalPage++;
	        }
	    }

	    // Thêm danh sách sản phẩm cho trang hiện tại vào model
	    modelMap.addAttribute("products", products.subList(startIndex,
	            Math.min(startIndex + Constants.PRODUCT_PER_PAGE_IN_CATEGORY, products.size())));
	    
	    // Thêm trang hiện tại vào model
	    modelMap.addAttribute("crrPage", crrPage);
	    
	    // Thêm tổng số trang vào model
	    modelMap.addAttribute("totalPage", totalPage);
	    
	    // Trả về tên view "admin/admin-product" để hiển thị danh sách sản phẩm
	    return "admin/admin-product";
	}

	
	//Tạo sản phẩm mới
	@RequestMapping(value = "create-product.htm")
	public String pCreateProduct(ModelMap model) {

		ProductBean productBean = new ProductBean();
		model.addAttribute("productBeans", productBean);

		return "admin/admin-createProduct";

	}

	@RequestMapping(value = "create-product.htm", method = RequestMethod.POST)
	public String pCreateProduct(@ModelAttribute("productBean") ProductBean product, HttpSession session,
			ModelMap model) {
		Account acc = (Account) session.getAttribute(DefineAttribute.UserAttribute);
		if (acc == null) {
			return "redirect:employee/logout.htm";
		}
		Product newProduct = new Product();
		newProduct.setAccount(acc);
		Category category = categoryDAO.getCategory(product.getCategoryId());
		if (category != null) {
			newProduct.setCategory(category);
		} else {
			System.out.println(product.getCategoryId() + "doesnt exissst");
			return "redirect:/admin/products.htm";
		}

		if (product.getDiscountId() != -1) {
			Coupon coupon = couponDAO.getCoupon(product.getDiscountId());
			if (coupon != null) {
				newProduct.setCoupon(coupon);
			}
		}

		if (product.getImageFile() != null) {

			File file = new File(productImgDir.getPath() + product.getImageFile());
			if (file.exists())
				file.delete();

			String avatarPath = productImgDir.getPath() + product.getImageFile();
			newProduct.setImage(product.getImageFile().getOriginalFilename());

			try {
				product.getImageFile().transferTo(new File(avatarPath));
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", 1);
				model.addAttribute("productBean", product);
				return "admin/admin-createProduct";
			}

		}

		newProduct.setProductName(product.getProductName());
		newProduct.setPrice(product.getPrice());
		newProduct.setQuantity(product.getQuantity());
		newProduct.setDetail(product.getDetail());
		newProduct.setPostingDate(product.getPostingDate());

		if (!productDAO.insertProduct(newProduct)) {
			System.out.println("error in adding");
		}
		return String.format("redirect:/admin/products.htm");
	}
}
