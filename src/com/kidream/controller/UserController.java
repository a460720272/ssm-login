package com.kidream.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kidream.context.UserContext;
import com.kidream.persistence.pojo.User;
import com.kidream.persistence.service.IUserService;

/**
 * ��¼�����û�����
 * 
 * @author ����Ȫ
 *
 */
@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private IUserService userService;

	@RequestMapping("index")
	public String index() {
		return "index";
	}

	/**
	 * ��¼������
	 * 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping("checkLogin")
	public String checkLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session) {
		try {
			userService.checkLogin(username, password);
		} catch (Exception e) {
			session.setAttribute("errorMsg", e.getMessage());
			return "redirect:/login.jsp";
		}
		return "redirect:index";
	}

	/**
	 * ע����¼����
	 * 
	 * @return
	 */
	@RequestMapping("cancellation")
	public String cancellation() {
		UserContext.getSession().invalidate();
		return "redirect:/login.jsp";
	}

//	@RequestMapping("checkLogon")
//	public String checkLogon(@RequestParam("username") String username, @RequestParam("password") String password) {
//		System.out.println(username);
//		System.out.println(password);
//		User user = userService.getUserByUsername(username);
//		if (user != null) {
//			System.out.println(user);
//			return "redirect:/register.jsp";
//		} else {
//			userService.regist(username, password);
//			return "redirect:/login.jsp";
//		}
//	}

	/**
	 * ע������� �˺Ų�����ͬ�����������ͬ
	 * 
	 * @return
	 */
	@RequestMapping("regist")
	public String regist(@Validated User user, BindingResult bindingResult, Model model) {
		List<ObjectError> errors = bindingResult.getAllErrors();
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getId());
		if (errors.size() > 0) {
//			model.addAttribute("user",new User());
			model.addAttribute("errors", errors);
			System.out.println("�û����������ʽ����ȷ");
			return "forward:/register.jsp";
		}
		System.out.println(user.toString());
		User flag = userService.getUserByUsername(user.getUsername());
		if (flag != null) {
//			// ���˻��Ѿ������ˣ����ݿ������и��˻����������ظ�ע��
			model.addAttribute("error", "��ID�ѱ�ע��");
			return "forward:/register.jsp";
		}
		userService.regist(user.getUsername(), user.getPassword());
		return "redirect:/login.jsp";
	}
}
