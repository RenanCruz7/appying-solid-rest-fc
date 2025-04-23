CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      status VARCHAR(50),
                      priority INT,
                      dueDate DATE
);