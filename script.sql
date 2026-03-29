DROP database if exists recipe_base;

use recipe_base;

CREATE TABLE `recipe_base`.`usuario` (
    `user_id` INT NOT NULL AUTO_INCREMENT , 
    `username` VARCHAR NOT NULL , 
    `password` VARCHAR NOT NULL , 
    `email` VARCHAR NOT NULL , 
    PRIMARY KEY (`user_id`))
    
CREATE TABLE `recipe_base`.`recipe` (
    `recipe_id` INT(50) NOT NULL AUTO_INCREMENT , 
    `user_id` INT(50) NOT NULL , 
    `name` VARCHAR(100) NOT NULL , 
    `description` VARCHAR(10000) NOT NULL , 
    `preparation_time` INT NOT NULL , 
    PRIMARY KEY (`recipe_id`))

 CREATE TABLE `recipe_base`.`ingredient` (
    `ingredient_id` INT NOT NULL AUTO_INCREMENT , 
    `name` VARCHAR NOT NULL , 
    PRIMARY KEY (`ingredient_id`))