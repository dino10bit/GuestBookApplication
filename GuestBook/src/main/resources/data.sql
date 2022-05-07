DROP TABLE IF EXISTS USERS;
  
CREATE TABLE USERS(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_name VARCHAR(250) NOT NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  role VARCHAR(250) NOT NULL
   
);

DROP TABLE IF EXISTS GUEST_BOOK_ENTRY;
  
CREATE TABLE GUEST_BOOK_ENTRY(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_name VARCHAR(250) NOT NULL,
  text_entry VARCHAR(250) NULL,
  file_name VARCHAR(250) NULL,
  image_entry BLOB NULL,
  created_datetime TIMESTAMP NOT NULL,
  updated_datetime TIMESTAMP NULL,
  updated_by VARCHAR(250) NULL,
  status VARCHAR(50) NULL
   
);

INSERT INTO USERS(id,user_name, first_name, last_name, email, password, role) VALUES
 (1,'guest', 'First1', 'last1','abc@gmail.com','12345','ROLE_GUEST'),
  (2,'admin', 'Admin', 'Admin','admin@gmail.com','12345','ROLE_ADMIN');
   
INSERT INTO GUEST_BOOK_ENTRY(id, user_name, text_entry, status, created_datetime) VALUES
 (1,'guest', 'First Entry', 'Pending',CURRENT_TIMESTAMP),
  (2,'guest', 'Second Entry','Pending', CURRENT_TIMESTAMP);

