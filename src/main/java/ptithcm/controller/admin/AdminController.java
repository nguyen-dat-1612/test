package ptithcm.controller.admin;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ptithcm.DAO.IAccountDAO;
import ptithcm.bean.UploadFile;
import ptithcm.bean.UserBean;
import ptithcm.entity.Account;
import ptithcm.utility.DefineAttribute;

@Controller // Đánh dấu đây là một controller trong Spring MVC
@RequestMapping("/admin") // Định nghĩa tiền tố cho tất cả các URL liên quan đến admin

public class AdminController {

    @Autowired // Tự động tiêm (inject) đối tượng IAccountDAO
    private IAccountDAO accountDAO;

    @Autowired // Tự động tiêm (inject) bean có Qualifier là "accountImgDir"
    @Qualifier("accountImgDir")
    private UploadFile accountImgDir;

    // Xử lý yêu cầu GET đến "/admin/index"
    @RequestMapping("index")
    public String index(ModelMap model) {
        // Chuyển hướng đến trang bảng điều khiển của admin
        return "redirect:/admin/dashboard.htm";
    }
    
    // Xử lý yêu cầu GET đến "/admin/profile"
    @RequestMapping("profile")
    public String gInfo(ModelMap model, HttpSession session) {
        // Lấy thông tin tài khoản từ session
        Account acc = (Account) session.getAttribute(DefineAttribute.UserAttribute);
        // Nếu không tìm thấy tài khoản, chuyển hướng đến trang đăng nhập
        if (acc == null) {
            return "redirect:/guest.htm";
        }

        // Chuyển đổi thông tin tài khoản thành UserBean để hiển thị trong form
        UserBean userBean = new UserBean(acc.getEmail(), acc.getFirstName(), acc.getLastName(), acc.getPhoneNumber());
        userBean.setPassword("***************");
        // Đưa UserBean vào model để hiển thị trong view
        model.addAttribute(DefineAttribute.UserBeanAttribute, userBean);
        // Trả về tên view "admin/admin-profile" để hiển thị thông tin tài khoản
        return "admin/admin-profile";
    }

    // Xử lý yêu cầu POST đến "/admin/profile"
    @RequestMapping(value = "profile", method = RequestMethod.POST)
    public String pInfo(@Validated @ModelAttribute(DefineAttribute.UserBeanAttribute) UserBean user,
            BindingResult errors, HttpSession session, ModelMap modelMap) {
        // Lấy thông tin tài khoản từ session
        Account acc = (Account) session.getAttribute(DefineAttribute.UserAttribute);
        // Nếu không tìm thấy tài khoản, chuyển hướng đến trang đăng nhập
        if (acc == null) {
            return "redirect:/guest.htm";
        }

        // Nếu có lỗi trong dữ liệu đầu vào, trả về trang chỉnh sửa thông tin với thông báo lỗi
        if (errors.hasErrors()) {
            modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
            modelMap.addAttribute("message", false);
            return "admin/admin-profile";
        }

        // Cập nhật thông tin tài khoản
        acc.setLastName(user.getLastName());
        acc.setFirstName(user.getFirstName());
        acc.setPhoneNumber(user.getPhoneNumber());
        acc.setEmail(user.getEmail());

        // Xử lý ảnh đại diện
        File file = new File(accountImgDir.getPath() + user.getAvatar());
        if (file.exists())
            file.delete();
        String avatarPath = accountImgDir.getPath() + user.getAvatar();
        acc.setAvatar(user.getAvatar().getOriginalFilename());
        try {
            user.getAvatar().transferTo(new File(avatarPath));
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("message", false);
            modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
            return "admin/admin-category-form";
        }

        // Cập nhật tài khoản trong cơ sở dữ liệu
        accountDAO.updateAccount(acc);

        // Trả về trang chỉnh sửa thông tin tài khoản với thông báo thành công
        modelMap.addAttribute(DefineAttribute.UserBeanAttribute, user);
        modelMap.addAttribute("message", true);
        return "admin/admin-profile";
    }
	
    // Xử lý yêu cầu GET đến "/admin/logout"
    @RequestMapping("logout")
    public String logout(ModelMap model, HttpSession session) {
        // Xóa thông tin tài khoản và đặt lại số lượng sản phẩm trong giỏ hàng và danh sách mong muốn
        session.removeAttribute(DefineAttribute.UserAttribute);
        session.setAttribute("totalCart", 0);
        session.setAttribute("totalWishlist", 0);
        // Chuyển hướng đến trang đăng xuất của khách hàng
		return "redirect:/guest/logout.htm";
	}

}
