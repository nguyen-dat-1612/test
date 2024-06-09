package ptithcm.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ptithcm.DAO.IAccountDAO;
import ptithcm.DAO.IAccountDAO.EnumRoleID;
import ptithcm.DAO.IOrderDAO;
import ptithcm.entity.Account;
import ptithcm.entity.Orders;

@Controller
@RequestMapping("/admin/dashboard") // Đặt URL cơ sở cho tất cả các phương thức trong lớp này
public class AdminControllerDashboard {
    
    @Autowired // Tự động tiêm (inject) đối tượng IOrderDAO
    private IOrderDAO orderDAO;

    @Autowired // Tự động tiêm (inject) đối tượng IAccountDAO
    private IAccountDAO accountDAO;

    @RequestMapping() // Xử lý các yêu cầu HTTP GET đến URL cơ sở
    public String index(ModelMap model) {
        // Khởi tạo biến tổng thu nhập từ đơn hàng
        double totalOrder = 0;
        
        // Lấy danh sách tất cả các đơn hàng từ cơ sở dữ liệu
        List<Orders> orders = orderDAO.getOrders();
        
        // Duyệt qua từng đơn hàng để tính tổng thu nhập từ các đơn hàng có trạng thái 2
        for (Orders o : orders) {
        	// Kiểm tra nếu trạng thái đơn hàng là 2 (hoàn thành)
            if (o.getStatus() == 2) {
            	 totalOrder += o.getPrice(); // Cộng giá trị đơn hàng vào tổng thu nhập
            }
               
        }

        // Lấy danh sách tất cả các tài khoản với vai trò GUEST từ cơ sở dữ liệu
        List<Account> client = accountDAO.listAccountWithRole(EnumRoleID.GUEST);

        // Đưa các thuộc tính vào model để hiển thị trên view
        model.addAttribute("totalOrder", orders.size()); // Tổng số đơn hàng
        model.addAttribute("totalEarning", totalOrder); // Tổng thu nhập từ các đơn hàng hoàn thành
        model.addAttribute("totalClient", client.size()); // Tổng số khách hàng (với vai trò GUEST)

        // Trả về tên view "admin/admin-dashboard" để hiển thị trang dashboard cho admin
        return "admin/admin-dashboard";
    }
}

