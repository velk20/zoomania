INSERT INTO user_roles(id, user_role_enum)
values (1,'ADMIN'),
       (2,'USER');

insert into categories(id,name)
value (1,'Dogs'),
    (2,'Cats'),
    (3,'Fish'),
    (4,'Birds');

INSERT into users (id, email, first_name, is_active, last_name, password, username,phone,age)
VALUES (1,'admin@admin.com','Admin',1,'Adminov',
       'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','admin','0123456789',18),
        (2,'user@user.com','User',1,'Userov',
         'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user','01234123312',20);

INSERT INTO users_user_roles (user_entity_id, user_roles_id)
VALUES (1,1),
       (1,2),
       (2,2);