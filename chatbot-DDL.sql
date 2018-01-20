Drop schema IF exists chatbot;
Create Schema chatbot;
use chatbot;
Drop table if exists `job_applications`;
Drop table if exists `user`;

CREATE TABLE `job_applications` (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `fname` varchar(30) NOT NULL,
  `lname` varchar(40) NOT NULL,
  `email` varchar(60) NOT NULL,
  `mob` varchar(10) NOT NULL,
  `position` varchar(100) NOT NULL,
  `portfolio_website` varchar(100) DEFAULT NULL,
  `resume_file_name` varchar(100) NOT NULL,
  `salary_expected` varchar(20) NOT NULL,
  `startDate` date NOT NULL,
  `relocate` varchar(10) NOT NULL,
  `last_Company` varchar(100) DEFAULT NULL,
  `Comments` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`application_id`),
  KEY `id_index` (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `FName` varchar(30) NOT NULL,
  `LName` varchar(40) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(30) NOT NULL,
  `mobile` varchar(10) DEFAULT NULL,
  `sec_que` varchar(200) DEFAULT NULL,
  `sec_ans` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `mobile_UNIQUE` (`mobile`),
  KEY `user_username_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='Containing user details used for sign in or register.';

