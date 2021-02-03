-- -----------------------------------------------------
-- Schema gift_certificate
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gift_certificate`;

-- -----------------------------------------------------
-- Table `gift_certificate`.`gift_certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS gift_certificate;
CREATE TABLE IF NOT EXISTS gift_certificate (
    id BIGINT(8) NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(200) NOT NULL,
    price DECIMAL(10) NOT NULL,
    duration INT NOT NULL,
    create_date TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_date TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gift_certificate`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tag (
    `id` BIGINT(8) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gift_certificate`.`gift_certificate_has_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS gift_certificate_has_tag (
    `gift_certificate_id` BIGINT(8) NOT NULL,
    `tag_id` BIGINT(8) NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `tag_id`),
    CONSTRAINT `gift_certificate_id`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES gift_certificate (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
    CONSTRAINT `fk_gift_certificate_has_tag_tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES tag (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
    ENGINE = InnoDB;

INSERT INTO gift_certificate (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('first name', 'first description', '100', '120', '2021-01-25 07:40:10.000000', '2021-01-25 07:40:10.000000');
INSERT INTO gift_certificate (`name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES ('second name', 'second description', '200', '240', '2021-01-25 07:50:10.000000', '2021-01-25 07:50:10.000000');

INSERT INTO tag (`name`) VALUES ('first tag');
INSERT INTO tag (`name`) VALUES ('second tag');

INSERT INTO gift_certificate_has_tag (`gift_certificate_id`, `tag_id`) VALUES ('1', '1');
INSERT INTO gift_certificate_has_tag (`gift_certificate_id`, `tag_id`) VALUES ('2', '2');