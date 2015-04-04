-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.10 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             7.0.0.4372
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for gbilet
CREATE DATABASE IF NOT EXISTS `gbilet` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `gbilet`;


-- Dumping structure for table gbilet.gb_announcement
CREATE TABLE IF NOT EXISTS `gb_announcement` (
  `id` int(10) NOT NULL,
  `announcement` varchar(255) NOT NULL,
  `organization_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `gb_announcement_gb_organization` (`organization_id`),
  CONSTRAINT `gb_announcement_gb_organization` FOREIGN KEY (`organization_id`) REFERENCES `gb_organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table gbilet.gb_announcement: ~0 rows (approximately)
/*!40000 ALTER TABLE `gb_announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `gb_announcement` ENABLE KEYS */;


-- Dumping structure for table gbilet.gb_organization
CREATE TABLE IF NOT EXISTS `gb_organization` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  `date` datetime NOT NULL,
  `image_small` varchar(255) DEFAULT NULL,
  `image_big` varchar(255) DEFAULT NULL,
  `description` text,
  `ticket_price` float NOT NULL,
  `number_of_tickets` int(11) NOT NULL,
  `number_of_rows` int(11) NOT NULL,
  `can_reserved` tinyint(4) NOT NULL DEFAULT '0',
  `organizator_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `type_organization_type_id` (`type`),
  KEY `organizator_id_user_id` (`organizator_id`),
  CONSTRAINT `organizator_id_user_id` FOREIGN KEY (`organizator_id`) REFERENCES `gb_user` (`id`),
  CONSTRAINT `type_organization_type_id` FOREIGN KEY (`type`) REFERENCES `gb_organization_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table gbilet.gb_organization: ~0 rows (approximately)
/*!40000 ALTER TABLE `gb_organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `gb_organization` ENABLE KEYS */;


-- Dumping structure for table gbilet.gb_organization_type
CREATE TABLE IF NOT EXISTS `gb_organization_type` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table gbilet.gb_organization_type: ~3 rows (approximately)
/*!40000 ALTER TABLE `gb_organization_type` DISABLE KEYS */;
REPLACE INTO `gb_organization_type` (`id`, `name`, `description`) VALUES
	(1, 'Konser', 'Süper konserler birbirinden çılgın gruplar'),
	(2, 'Sinema', 'Süper sinemalar'),
	(3, 'Tiyatro', 'Sıkıcı tiyatrolar');
/*!40000 ALTER TABLE `gb_organization_type` ENABLE KEYS */;


-- Dumping structure for table gbilet.gb_ticket
CREATE TABLE IF NOT EXISTS `gb_ticket` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `organization_id` int(10) NOT NULL,
  `user_id` int(10) DEFAULT NULL,
  `status` char(1) NOT NULL,
  `row_no` int(10) NOT NULL,
  `seat_no` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `organization_id_organization_id` (`organization_id`),
  KEY `user_id_user_id` (`user_id`),
  CONSTRAINT `organization_id_organization_id` FOREIGN KEY (`organization_id`) REFERENCES `gb_organization` (`id`),
  CONSTRAINT `user_id_user_id` FOREIGN KEY (`user_id`) REFERENCES `gb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table gbilet.gb_ticket: ~0 rows (approximately)
/*!40000 ALTER TABLE `gb_ticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `gb_ticket` ENABLE KEYS */;


-- Dumping structure for table gbilet.gb_user
CREATE TABLE IF NOT EXISTS `gb_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `role` char(1) NOT NULL,
  `email` varchar(50) NOT NULL,
  `telephone` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table gbilet.gb_user: ~0 rows (approximately)
/*!40000 ALTER TABLE `gb_user` DISABLE KEYS */;
REPLACE INTO `gb_user` (`id`, `username`, `name`, `surname`, `password`, `role`, `email`, `telephone`) VALUES
	(1, 'gulsbh', NULL, NULL, '1', 'A', 'gulsbh_kocak@hotmail.com', NULL);
/*!40000 ALTER TABLE `gb_user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
