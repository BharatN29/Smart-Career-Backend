package com.chatbot.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class UserController {

	String user;
	Map<String, String> userDetails = new HashMap<String, String>();

	@RequestMapping("/api/getLoginStatus")
	public Object checkLoginStatus() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		if (session.getAttribute("user") != null)
			return session.getAttribute("user");
		else {
			return "{\"message\": \"Not logged in.\"}";
		}
	}

	@RequestMapping("/api/logout")
	public void logout() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		if (session.getAttribute("user") != null) {
			session.removeAttribute("user");
			userDetails.clear();
		}
	}

	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public Object login(@RequestParam("uname") String uname, @RequestParam("pass") String pass) {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.removeAttribute("user");
		userDetails.clear();

		if (uname != null && pass != null && !uname.equals("") && !uname.equals("")) {

			String sql = "SELECT FNAME, LNAME, USERNAME, EMAIL, MOBILE FROM USER WHERE USERNAME='" + uname + "' AND PASSWORD='"
					+ pass + "'";

			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/chatbot", "root", "root");
					Statement stmt = conn.createStatement()) {
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {

					userDetails.put("fname", rs.getString(1));
					userDetails.put("lname", rs.getString(2));
					userDetails.put("uname", rs.getString(3));
					userDetails.put("email", rs.getString(4));
					userDetails.put("mob", rs.getString(5));
					session.setAttribute("user", userDetails);
					return userDetails;
				} else {
					return "{\"err\": \"Invalid username or password.\"}";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return "{\"err\": \"Some error occured, try again after some time.\"}";
			}
		} else {

			return "{\"err\": \"Username and password can't be empty\"}";
		}
	}

	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public Object register(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
			@RequestParam("email") String email, @RequestParam("uname") String uname, @RequestParam("pass") String pass,
			@RequestParam("mob") String mob, @RequestParam("sec_que") String sec_que,
			@RequestParam("sec_ans") String sec_ans) {
		if (email == null || fname == null || lname == null || pass == null || uname == null || pass.length() < 8
				|| pass.length() > 24 || email.length() < 1 || uname.length() < 1 || fname.length() < 1
				|| lname.length() < 1) {
			return "{\"err\": \"Some of the reuqired fields are not filled properly.\"}";
		} else {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.removeAttribute("user");
			userDetails.clear();
			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/chatbot", "root", "root");
					Statement stmt = conn.createStatement();) {
				ResultSet rs = stmt.executeQuery("select username from user where username='" + uname + "'");
				if (rs.next()) {
					return "{\"err\": \"Username already in use.\"}";
				} else {
					rs.close();
					String sql = "Insert into user (fname, lname, email, username, password, mobile, sec_que, sec_ans) values(?,?,?,?,?,?,?,?)";
					PreparedStatement preStmt = conn.prepareStatement(sql);
					preStmt.setObject(1, fname);
					preStmt.setObject(2, lname);
					preStmt.setObject(3, email);
					preStmt.setObject(4, uname);
					preStmt.setObject(5, pass);
					preStmt.setObject(6, mob);
					preStmt.setObject(7, sec_que);
					preStmt.setObject(8, sec_ans);
					int res = preStmt.executeUpdate();
					preStmt.close();
					if (res > 0) {
						userDetails.put("fname", fname);
						userDetails.put("lname", lname);
						userDetails.put("uname", uname);
						userDetails.put("email", email);
						session.setAttribute("user", userDetails);
						return userDetails;

					} else {
						return "{\"err\": \"Some error occured while creating account.\"}";
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "{\"err\": \"Some error occured.\"}";
		}
	}
	
	
}