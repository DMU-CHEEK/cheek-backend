CREATE TABLE member (
                        member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nickname VARCHAR(20) NOT NULL UNIQUE,
                        email VARCHAR(50) NOT NULL UNIQUE,
                        information TEXT,
                        description TEXT,
                        profile_picture VARCHAR(255),
                        member_type VARCHAR(10) NOT NULL,
                        role VARCHAR(10),
                        status VARCHAR(10),
                        firebase_token VARCHAR(255),
                        refresh_token VARCHAR(255),
                        token_expiration_time DATETIME,
                        created_at DATETIME NOT NULL,
                        updated_at DATETIME NOT NULL
);

CREATE TABLE domain (
                        domain_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        domain VARCHAR(255) NOT NULL,
                        is_valid BOOLEAN NOT NULL
);

CREATE TABLE email_verification (
                                    email_verification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL,
                                    verification_code VARCHAR(50) NOT NULL,
                                    validity_period DATETIME NOT NULL,
                                    is_verified BOOLEAN NOT NULL
);

CREATE TABLE question (
                          question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          content TEXT NOT NULL,
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME NOT NULL,
                          member_id BIGINT NOT NULL,
                          category_id BIGINT NOT NULL,
                          FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE,
                          FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE
);

CREATE TABLE collection (
                            collection_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            category_id BIGINT,
                            member_id BIGINT,
                            story_id BIGINT,
                            folder_id BIGINT,
                            created_at DATETIME NOT NULL,
                            updated_at DATETIME NOT NULL,
                            FOREIGN KEY (category_id) REFERENCES category(category_id),
                            FOREIGN KEY (member_id) REFERENCES member(member_id),
                            FOREIGN KEY (story_id) REFERENCES story(story_id),
                            FOREIGN KEY (folder_id) REFERENCES folder(folder_id)
);

CREATE TABLE folder (
                        folder_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        folder_name VARCHAR(255) NOT NULL,
                        thumbnail_picture VARCHAR(255),
                        member_id BIGINT,
                        created_at DATETIME NOT NULL,
                        updated_at DATETIME NOT NULL,
                        FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE highlight (
                           highlight_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           thumbnail_picture VARCHAR(255),
                           subject VARCHAR(255),
                           member_id BIGINT,
                           created_at DATETIME NOT NULL,
                           updated_at DATETIME NOT NULL,
                           FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE member_connection (
                                   member_connection_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   to_member_id BIGINT,
                                   from_member_id BIGINT,
                                   created_at DATETIME NOT NULL,
                                   updated_at DATETIME NOT NULL,
                                   FOREIGN KEY (to_member_id) REFERENCES member(member_id),
                                   FOREIGN KEY (from_member_id) REFERENCES member(member_id)
);

CREATE TABLE notification (
                              notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              to_member_id BIGINT,
                              from_member_id BIGINT,
                              created_at DATETIME NOT NULL,
                              updated_at DATETIME NOT NULL,
                              FOREIGN KEY (to_member_id) REFERENCES member(member_id),
                              FOREIGN KEY (from_member_id) REFERENCES member(member_id)
);

CREATE TABLE upvote (
                        upvote_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        member_id BIGINT,
                        story_id BIGINT,
                        created_at DATETIME NOT NULL,
                        updated_at DATETIME NOT NULL,
                        FOREIGN KEY (member_id) REFERENCES member(member_id),
                        FOREIGN KEY (story_id) REFERENCES story(story_id)
);

CREATE TABLE story (
                       story_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       content TEXT,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME NOT NULL
);

CREATE TABLE category (
                          category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE highlight_story (
                                 highlight_story_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 highlight_id BIGINT,
                                 story_id BIGINT,
                                 created_at DATETIME NOT NULL,
                                 updated_at DATETIME NOT NULL,
                                 FOREIGN KEY (highlight_id) REFERENCES highlight(highlight_id),
                                 FOREIGN KEY (story_id) REFERENCES story(story_id)
);
