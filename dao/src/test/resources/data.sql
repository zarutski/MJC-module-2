CREATE SCHEMA IF NOT EXISTS `gift_certificate`;

DROP TABLE IF EXISTS `gift_certificate`;
CREATE TABLE `gift_certificate`
(
    `id`               bigint         NOT NULL AUTO_INCREMENT,
    `name`             varchar(200)   NOT NULL,
    `description`      varchar(200)   NOT NULL,
    `price`            decimal(10, 0) NOT NULL,
    `duration`         int            NOT NULL,
    `create_date`      timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date` timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `gift_certificate`
VALUES (1, 'name 1', 'description 1', 200, 120, '2021-01-26 03:41:58', '2021-01-26 03:41:58'),
       (2, 'name 2', 'description 2', 200, 240, '2021-01-26 03:51:58', '2021-01-26 03:51:58');

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`   bigint       NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `tag`
VALUES (1, 'abdullah'),
       (2, 'ability');

DROP TABLE IF EXISTS `gift_certificate_has_tag`;
CREATE TABLE `gift_certificate_has_tag`
(
    `gift_certificate_id` bigint NOT NULL,
    `tag_id`              bigint NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `tag_id`),
    CONSTRAINT `fk_gift_certificate_has_tag_gift_certificate`
        FOREIGN KEY (`gift_certificate_id`)
            REFERENCES `gift_certificate` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_gift_certificate_has_tag_tag1`
        FOREIGN KEY (`tag_id`)
            REFERENCES `tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `gift_certificate_has_tag`
VALUES (1, 1),
       (2, 2);

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`
(
    `id`   int         NOT NULL,
    `name` varchar(45) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user_roles`
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `first_name` varchar(100) NOT NULL,
    `last_name`  varchar(100) NOT NULL,
    `email`      varchar(45)  NOT NULL,
    `login`      varchar(45)  NOT NULL,
    `password`   varchar(255) NOT NULL,
    `role_id`    int          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_UNIQUE` (`email`),
    UNIQUE KEY `login_UNIQUE` (`login`),
    CONSTRAINT `fk_user_user_role1` FOREIGN KEY (`role_id`) REFERENCES `user_roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `users`
VALUES (1, 'Jessamine', 'Radin', 'jradin8@senate.gov', 'jradin8',
        '$2a$12$d5BiJASvcPFSDUfJMtLjW.wsb6on93mi/3Zx0wyUH1FFiB2UU/Fa.', 2),
       (2, 'Blakeley', 'Kleyn', 'bkleyn9@nationalgeographic.com', 'bkleyn9',
        '$2a$12$Oxpxss4f.TwMOWjnqPrTjuzdCEAJ8KCRNpe5Ji/ID7CZRfOpmOsT6', 1);

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `id`            bigint         NOT NULL AUTO_INCREMENT,
    `cost`          decimal(10, 0) NOT NULL,
    `purchase_date` timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`       bigint         NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_order_user1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;

INSERT INTO `orders`
VALUES (1, 200, '2021-02-08 22:12:48', 1),
       (2, 100, '2021-02-09 15:47:48', 2);

DROP TABLE IF EXISTS `gift_certificate_has_orders`;
CREATE TABLE `gift_certificate_has_orders`
(
    `gift_certificate_id` bigint NOT NULL,
    `orders_id`           bigint NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `orders_id`),
    UNIQUE KEY `gift_certificate_id_UNIQUE` (`gift_certificate_id`),
    CONSTRAINT `fk_gift_certificate_has_orders_gift_certificate1`
        FOREIGN KEY (`gift_certificate_id`)
            REFERENCES `gift_certificate` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_gift_certificate_has_orders_orders1`
        FOREIGN KEY (`orders_id`)
            REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `gift_certificate_has_orders`
VALUES (1, 1),
       (2, 2);