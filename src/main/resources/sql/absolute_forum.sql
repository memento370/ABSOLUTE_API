CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    status VARCHAR(255),
    title VARCHAR(255),
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nick VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    role VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS topic (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     status VARCHAR(255),
    user_id BIGINT NOT NULL,
    creation_date TIMESTAMP,
    sub_section VARCHAR(255),
    title VARCHAR(255),
    message TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id)
    );

CREATE TABLE IF NOT EXISTS comment_topic (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       user_id BIGINT NOT NULL,
                                       topic_id BIGINT NOT NULL,
                                       text TEXT,
                                       creation_date TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (topic_id) REFERENCES topic(id)
    );