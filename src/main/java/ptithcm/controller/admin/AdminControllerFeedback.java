package ptithcm.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ptithcm.DAO.IFeedbackDAO;
import ptithcm.entity.Feedback;
import ptithcm.utility.Constants;

@Controller
@RequestMapping("/admin/feedback")
public class AdminControllerFeedback {

    @Autowired // Tự động tiêm (inject) đối tượng IFeedbackDAO
    private IFeedbackDAO feedbackDAO;

    // Xử lý yêu cầu GET để lấy danh sách phản hồi
    @RequestMapping()
    public String gCategoryList(ModelMap model,
            @RequestParam(value = "crrPage", required = false, defaultValue = "1") int crrPage,
            @RequestParam(value = "filter", defaultValue = "0") int filter) {

        // Lấy danh sách phản hồi từ cơ sở dữ liệu
        List<Feedback> feedbacks = feedbackDAO.listFeedback();

        // Áp dụng bộ lọc dựa trên giá trị của filter
        if (filter == 1) {
            // Lọc phản hồi có đánh giá 1 sao
            feedbacks = feedbacks.stream().filter(r -> r.getRatingStar() == 1).collect(Collectors.toList());
        } else if (filter == 2) {
            // Lọc phản hồi có đánh giá 2 sao
            feedbacks = feedbacks.stream().filter(r -> r.getRatingStar() == 2).collect(Collectors.toList());
        } else if (filter == 3) {
            // Lọc phản hồi có đánh giá 3 sao
            feedbacks = feedbacks.stream().filter(r -> r.getRatingStar() == 3).collect(Collectors.toList());
        } else if (filter == 4) {
            // Lọc phản hồi có đánh giá 4 sao
            feedbacks = feedbacks.stream().filter(r -> r.getRatingStar() == 4).collect(Collectors.toList());
        } else if (filter == 5) {
            // Lọc phản hồi có đánh giá 5 sao
            feedbacks = feedbacks.stream().filter(r -> r.getRatingStar() == 5).collect(Collectors.toList());
        }

        // Tính toán số trang
        int startIndex = (crrPage - 1) * Constants.USER_PER_PAGE;
        int totalPage = 1;
        if (feedbacks.size() <= Constants.USER_PER_PAGE) {
            totalPage = 1;
        } else {
            totalPage = feedbacks.size() / Constants.USER_PER_PAGE;
            if (feedbacks.size() % Constants.USER_PER_PAGE != 0) {
                totalPage++;
            }
        }

        // Thêm các thuộc tính vào model để hiển thị trong view
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("crrPage", crrPage);
        model.addAttribute("list", feedbacks.subList(startIndex, Math.min(startIndex + Constants.USER_PER_PAGE, feedbacks.size())));
        model.addAttribute("filter", filter);
        return "admin/admin-feedback";
    }

    // Xử lý yêu cầu GET để thay đổi trạng thái phản hồi
    @RequestMapping(value = "enable")
    public String enableFeedback(@RequestParam("id") int id) {
        // Lấy phản hồi từ cơ sở dữ liệu dựa vào id
        Feedback feedback = feedbackDAO.getFeedback(id);
        if (feedback != null) {
            // Kiểm tra trạng thái hiện tại của phản hồi và chuyển đổi
            System.out.println(feedback.getStatus());
            if (feedback.getStatus() == 1)
                feedback.setStatus(0);
            else if (feedback.getStatus() == 0)
                feedback.setStatus(1);
            // Cập nhật phản hồi trong cơ sở dữ liệu
            feedbackDAO.updateFeedback(feedback);
        }

        // Chuyển hướng về trang danh sách phản hồi
        return "redirect:/admin/feedback.htm";
    }
}

