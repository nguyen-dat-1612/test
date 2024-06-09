package ptithcm.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.DAO.ICategoryDAO;
import ptithcm.bean.CategoryBean;
import ptithcm.bean.UploadFile;
import ptithcm.entity.Category;
import ptithcm.utility.Constants;

@Controller
@RequestMapping("/admin/category")
public class AdminControllerCategory {

	@Autowired
	private ICategoryDAO categoryDAO;

	@Autowired
	@Qualifier("categoryImgDir")
	private UploadFile categoryImgDir;

	// Lấy danh sách đơn đặt hàng
	@RequestMapping()
	public String gCategoryList(ModelMap model, @RequestParam(value = "crrPage", required = false, defaultValue = "1") int crrPage,
	                            				@RequestParam(value = "filter", defaultValue = "0") int filter) {

	    // Lấy danh sách các danh mục từ categoryDAO
	    List<Category> categories = categoryDAO.getListCategories();

	    // Tính chỉ số bắt đầu của danh mục trên trang hiện tại
	    int startIndex = (crrPage - 1) * Constants.USER_PER_PAGE;
	    int totalPage = 1;

	    // Tính toán tổng số trang
	    if (categories.size() <= Constants.USER_PER_PAGE)
	        totalPage = 1;
	    else {
	        totalPage = categories.size() / Constants.USER_PER_PAGE;
	        if (categories.size() % Constants.USER_PER_PAGE != 0) {
	            totalPage++;
	        }
	    }

	    // Thêm tổng số trang và trang hiện tại vào model
	    model.addAttribute("totalPage", totalPage);
	    model.addAttribute("crrPage", crrPage);

	    // Chuyển đổi danh sách Category sang danh sách CategoryBean và chỉ lấy phần của trang hiện tại
	    List<CategoryBean> categoriesBean = CategoryBean.ConvertListCategory( categories.subList(startIndex, Math.min(startIndex + Constants.USER_PER_PAGE, categories.size())),categoryDAO);

	    // Lọc danh sách CategoryBean theo filter
	    if (filter == 1) {
	        // Lọc ra các danh mục không có sản phẩm
	        categoriesBean = categoriesBean.stream().filter(r -> r.getProducts().size() == 0).collect(Collectors.toList());
	    } else if (filter == 2) {
	        // Lọc ra các danh mục có ít nhất một sản phẩm
	        categoriesBean = categoriesBean.stream().filter(r -> r.getProducts().size() > 0).collect(Collectors.toList());
	    }

	    // Thêm danh sách CategoryBean và giá trị của filter vào model
	    model.addAttribute("list", categoriesBean);
	    model.addAttribute("filter", filter);

	    // Trả về tên view "admin/admin-category" để hiển thị danh sách danh mục
	    return "admin/admin-category";
	}

	
	//Tìm kiếm loại sản phẩm
	@RequestMapping("searchCategory")
	public String gCategoryWithSearch(@RequestParam(required = false, value = "search") String search,
			@RequestParam(required = false, value = "crrPage", defaultValue = "1") int crrPage, ModelMap model) {
		List<Category> categories = categoryDAO.searchCategory(search);
		int startIndex = (crrPage - 1) * Constants.USER_PER_PAGE;
		int totalPage = 1;
		if (categories.size() <= Constants.USER_PER_PAGE)
			totalPage = 1;
		else {
			totalPage = categories.size() / Constants.USER_PER_PAGE;
			if (categories.size() % Constants.USER_PER_PAGE != 0) {
				totalPage++;
			}
		}

		model.addAttribute("crrPage", crrPage);
		model.addAttribute("totalPage", totalPage);
		ArrayList<CategoryBean> categoriesBean = CategoryBean.ConvertListCategory(categories.subList(startIndex,
				Math.min(startIndex + Constants.PRODUCT_PER_PAGE_IN_HOME, categories.size())), categoryDAO);
		model.addAttribute("list", categoriesBean);
		model.addAttribute("filter", 0);
		return "admin/admin-category";
	}
	
