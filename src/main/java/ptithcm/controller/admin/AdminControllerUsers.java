package ptithcm.controller.admin;

import java.io.File;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ptithcm.DAO.IAccountDAO;
import ptithcm.DAO.IAccountDAO.EnumRoleID;
import ptithcm.bean.UploadFile;
import ptithcm.bean.UserBean;
import ptithcm.entity.Account;
import ptithcm.entity.Role;
import ptithcm.utility.Constants;
import ptithcm.utility.DefineAttribute;

@Controller
@RequestMapping("/admin/user")
public class AdminControllerUsers {
	@Autowired
	private IAccountDAO accountDAO;

	@Autowired
	@Qualifier("accountImgDir")
	private UploadFile accountImgDir;

	@RequestMapping()
	public String index() {
		return "redirect:get-employee.htm";
	}
	
	//Admin đọc / lấy thông tin khách hàng
	@RequestMapping("get-guest")
	public String gUserListGuest(ModelMap modelMap,
			@RequestParam(value = "crrPage", required = false, defaultValue = "1") int crrPage) {
		
		// Lấy danh sách tài khoản với vai trò GUEST
		List<Account> accounts = accountDAO.listAccountWithRole(EnumRoleID.GUEST);
		
		// Tính chỉ số bắt đầu của trang hiện tại trong danh sách tài khoản
		int startIndex = (crrPage - 1) * Constants.USER_PER_PAGE;
		
		// Biến tổng số trang, mặc định là 1
		int totalPage = 1;
		
		// Nếu số lượng tài khoản ít hơn hoặc bằng số tài khoản trên mỗi trang
		if (accounts.size() <= Constants.USER_PER_PAGE)
			totalPage = 1; // Tổng số trang là 1
		else {
			 // Tính tổng số trang bằng cách chia số lượng tài khoản cho số tài khoản trên mỗi trang
			totalPage = accounts.size() / Constants.USER_PER_PAGE;
			
			// Nếu còn dư tài khoản sau phép chia, tăng tổng số trang lên 1
			if (accounts.size() % Constants.USER_PER_PAGE != 0) {
				totalPage++;
			}
		}
		// Thêm danh sách tài khoản của trang hiện tại vào modelMap
		modelMap.addAttribute("accounts", accounts.subList(startIndex, Math.min(startIndex + Constants.USER_PER_PAGE, accounts.size())));
		 // Thêm thông tin trang hiện tại vào modelMap
		modelMap.addAttribute("crrPage", crrPage);
		 // Thêm tổng số trang vào modelMap
		modelMap.addAttribute("totalPage", totalPage);
		// Thêm nguồn yêu cầu vào modelMap (để sử dụng trong giao diện)
		modelMap.addAttribute("source", "get-guest.htm");

		return "admin/admin-guest";
	}
	//Admin đọc / lấy thông tin danh sách nhân viên
	@RequestMapping("get-employee")
	public String gUserListEmployee(ModelMap modelMap,
			@RequestParam(value = "crrPage", required = false, defaultValue = "1") int crrPage) {
		
		// Lấy danh sách tài khoản với vai trò EMPLOYEE từ accountDAO
		List<Account> accounts = accountDAO.listAccountWithRole(EnumRoleID.EMPLOYEE);
		
		// Tính chỉ số bắt đầu của trang hiện tại trong danh sách tài khoản
		int startIndex = (crrPage - 1) * Constants.USER_PER_PAGE;
		
		// Biến tổng số trang, mặc định là 1
		int totalPage = 1;
		
		 // Nếu số lượng tài khoản ít hơn hoặc bằng số tài khoản trên mỗi trang
		if (accounts.size() <= Constants.USER_PER_PAGE)
			totalPage = 1;
		else {
			// Tính tổng số trang bằng cách chia số lượng tài khoản cho số tài khoản trên mỗi trang
			totalPage = accounts.size() / Constants.USER_PER_PAGE;
			
			// Nếu còn dư tài khoản sau phép chia, tăng tổng số trang lên 1	
			if (accounts.size() % Constants.USER_PER_PAGE != 0) {
				totalPage++;
			}
		}
		
		// Thêm danh sách tài khoản của trang hiện tại vào modelMap
		modelMap.addAttribute("accounts", accounts.subList(startIndex, Math.min(startIndex + Constants.USER_PER_PAGE, accounts.size())));
		// Thêm thông tin trang hiện tại vào modelMap
		modelMap.addAttribute("crrPage", crrPage);
		// Thêm tổng số trang vào modelMap
		modelMap.addAttribute("totalPage", totalPage);
		// Thêm nguồn yêu cầu vào modelMap (để sử dụng trong giao diện)
		modelMap.addAttribute("source", "get-employee.htm");
		
		return "admin/admin-employee";
	}
	
