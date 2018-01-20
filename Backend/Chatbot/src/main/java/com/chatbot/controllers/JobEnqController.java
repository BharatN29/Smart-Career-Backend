package com.chatbot.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class JobEnqController {

	@RequestMapping(value = "/api/resumeUpload", method = RequestMethod.POST)
	public Object uploadResumeDetails(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
			@RequestParam("email") String email, @RequestParam("mob") String mob, @RequestParam("pos") String pos,
			@RequestParam("resumeFile") MultipartFile file, @RequestParam("pweb") String pweb,
			@RequestParam("sal") String sal, @RequestParam("startDate") String strtDate,
			@RequestParam("relocate") String relocate, @RequestParam("lastComp") String lastComp,
			@RequestParam("comments") String comments) {

		if (email == null || fname == null || lname == null || mob == null || pos == null || sal == null
				|| strtDate == null || relocate == null || mob.length() != 10 || email.length() < 1 || pos.length() < 1
				|| fname.length() < 1 || sal.length() < 1 || lname.length() < 1 || strtDate.length() < 1
				|| relocate.length() < 1 || file.isEmpty()) {
			return "{\"err\": \"Some of the reuqired fields are not filled properly.\"}";
		} else {

			try {
				DateFormat df = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);
				Date startDate = df.parse(strtDate);
				String sql = "INSERT INTO JOB_APPLICATIONS (FNAME, LNAME, EMAIL, MOB, POSITION, PORTFOLIO_WEBSITE, RESUME_FILE_NAME, SALARY_EXPECTED, STARTDATE, RELOCATE, LAST_COMPANY, COMMENTS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/chatbot", "root",
						"root"); Statement stmt = conn.createStatement(); PreparedStatement prestmt = conn.prepareStatement(sql);) {

					ResultSet rs = stmt.executeQuery("SELECT MAX(application_id) from job_applications");
					int nxt_ID = 0;
					if (rs.next()) {
						nxt_ID = rs.getInt(1);
					}
					if (nxt_ID > 0) {
						nxt_ID++;
					} else {
						nxt_ID = 1;
					}
					String path = Paths.get(".").toAbsolutePath().normalize().toFile().toString()
							+ "\\src\\main\\resources\\uploadedFiles\\resume\\" + "Resume_" + fname + nxt_ID + ".pdf";
					file.transferTo(new File(path));
					rs.close();
					prestmt.setObject(1, fname);
					prestmt.setObject(2, lname);
					prestmt.setObject(3, email);
					prestmt.setObject(4, mob);
					prestmt.setObject(5, pos);
					prestmt.setObject(6, pweb);
					prestmt.setObject(7, "Resume_" + fname + nxt_ID + ".pdf");
					prestmt.setObject(8, sal);
					prestmt.setObject(9, startDate);
					prestmt.setObject(10, relocate);
					prestmt.setObject(11, lastComp);
					prestmt.setObject(12, comments);
					prestmt.execute();
					
					return "{\"msg\": \"Details submitted successfully.\"}";
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ParseException e) {
				return "{\"err\": \"Invalid date format.\"}";
			} catch (IllegalStateException e) {
				return "{\"err\": \"Invalid file format.\"}";
			} catch (IOException e) {
				return "{\"err\": \"Invalid file format.\"}";
			}
		}
		return "{\"err\": \"Some error occurred, please try again after some time.\"}";
	}
}