	// Thêm loại sản phẩm mới
	@RequestMapping("add")
	public String gCategoryAdd(ModelMap modelMap) {
	    // Tạo một đối tượng Category mới
	    Category category = new Category();
	    
	    // Tạo một CategoryBean từ đối tượng Category, với các thuộc tính ban đầu
	    CategoryBean categoryBean = new CategoryBean(category.getCategoryId(), category.getName(), category.getImage());
	    
	    // Đưa CategoryBean vào modelMap với key là "addBean" để hiển thị trên form
	    modelMap.addAttribute("addBean", categoryBean);
	    
	    // Trả về tên view "admin/admin-category-form" để hiển thị form thêm danh mục mới
	    return "admin/admin-category-form";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String pCategoryAdd(@ModelAttribute("addBean") CategoryBean categoryBean, ModelMap model) {
	    // Kiểm tra nếu categoryBean không null
	    if (categoryBean != null) {
	        // Tạo một đối tượng Category mới
	        Category category = new Category();
	        
	        // Đặt tên danh mục từ categoryBean
	        category.setName(categoryBean.getName());
	        
	        // Kiểm tra nếu không có hình ảnh tải lên
	        if (categoryBean.getFileImage().isEmpty()) {
	            // Không làm gì cả nếu không có hình ảnh tải lên
	        } else {
	            // Xóa file hình ảnh cũ nếu tồn tại
	            File file = new File(categoryImgDir.getPath() + categoryBean.getFileImage().getOriginalFilename());
	            if (file.exists())
	                file.delete();
	            
	            // Đặt đường dẫn và tên file hình ảnh mới
	            String avatarPath = categoryImgDir.getPath() + categoryBean.getFileImage().getOriginalFilename();
	            category.setImage(categoryBean.getFileImage().getOriginalFilename());
	            
	            try {
	                // Lưu file hình ảnh mới vào đường dẫn chỉ định
	                categoryBean.getFileImage().transferTo(new File(avatarPath));
	                
	                // Tạm dừng để đảm bảo file đã được lưu (thường không cần thiết nhưng có thể được sử dụng để kiểm tra hoặc debug)
	                Thread.sleep(2000);
	            } catch (Exception e) {
	                // Xử lý ngoại lệ nếu có lỗi trong quá trình lưu file
	                e.printStackTrace();
	                
	                // Thêm thông báo lỗi vào model và trả về form để người dùng sửa lỗi
	                model.addAttribute("message", 1);
	                model.addAttribute("categoryBean", categoryBean);
	                return "admin/admin-category-form";
	            }
	        }
	        
	        // Thêm danh mục mới vào cơ sở dữ liệu
	        categoryDAO.addCategory(category);
	        
	        // Thêm thông báo thành công vào model
	        model.addAttribute("message", 2);
	    }
	    
	    // Chuyển hướng về trang danh sách danh mục
	    return "redirect:/admin/category.htm";
	}


	@Autowired
	@Qualifier("categoryImgDir")
	private UploadFile uploadCategory;
	
	// Cập nhật thông tin sản phẩm - Phương thức GET để hiển thị form cập nhật
	@RequestMapping("update{id}")
	public String gCategoryUpdate(@PathVariable("id") int id, ModelMap modelMap) {
	    // Lấy thông tin danh mục dựa vào id
	    Category category = categoryDAO.getCategory(id);
	    // Chuyển đổi thông tin danh mục thành CategoryBean
	    CategoryBean categoryBean = new CategoryBean(category);
	    // Đưa CategoryBean vào model để hiển thị trong form cập nhật
	    modelMap.addAttribute("updateBean", categoryBean);
	    // Trả về tên view "admin/admin-category-form" để hiển thị form cập nhật
	    return "admin/admin-category-form";
	}

	// Cập nhật thông tin sản phẩm - Phương thức POST để xử lý việc cập nhật
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String pCategoryUpdate(@ModelAttribute("updateBean") CategoryBean categoryBean, ModelMap model) {
	    // Lấy thông tin danh mục hiện tại dựa vào id của CategoryBean
	    Category category = categoryDAO.getCategory(categoryBean.getId());
	    if (category != null) {
	        // Cập nhật tên danh mục
	        category.setName(categoryBean.getName());

	        // Kiểm tra xem có cập nhật hình ảnh hay không
	        if (categoryBean.getFileImage().isEmpty()) {
	            // Nếu không có hình ảnh mới thì không làm gì
	        } else {
	            // Xóa file hình ảnh cũ nếu tồn tại
	            File file = new File(categoryImgDir.getPath() + categoryBean.getFileImage().getOriginalFilename());
	            if (file.exists())
	                file.delete();
	            
	            // Lưu đường dẫn của file hình ảnh mới
	            String avatarPath = String.format("%s%s", categoryImgDir.getPath(), categoryBean.getFileImage().getOriginalFilename());
	            category.setImage(categoryBean.getFileImage().getOriginalFilename());

	            try {
	                // Chuyển file hình ảnh mới tới đường dẫn đã xác định
	                categoryBean.getFileImage().transferTo(new File(avatarPath));
	                // Dừng chương trình 2 giây để đảm bảo file được lưu trữ (có thể không cần thiết)
	                Thread.sleep(2000);
	            } catch (Exception e) {
	                e.printStackTrace();
	                // Nếu có lỗi, đưa thông báo lỗi vào model và trả về form cập nhật
	                model.addAttribute("message", 1);
	                model.addAttribute("categoryBean", categoryBean);
	                return "admin/admin-category-form";
	            }
	        }
	        // Cập nhật danh mục trong cơ sở dữ liệu
	        categoryDAO.updateCategory(category);
	        // Đưa thông báo thành công vào model
	        model.addAttribute("message", 2);
	    }

	    // Chuyển hướng về trang danh sách danh mục
	    return "redirect:/admin/category.htm";
	}


	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String pCategoryDelete(@RequestParam("id") int id, RedirectAttributes reAttributes) {
		Category category = categoryDAO.getCategory(id);
		if (categoryDAO.deleteCategory(category)) {
			reAttributes.addFlashAttribute("alert", 2);
		} else {
			reAttributes.addFlashAttribute("alert", 1);
		}

		return "redirect:/admin/category.htm";
	}
}
