/*
 Navicat Premium Data Transfer

 Source Server         : MySQL database
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : vaccine

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 12/09/2021 12:53:04
*/

DROP DATABASE IF EXISTS vaccine;
CREATE DATABASE vaccine;
USE vaccine;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_roles
-- ----------------------------
DROP TABLE IF EXISTS `account_roles`;
CREATE TABLE `account_roles`  (
  `account_id` int UNSIGNED NOT NULL,
  `role_id` int UNSIGNED NOT NULL,
  PRIMARY KEY (`account_id`, `role_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `account_roles_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `account_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_roles
-- ----------------------------
INSERT INTO `account_roles` VALUES (1, 1);
INSERT INTO `account_roles` VALUES (3, 1);
INSERT INTO `account_roles` VALUES (4, 1);
INSERT INTO `account_roles` VALUES (7, 1);
INSERT INTO `account_roles` VALUES (8, 1);
INSERT INTO `account_roles` VALUES (10, 1);
INSERT INTO `account_roles` VALUES (11, 1);
INSERT INTO `account_roles` VALUES (1, 2);
INSERT INTO `account_roles` VALUES (3, 2);
INSERT INTO `account_roles` VALUES (1, 3);
INSERT INTO `account_roles` VALUES (1, 4);
-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts`  (
  `user_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fullname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `age` tinyint UNSIGNED NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `citizen_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `prioritize` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  UNIQUE INDEX `citizen_id`(`citizen_id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES (1, 'admin', '$2a$10$pY76Hw313QzkyRXjL6RWcu3.tPUBD3AI9R3MQe55ujEW9q9ttGHoS', 'Administrator', 24, 'Hà Nội', '0981351511', 'admin@gmail.com', '132349018', 0);
INSERT INTO `accounts` VALUES (3, 'super', '$2a$10$bnKIzEU6b.cTGNDXYS5O/u4Fu.F89aI.48pWay2goBzbjqmmqDB/a', 'Administrator', 24, 'Hà Nội', '0981351512', 'superuser@gmail.com', '132349019', 0);
INSERT INTO `accounts` VALUES (4, 'user1', '$2a$10$le0Rc4Sqyaoj4J0mTIMD5OVoCVjzkEAYo1hM5PD9eIHNDErDkmWue', 'Người dùng 1', 24, 'Hà Nội', '0981351513', 'user1@gmail.com', '132349012', 0);
INSERT INTO `accounts` VALUES (7, 'user2', '$2a$10$6uHLC7EVaZUIPZzBCw4IhudOGUNCS.Dkw3fw5h1IGR5PkqM/BWs86', 'Người dùng 2', 24, 'Hà Nội', '0981351514', 'user2@gmail.com', '132349013', 0);
INSERT INTO `accounts` VALUES (8, 'user3', '$2a$10$I2r2dRIdz99Apohp6ZPwDeI8QFEZq6VP7fPfkWJrkGlVjBdChMI5W', 'Người dùng 3', 24, 'Hà Nội', '0981351515', 'user3@gmail.com', '132349014', 0);
INSERT INTO `accounts` VALUES (10, 'user4', '$2a$10$8yPy8jghK1wQhqRq1ju0yuhleVgffBZ7C94vt2P2rdKj7PHQpiQZi', 'Người dùng 4', 24, 'Hà Nội', '0981351516', 'user4@gmail.com', '132349015', 0);
INSERT INTO `accounts` VALUES (11, 'user5', '$2a$10$23dULqn7zF2YVBpSbVBI4eng5kKAC.lBqoAOKjD7GhjG/jQVu2WZu', 'Người dùng 5', 24, 'Hà Nội', '0981351517', 'user5@gmail.com', '132349016', 0);

-- ----------------------------
-- Table structure for injection_history
-- ----------------------------
DROP TABLE IF EXISTS `injection_history`;
CREATE TABLE `injection_history`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int UNSIGNED NOT NULL,
  `vaccine_id` int UNSIGNED NOT NULL,
  `location_id` int UNSIGNED NOT NULL,
  `date_injection` timestamp(0) NOT NULL,
  `status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Đang chờ',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `location_id`(`location_id`) USING BTREE,
  INDEX `vacine_id`(`vaccine_id`) USING BTREE,
  CONSTRAINT `injection_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `accounts` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `injection_history_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `locations` (`location_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `injection_history_ibfk_3` FOREIGN KEY (`vaccine_id`) REFERENCES `vaccines` (`vaccine_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Đăng ký tiêm' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of injection_history
-- ----------------------------
INSERT INTO `injection_history` VALUES (1, 1, 1, 1, '2021-09-12 22:45:11', 'Đang chờ');
INSERT INTO `injection_history` VALUES (2, 3, 4, 1, '2021-09-20 22:45:37', 'Đang chờ');
INSERT INTO `injection_history` VALUES (3, 4, 2, 2, '2021-09-20 12:44:04', 'Đang chờ');
INSERT INTO `injection_history` VALUES (4, 7, 3, 3, '2021-09-20 12:44:35', 'Đang chờ');
INSERT INTO `injection_history` VALUES (5, 8, 3, 1, '2021-09-20 12:44:54', 'Đang chờ');
INSERT INTO `injection_history` VALUES (6, 10, 4, 4, '2021-09-21 12:45:15', 'Đang chờ');
INSERT INTO `injection_history` VALUES (7, 11, 2, 2, '2021-09-20 12:45:35', 'Đang chờ');

-- ----------------------------
-- Table structure for locations
-- ----------------------------
DROP TABLE IF EXISTS `locations`;
CREATE TABLE `locations`  (
  `location_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `location_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Hà Nội',
  PRIMARY KEY (`location_id`) USING BTREE,
  UNIQUE INDEX `UKdkw8mrr8kuqilpr7ti75dx1b9`(`location_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Địa điểm tiêm' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of locations
-- ----------------------------
INSERT INTO `locations` VALUES (1, 'Hà Nội');
INSERT INTO `locations` VALUES (2, 'Hồ Chí Minh');
INSERT INTO `locations` VALUES (4, 'Sài Gòn');
INSERT INTO `locations` VALUES (3, 'Đà Nẵng');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `role_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (2, 'ROLE_ADMIN');
INSERT INTO `roles` VALUES (3, 'ROLE_MOD');
INSERT INTO `roles` VALUES (1, 'ROLE_USER');
INSERT INTO `roles` VALUES (4, 'ROLE_ROOT');

-- ----------------------------
-- Table structure for vaccines
-- ----------------------------
DROP TABLE IF EXISTS `vaccines`;
CREATE TABLE `vaccines`  (
  `vaccine_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `vaccine_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'Chưa nghiên cứu được',
  `price` decimal(10, 2) NOT NULL,
  `amount` int UNSIGNED NOT NULL,
  `expiry_date` timestamp(0) NOT NULL,
  `manufacture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`vaccine_id`) USING BTREE,
  UNIQUE INDEX `UKahbkqbep8p6y51tvxx2wm2fw2`(`vaccine_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Loại vaccine' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vaccines
-- ----------------------------
INSERT INTO `vaccines` VALUES (1, 'AstraZeneca', 'Vaccine COVID-19 do Tập đoàn AstraZeneca sản xuất', 1000.00, 100, '2022-07-01 22:41:16', 'AstraZeneca');
INSERT INTO `vaccines` VALUES (2, 'SPUTNIK V', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
INSERT INTO `vaccines` VALUES (3, 'Vero Cell', 'Chưa nghiên cứu được', 900.00, 200, '2023-03-10 22:43:38', 'Sinopharm');
INSERT INTO `vaccines` VALUES (4, 'Pfizer/BioNTech', 'Vaccine  của Pfizer/BioNTech đã được cấp phép sử dụng tại 111 quốc gia và vùng lãnh thổ', 1500.00, 300, '2023-06-10 22:44:17', 'Pfizer/BioNTech');
INSERT INTO `vaccines` VALUES (5, 'SPUTNIK V2', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
INSERT INTO `vaccines` VALUES (6, 'SPUTNIK V3', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
INSERT INTO `vaccines` VALUES (7, 'SPUTNIK V4', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
INSERT INTO `vaccines` VALUES (8, 'SPUTNIK V5', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
INSERT INTO `vaccines` VALUES (9, 'SPUTNIK V6', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
INSERT INTO `vaccines` VALUES (10, 'SPUTNIK V7', 'Chưa nghiên cứu được', 800.00, 100, '2022-02-01 22:42:53', 'Nga');
SET FOREIGN_KEY_CHECKS = 1;
