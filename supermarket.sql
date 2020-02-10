/*
Navicat MySQL Data Transfer

Source Server         : lyj
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : supermarket

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2020-02-10 22:10:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cart`
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `checked` int(1) unsigned zerofill DEFAULT '0' COMMENT '0表示未结算，1表示已结算，后面不显示',
  `quantity` int(11) DEFAULT NULL COMMENT '商品数量',
  `cart_sumprice` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES ('15', '4', '5', '1', '1', '1.5');
INSERT INTO `cart` VALUES ('16', '4', '7', '1', '8', '88');
INSERT INTO `cart` VALUES ('17', '5', '13', '1', '1', '1');
INSERT INTO `cart` VALUES ('18', '5', '11', '1', '5', '65');
INSERT INTO `cart` VALUES ('19', '4', '14', '1', '2', '24');
INSERT INTO `cart` VALUES ('20', '4', '6', '1', '4', '400');
INSERT INTO `cart` VALUES ('21', '4', '12', '1', '4', '48');
INSERT INTO `cart` VALUES ('22', '4', '26', '1', '1', '12');
INSERT INTO `cart` VALUES ('25', '4', '14', '1', '1', '12');
INSERT INTO `cart` VALUES ('26', '4', '15', '1', '1', '213');
INSERT INTO `cart` VALUES ('27', '4', '17', '1', '1', '1');
INSERT INTO `cart` VALUES ('28', '4', '18', '1', '1', '1');
INSERT INTO `cart` VALUES ('29', '4', '19', '1', '1', '11');

-- ----------------------------
-- Table structure for `payinfo`
-- ----------------------------
DROP TABLE IF EXISTS `payinfo`;
CREATE TABLE `payinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `order_no` bigint(20) DEFAULT NULL,
  `pay_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付状态',
  `create_time` datetime DEFAULT NULL,
  `trade_no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pau_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of payinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '是否上下架，0表示上架，1表示下架',
  `quantity` int(11) DEFAULT NULL COMMENT '库存',
  `price` decimal(20,2) DEFAULT NULL,
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '暂无其它介绍' COMMENT '商品描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '奶茶', '0', '35', '12.00', '80c9e762-5a48-4ff5-bcfc-8921db80b4e7.jpg', '这是非常好喝的美味的奶茶');
INSERT INTO `product` VALUES ('5', '鸡蛋', '0', '15', '1.50', 'b2686c5a-1d89-4812-96f4-adb585cbf67d.jpg', '进口本地鸡蛋');
INSERT INTO `product` VALUES ('6', '火龙果', '0', '10', '100.00', '312ab0c2-37fc-4876-a9eb-4721932e322c.jpg', '世界各地进口活力火龙果');
INSERT INTO `product` VALUES ('7', '牛奶', '0', '142', '11.00', '2c3d68ee-fc5a-49e6-876a-5881011ef5ff.jpg', '纯牛奶');
INSERT INTO `product` VALUES ('11', '果冻', '0', '8', '13.00', 'ce0daa45-504f-4400-9309-67fde7b7fbf0.jpg', '果冻的肉是用新鲜的水果制成的');
INSERT INTO `product` VALUES ('12', '柠檬', '0', '96', '12.00', 'e6acac14-2651-4b30-bd39-3439666e4006.jpg', '酸酸的柠檬');
INSERT INTO `product` VALUES ('13', '可乐', '1', '0', '1.00', '0ec19ada-f3ef-47f5-89d6-a6ae88d89e90.jpg', '1111');
INSERT INTO `product` VALUES ('14', '咖啡', '0', '9', '12.00', '57ca0798-c914-41d5-bcc3-58d395a3e6c1.jpg', '12121');
INSERT INTO `product` VALUES ('15', '腊肉', '0', '122', '213.00', 'acfca594-53bd-40eb-8fd0-014e2fb4a7f2.jpg', '腊（xī là 同“臘”）肉是指肉经腌制后再经过烘烤（或日光下曝晒）的过程所制成的加工品。腊肉的防腐能力强，能延长保存时间，并增添特有的风味，这是与咸肉的主要区别。腊肉并非因为在腊月所制，而为腊肉，腊月的腊（là）与腊肉的腊（xī）在古文里并非同一个字，亦即，腊月的腊是繁体的腊，而腊肉的腊本来就是腊月的腊的简化字。所以，腊肉之所以称为腊肉，至于为什么现在人们都读là，而不读xī，除了简化字的原因使两个字没有了区别以外，可能确实跟腊肉一般都在腊月里制作以待年夜饭之用有关。');
INSERT INTO `product` VALUES ('17', '5', '1', '0', '1.00', '4b8ea4f8-4ea6-4459-a75e-f19e82491714.jpg', '111111');
INSERT INTO `product` VALUES ('18', 'w', '1', '0', '1.00', '5e3ff215-4cd7-41bd-bdb1-e41c93b6df9f.jpg', '111111');
INSERT INTO `product` VALUES ('19', 'a', '1', '0', '11.00', 'c86f2834-d260-488f-8924-9939a07f8d88.jpg', '1');
INSERT INTO `product` VALUES ('20', '121', '0', '1', '1.00', '396bfb34-226e-4dfc-9a5c-e354d197d518.jpg', '111');
INSERT INTO `product` VALUES ('21', '123', '0', '1', '1.00', 'd0fe7920-4a48-4d5b-ae3d-f203f7632f72.jpg', '1');
INSERT INTO `product` VALUES ('26', '萝卜', '0', '12', '12.00', 'f4881b96-a955-42b0-88b5-48eb707bfc9f.jpg', '好吃就是胡萝卜');
INSERT INTO `product` VALUES ('27', '冬菇', '0', '12', '121.00', 'bed1067a-2647-469e-9ad3-35bd38dedb19.jpg', '213  1234');

-- ----------------------------
-- Table structure for `tb_order`
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL,
  `product_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sumprice` decimal(20,2) DEFAULT NULL COMMENT '订单总价',
  `create_time` datetime DEFAULT NULL,
  `beizu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '无',
  `status` tinyint(4) DEFAULT '0' COMMENT '0:未发货，1：已发货',
  PRIMARY KEY (`id`),
  KEY `orderuser_id` (`user_id`),
  KEY `orderproduct_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES ('2', '1000000178780032', '5', '13%', '1.00', '2020-02-04 11:49:18', '无', '1');
INSERT INTO `tb_order` VALUES ('4', '1000001743204471', '5', '13%', '1.00', '2020-02-04 21:34:47', '无', '1');
INSERT INTO `tb_order` VALUES ('5', '1000001774496950', '5', '11%', '13.00', '2020-02-04 21:54:18', '这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是要备注下的这里是', '1');
INSERT INTO `tb_order` VALUES ('6', '1000001025642004', '4', '14%6%12%', '136.00', '2020-02-08 22:47:39', '明天中午之前送到，外加两包调味料', '0');
INSERT INTO `tb_order` VALUES ('7', '1000001360097321', '4', '26%', '12.00', '2020-02-09 10:01:58', '购物车要进行修改成id来判断，这里收货信息要是一个变了全都要变，这也是弊端。', '0');
INSERT INTO `tb_order` VALUES ('8', '1000000085556668', '4', '1%', '12.00', '2020-02-09 10:22:49', '不用包装盒', '0');
INSERT INTO `tb_order` VALUES ('9', '1000000353959564', '4', '14%14%15%', '249.00', '2020-02-09 10:51:46', '剩下三条记录可以先别这样结算', '0');
INSERT INTO `tb_order` VALUES ('10', '1000001027020792', '4', '17%18%19%', '13.00', '2020-02-09 10:52:42', null, null);

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` int(4) DEFAULT '0' COMMENT '0：普通用户，1：管理员，2：销售员，3：送货员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', '1', 'c4ca4238a0b923820dcc509a6f75849b', '1');
INSERT INTO `tb_user` VALUES ('2', '2', 'c81e728d9d4c2f636f067f89cc14862c', '2');
INSERT INTO `tb_user` VALUES ('4', '0', 'cfcd208495d565ef66e7dff9f98764da', '0');
INSERT INTO `tb_user` VALUES ('5', '00', 'b4b147bc522828731f1a016bfa72c073', '0');
INSERT INTO `tb_user` VALUES ('7', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '3');

-- ----------------------------
-- Table structure for `user_address`
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES ('3', '5', '钟南山', '15017215364', '广东省广州市理工学院102');
INSERT INTO `user_address` VALUES ('4', '4', '钟东海', '15017215364', '广东省汕头市濠江区达濠华侨中学旁花区别墅31号');