	//Cập nhật trạng thái của tài khoản
	@RequestMapping(value = "enable{id}")
	public String gEnableStatusUser(@PathVariable("id") int id, ModelMap modelMap,
			@RequestParam("source") String source) {

		Account acc = accountDAO.getAccount(id);
		if (acc != null) {
			System.out.println(acc.getStatus());
			if (acc.getStatus() == 1)
				acc.setStatus(0);
			else if (acc.getStatus() == 0)
				acc.setStatus(1);
			accountDAO.updateAccount(acc);
		}
		return "redirect:" + source;
	}
	
	// Tạo nhân viên admin-register-employee
	// Định nghĩa phương thức xử lý yêu cầu HTTP GET cho đường dẫn "create-employee"
	@RequestMapping(value = "create-employee", method = RequestMethod.GET)
	public String registerEmployee(ModelMap modelMap) {
		
		// Tạo đối tượng UserBean mới
		UserBean accEmploy = new UserBean();
		
		// Thêm đối tượng UserBean vào modelMap với tên thuộc tính được định nghĩa trong DefineAttribute.UserBeanAttribute
		modelMap.addAttribute(DefineAttribute.UserBeanAttribute, accEmploy);
		
		// Trả về tên view "admin/admin-register-employee" để hiển thị trang đăng ký nhân viên
		return "admin/admin-register-employee";
	}

