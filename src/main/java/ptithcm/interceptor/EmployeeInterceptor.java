package ptithcm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ptithcm.DAO.IAccountDAO.EnumRoleID;
import ptithcm.entity.Account;
import ptithcm.utility.DefineAttribute;

public class EmployeeInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		Account acc = (Account) session.getAttribute(DefineAttribute.UserAttribute);
		if (acc != null && acc.getRole().getRoleId().equals(EnumRoleID.EMPLOYEE.toString())) {
			return true;
		}
		response.sendRedirect(request.getContextPath() + "/index.htm");
		return false;

	}
}
