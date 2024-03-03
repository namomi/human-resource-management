CREATE TABLE `EMPLOYEE` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(255) NOT NULL UNIQUE,
                            `teamName` VARCHAR(255),
                            `team_id` BIGINT,
                            `role` VARCHAR(255) NOT NULL,
                            `birthday` DATE NOT NULL,
                            `workStartDate` DATE NOT NULL,
                            PRIMARY KEY (`id`),
                            CONSTRAINT `fk_employee_team` FOREIGN KEY (`team_id`) REFERENCES `TEAM` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TEAM` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `name` VARCHAR(255) NOT NULL,
                        `manager` VARCHAR(255),
                        `leaveNoticeDays` INT,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ATTENDANCE` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `employee_id` BIGINT,
                              `date` DATE,
                              `startTime` TIME,
                              `endTime` TIME,
                              PRIMARY KEY (`id`),
                              CONSTRAINT `fk_attendance_employee` FOREIGN KEY (`employee_id`) REFERENCES `EMPLOYEE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ANNUAL_LEAVE` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                `employee_id` BIGINT,
                                `leaveDate` DATE,
                                `totalLeaves` INT,
                                `usedLeaves` INT,
                                `used` BOOLEAN,
                                PRIMARY KEY (`id`),
                                CONSTRAINT `fk_annual_leave_employee` FOREIGN KEY (`employee_id`) REFERENCES `EMPLOYEE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
