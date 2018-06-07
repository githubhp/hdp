//package com.br.mom.ms.controller.user_center;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.br.mom.ms.constent.ResultStatus;
//import com.br.mom.ms.constent.UserRole;
//import com.br.mom.ms.model.User;
//import com.br.mom.ms.service.UserService;
//import com.br.mom.ms.util.MdImplement;
//
///**
// *
// * @author xin.cao@100credit.com
// */
//@Controller
//@RequestMapping("/user_center")
//public class UserController {
//	@Autowired
//	private UserService userService;
//
//	/**
//	 * 用户管理
//	 *
//	 * @param httpServletRequest
//	 * @param model
//	 * @return
//	 * @throws java.io.IOException
//	 */
//	@RequestMapping(value = "/user")
//	public String userAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
//		// DatabaseServiceClient databaseService =
//		// DatabaseServiceClient.getInstance();
//		String action = httpServletRequest.getParameter("action");
//		if (action == null || "".equals(action)) {
//			User user = new User();
//			// List<User> users = databaseService.selectAllUserSelective(user);
//			List<User> users = userService.selectAllSelective(user);
//			model.addAttribute("resultStatuss", ResultStatus.values());
//			model.addAttribute("userRoles", UserRole.values());
//			model.addAttribute("users", users);
//			return "user_center/user";
//		} else if ("save".equals(action)) {
//			String[] ids = httpServletRequest.getParameterValues("ids[]");
//			if (ids != null) {
//				for (String id : ids) {
//					User user = new User();
//					String userRole = httpServletRequest.getParameter(id + ":userRole");
//					String account = httpServletRequest.getParameter(id + ":account");
//					String passwd = httpServletRequest.getParameter(id + ":passwd");
//					String email = httpServletRequest.getParameter(id + ":email");
//					String valied = httpServletRequest.getParameter(id + ":valied");
//					user.setId(Integer.valueOf(id));
//					if (account == null || account.isEmpty()) {
//						continue;
//					}
//					user.setUserRole(Integer.valueOf(userRole));
//					user.setAccount(account);
//					user.setPasswd(passwd);
//					user.setEmail(email);
//					user.setValied(Integer.valueOf(valied));
//					// databaseService.updateUserByPrimaryKeySelective(user);
//					userService.updateByPrimaryKeySelective(user);
//				}
//			}
//			String[] newUserRole = httpServletRequest.getParameterValues("new:userRole[]");
//			String[] newAccount = httpServletRequest.getParameterValues("new:account[]");
//			String[] newPasswd = httpServletRequest.getParameterValues("new:passwd[]");
//			String[] newEmail = httpServletRequest.getParameterValues("new:email[]");
//			String[] newValied = httpServletRequest.getParameterValues("new:valied[]");
//			if (newAccount != null) {
//				int len = newAccount.length;
//				for (int i = 0; i < len; i++) {
//					User user = new User();
//					if (newAccount[i] == null || newAccount[i].isEmpty()) {
//						continue;
//					}
//					if (newPasswd[i] == null || newPasswd[i].isEmpty()) {
//						continue;
//					}
//					if (newEmail[i] == null || newEmail[i].isEmpty()) {
//						continue;
//					}
//					user.setUserRole(Integer.valueOf(newUserRole[i]));
//					user.setAccount(newAccount[i]);
//					user.setPasswd(MdImplement.encodeMD5To32(newPasswd[i]));
//					user.setEmail(newEmail[i]);
//					user.setValied(Integer.valueOf(newValied[i]));
//					// databaseService.insertUserSelective(user);
//					userService.insertSelective(user);
//
//				}
//			}
//		} else if ("delete".equals(action)) {
//			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
//			if (selectedIds != null) {
//				for (String selectedId : selectedIds) {
//					// databaseService.deleteUserByPrimaryKey(Integer.valueOf(selectedId));\
//					userService.deleteByPrimaryKey(Integer.valueOf(selectedId));
//				}
//			}
//		}
//		return "redirect:/user_center/user";
//	}
//}
