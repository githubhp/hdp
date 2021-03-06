//package com.br.mom.ms.controller.core;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.ModelAndViewDefiningException;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.br.mom.ms.constent.Config;
//import com.br.mom.ms.constent.UserRole;
//import com.br.mom.ms.util.DateUtil;
//import com.br.mom.ms.util.VolecityUIUtil;
//
///**
// *
// * @author xin.cao@100credit.com
// */
//public class AdminRoleInterceptor extends HandlerInterceptorAdapter {
//
//	private static final String defaultUrl = "/forbidden";
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		HttpSession session = request.getSession();
//		session.setMaxInactiveInterval(86400);
//		String url = request.getRequestURI();
//		boolean isOk = true;
//		if (session.getAttribute(Config.USER_ROLE) != null) {
//			String userRole = (String) session.getAttribute(Config.USER_ROLE);
//			isOk = userRole.contains(UserRole.ADMIN.getKey());
//		} else {
//			isOk = false;
//		}
//		if (!isOk) {
//			ModelAndView mv = new ModelAndView(defaultUrl);
//			mv.addObject("forward", url);
//			throw new ModelAndViewDefiningException(mv);
//		}
//		return true;
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		HttpSession session = request.getSession();
//		if (modelAndView != null) {
//			if (session.getAttribute(Config.USER_ROLE) != null) {
//				String userRole = (String) session.getAttribute(Config.USER_ROLE);
//				if (userRole.contains(UserRole.COMMON.getKey())) {
//					modelAndView.addObject(UserRole.COMMON.getKey(), UserRole.COMMON.getKey());
//				}
//				if (userRole.contains(UserRole.ADMIN.getKey())) {
//					modelAndView.addObject(UserRole.ADMIN.getKey(), UserRole.ADMIN.getKey());
//				}
//				if (userRole.contains(UserRole.SUPER_ADMIN.getKey())) {
//					modelAndView.addObject(UserRole.SUPER_ADMIN.getKey(), UserRole.SUPER_ADMIN.getKey());
//				}
//			}
//			modelAndView.addObject("DateUtil", DateUtil.getInstance());
//			modelAndView.addObject("VolecityUIUtil", VolecityUIUtil.getInstance());
//		}
//	}
//
//}