	@RequestMapping(value = "create-employee", method = RequestMethod.POST)
	public String createEmployee(@Validated @ModelAttribute(DefineAttribute.UserBeanAttribute) UserBean user,
			BindingResult errors, RedirectAttributes reAttributes, ModelMap modelMap) {
		
		// Lấy vai trò EMPLOYEE từ cơ sở dữ liệu
		Role role = accountDAO.getRoleViaEnum(EnumRoleID.EMPLOYEE);
		Account account = null;
		
		// Kiểm tra xem có lỗi ràng buộc không
		if (!errors.hasErrors()) {
			
			// Nếu người dùng không tải lên avatar
			if (user.getAvatar().isEmpty()) {
			} 
			
			else {
				MultipartFile avatarFile = user.getAvatar();
				
				// Nếu người dùng có tải lên avatar
				File file = new File(accountImgDir.getPath() + avatarFile.getOriginalFilename());
				
				// Nếu tệp avatar đã tồn tại, xóa nó
				if (file.exists())
					file.delete();
				
				// Đường dẫn để lưu avatar
				String avatarPath = accountImgDir.getPath()  + avatarFile.getOriginalFilename();

				try {
					// Lưu tệp avatar lên đường dẫn đã chỉ định
					user.getAvatar().transferTo(new File(avatarPath));
					Thread.sleep(2000);
				} catch (Exception e) {
					// Nếu có lỗi, thêm thông báo lỗi vào modelMap và trả về trang chỉnh sửa người dùng
					e.printStackTrace();
					modelMap.addAttribute("alert", 3);
					modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
					return "admin/admin-edit-user";
				}
			}
			
			// Tạo đối tượng Account từ thông tin của UserBean
			account = new Account(role, user.getLastName(), user.getFirstName(), user.getEmail(), user.getPhoneNumber(),
					user.getAvatar().getOriginalFilename(), BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
				
			 // Nếu người dùng có chỉ định đường dẫn avatar riêng, cập nhật vào đối tượng Account
			if (!user.getAvatarDir().isEmpty()) {
				account.setAvatar(user.getAvatarDir());
			}
			
			// Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
			if (accountDAO.findAccountByEmail(user.getEmail()) != null) {
				modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
				modelMap.addAttribute("alert", 1);
				return "admin/admin-register-employee";
			}

			if (accountDAO.addAccountToDB(account)) {
				reAttributes.addFlashAttribute("alert", 2);
				return "redirect:create-employee.htm";

			}
		}
		modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
		return "admin/admin-register-employee";
	}

	// Chỉnh sửa người dùng admin-edit-user
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editEmployee(ModelMap modelMap, @RequestParam("id") int id) {
		
	    // Lấy thông tin tài khoản từ cơ sở dữ liệu dựa trên id
	    Account account = accountDAO.getAccount(id);
	    
	    // Tạo một đối tượng UserBean từ thông tin tài khoản
	    UserBean userBean = new UserBean(account.getEmail(), account.getFirstName(), account.getLastName(),
	            account.getPhoneNumber(), account.getAvatar());
	    
	    // Ẩn mật khẩu để không hiển thị trong trường hợp sửa đổi thông tin
	    userBean.setPassword("***************");
	    
	    // Thêm đối tượng UserBean và id của tài khoản vào modelMap
	    modelMap.addAttribute(DefineAttribute.UserBeanAttribute, userBean);
	    modelMap.addAttribute("id", id);
	    
	    // Trả về tên view "admin/admin-edit-user" để hiển thị trang chỉnh sửa người dùng
	    return "admin/admin-edit-user";
	}

	// Định nghĩa phương thức xử lý yêu cầu HTTP POST cho đường dẫn "edit"
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editEmployee(ModelMap modelMap,
	        @Validated @ModelAttribute(DefineAttribute.UserBeanAttribute) UserBean user, BindingResult errors,
	        ModelMap model, @RequestParam("id") int id) {

	    // Nếu có lỗi ràng buộc
	    if (errors.hasErrors()) {
	        // Thêm thông báo lỗi vào modelMap và trả về trang chỉnh sửa người dùng
	        modelMap.addAttribute("alert", 1);
	        return "admin/admin-edit-user";
	    }
	    
	    // Lấy thông tin tài khoản từ cơ sở dữ liệu dựa trên id
	    Account account = accountDAO.getAccount(id);
	    
	    // Cập nhật thông tin tài khoản với dữ liệu từ đối tượng UserBean
	    account.setLastName(user.getLastName());
	    account.setFirstName(user.getFirstName());
	    account.setPhoneNumber(user.getPhoneNumber());
	    account.setEmail(user.getEmail());
	    
	    // Nếu người dùng tải lên avatar mới
	    if (!user.getAvatar().isEmpty()) {
	        // Nếu tệp avatar đã tồn tại, xóa nó
	        File file = new File(accountImgDir.getPath() + user.getAvatar());
	        if (file.exists())
	            file.delete();

	        // Đường dẫn để lưu avatar
	        String avatarPath = accountImgDir.getPath() + user.getAvatar().getOriginalFilename();
	        account.setAvatar(user.getAvatar().getOriginalFilename());

	        try {
	            // Lưu tệp avatar lên đường dẫn đã chỉ định
	            user.getAvatar().transferTo(new File(avatarPath));
	            // Dừng lại 2 giây để đảm bảo tệp được lưu
	            Thread.sleep(2000);
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Nếu có lỗi, thêm thông báo lỗi vào modelMap và trả về trang chỉnh sửa người dùng
	            modelMap.addAttribute("alert", 3);
	            model.addAttribute(DefineAttribute.UserBeanAttribute, user);
	            return "admin/admin-edit-user";
	        }
	    }

	    // Cập nhật thông tin tài khoản trong cơ sở dữ liệu
	    if (accountDAO.updateAccount(account)) {
	        // Nếu cập nhật thành công, chuyển hướng đến trang danh sách người dùng tương ứng với vai trò của tài khoản
	        if (account.getRole().getRoleName().equals("Guest"))
	            return "redirect:get-guest.htm";
	        else
	            return "redirect:get-employee.htm";
	    }

	    // Nếu có lỗi trong quá trình cập nhật, thêm thông báo lỗi vào modelMap và trả về trang chỉnh sửa người dùng
	    modelMap.addAttribute("alert", 1);
	    modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
	    return "admin/admin-edit-user";
	}

	
	//Admin tạo tài khoản cho khách admin-register-guest
	@RequestMapping(value = "create-guest", method = RequestMethod.GET)
	public String registerGuest(ModelMap modelMap) {
		
		// Tạo đối tượng UserBean mới
		UserBean accEmploy = new UserBean();
		
		// Thêm đối tượng UserBean vào modelMap với tên thuộc tính được định nghĩa trong DefineAttribute.UserBeanAttribute
		modelMap.addAttribute(DefineAttribute.UserBeanAttribute, accEmploy);
		
		// Trả về tên view "admin/admin-register-guest" để hiển thị trang đăng ký khách
		return "admin/admin-register-guest";
	}

	@RequestMapping(value = "create-guest", method = RequestMethod.POST)
	public String createGuest(ModelMap model,
			@Validated @ModelAttribute(DefineAttribute.UserBeanAttribute) UserBean user, BindingResult errors,
			RedirectAttributes reAttributes, ModelMap modelMap) {
			
		// Lấy vai trò GUEST từ cơ sở dữ liệu
		Role role = accountDAO.getRoleViaEnum(EnumRoleID.GUEST);
		
		// Tạo đối tượng Account từ thông tin của UserBean
		Account account = new Account(role, user.getLastName(), user.getFirstName(), user.getEmail(),
				user.getPhoneNumber(), "", BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		
		// Kiểm tra xem có lỗi ràng buộc không
		if (!errors.hasErrors()) {
			if (!user.getAvatar().isEmpty()) {
				
				File file = new File(accountImgDir.getPath() + user.getAvatar().getOriginalFilename());
				// Nếu tệp avatar đã tồn tại, xóa nó
				if (file.exists())
					file.delete();
				
				// Đường dẫn để lưu avatar
				String avatarPath = accountImgDir.getPath() + user.getAvatar().getOriginalFilename();
				account.setAvatar(user.getAvatar().getOriginalFilename());

				try {
					// Lưu tệp avatar lên đường dẫn đã chỉ định
					user.getAvatar().transferTo(new File(avatarPath));
					Thread.sleep(2000);
				} catch (Exception e) {
					// Nếu có lỗi, thêm thông báo lỗi vào model và trả về trang hồ sơ tài khoản
					e.printStackTrace();
					model.addAttribute("message", 2);
					return "account/accountProfile";
				}
			}
			
			// Nếu người dùng có chỉ định đường dẫn avatar riêng, cập nhật vào đối tượng Account
			if (!user.getAvatarDir().isEmpty()) {
				account.setAvatar(user.getAvatarDir());
			}
			
			// Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
			if (accountDAO.findAccountByEmail(user.getEmail()) != null) {
				modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
				modelMap.addAttribute("alert", 1);
				return "admin/admin-register-guest";
			}
			
			// Thêm tài khoản mới vào cơ sở dữ liệu
			if (accountDAO.addAccountToDB(account)) {
				// Nếu thêm thành công, thêm thông báo thành công vào reAttributes và chuyển hướng đến trang đăng ký khách
				reAttributes.addFlashAttribute("alert", 2);
				return "redirect:create-guest.htm";

			}
		}
		// Nếu có lỗi, thêm đối tượng UserBean vào modelMap và trả về trang đăng ký khách
		modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
		return "admin/admin-register-guest";
	}
}
