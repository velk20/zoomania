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
         'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user','2101234123312',20),
       (3,'sergiogarciagarcia@kaulananews.com','Atanas',1,'Atanasov',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user1','3201234123312',21),
       (4,'leo@myxl.live','Georgi',1,'Georgiev',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user2','321201234123312',
        22),
       (5,'angelawolsey@freeallapp.com','Petur',1,'Petrov',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user3','301234123312',23),
       (6,'tkirkbride@gmailvn.net','Angel',1,'Angelov',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user4','401234123312',24),
       (7,'york17mur@abusemail.de','Venelin',1,'Venelinov',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user5','5401234123312',25),
       (8,'lostboy18@saxophonexltd.com','Maria',1,'Marinova',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user6','3101234123312',26),
       (9,'maratkarimov95@gmailwe.com','Malin',1,'Malinov',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user7','7601234123312',27),
       (10,'m004@hearkn.com','Fake',1,'Profile',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user8','8701234123312',28),
       (11,'blockerx34@myxl.live','Toni',1,'Storaro',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user9','34501234123312',32),
       (12,'mattwilliams7@reacc.me','Slavi',1,'Trifonov',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user10','7501234123312',33),
       (13,'nizart@hearkn.com','Kuku',1,'Bent',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user11','21301234123312',
        45),
       (14,'pimshindenis@nlchttvpkz.ga','Tedi',1,'Bear',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user12','4301234123312',26),
       (15,'chochain@snapboosting.com','Ton',1,'Bonbon',
        'feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c','user13','501234123312',43);

INSERT INTO users_user_roles (user_entity_id, user_roles_id)
VALUES (1,1),
       (1,2),
       (2,2),
       (3,2),
       (4,2),
       (5,2),
       (6,2),
       (7,2),
       (8,2),
       (9,2),
       (10,2),(11,2),(12,2),(13,2),(14,2),(15,2);

# insert into offers(id, created_on, description, image_url, price, title, category_id, seller_id, breed)
# #         Dogs
# values (1,'2023-01-03','','https://images.unsplash.com/photo-1585943870180-be99fca07f23?ixlib=rb-4.0
# .3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=765&q=80',250,'Alonso',1,1,'Affenpinscher'),
#     (2,'2023-01-04','','https://images.unsplash.com/photo-1616832212184-aeeb37024620?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=765&q=80'
#     ,150,'Fluffy',1,2,'Akita'),
#        (3,'2023-01-05','','https://images.unsplash.com/photo-1573995974701-1af577c7de08?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80'
#        ,270,'Alis',1,3,'Alaskan'),
#        (4,'2023-01-06','','https://images.unsplash.com/photo-1609075939132-1f7e076a5f2c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80'
#        ,350,'Calyn',1,4,'Spaniel'),
#
# #         Cats
#        (5,'2023-01-07','','https://images.unsplash.com/photo-1605254252163-9208d6fd3fd7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80'
#        ,0,'Teny',2,5,
#         'Street Superb'),
#        (6,'2023-01-08','','https://images.unsplash.com/photo-1622177577032-326d38124646?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=686&q=80'
#        ,150,'Antonin',2,1,'Birman'),
#        (7,'2023-01-09','','https://images.unsplash.com/photo-1601295617660-d7ce06fdba5b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1166&q=80'
#        ,90,'Camille',2,1,'Chartreux'),
#        (8,'2023-01-10','','https://images.unsplash.com/photo-1649472729650-9803247c67d7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1632&q=80'
#        ,100,'Liona',2,1,'Korat'),
#
# #         Fish
#        (9,'2023-01-11','','https://images.unsplash.com/photo-1602143221967-ff9a1a490e00?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80'
#        ,20,'Gary Ellis',3,6,'Guppies'),
#        (10,'2023-01-12','','https://images.unsplash.com/photo-1672572442059-c46f99bc5354?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80'
#        ,25,'Sohrab',3,7,'Zia'),
#        (11,'2023-01-13','','https://images.unsplash.com/photo-1571752726668-05843b3d0073?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1165&q=80'
#        ,50,'Dori',3,8,'Blue Fish'),
#
# #         Birds
#        (12,'2023-01-14','','https://images.unsplash.com/photo-1626133830160-2d463a6b64a7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=735&q=80'
#        ,60,'Elis',4,9,'African Gray Parrot'),
#        (13,'2023-01-15','','https://images.unsplash.com/photo-1662392526190-1c71941da130?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1171&q=80'
#        ,15,'Eric',4,10,'Black-throated Sparrow'),
#        (14,'2023-01-16','','https://images.unsplash.com/photo-1656085422329-29eee95ddb66?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1188&q=80'
#        ,20,'Martin',4,11,'Wemyss'),
#        (15,'2023-01-17','','https://images.unsplash.com/photo-1652533879656-471594a2a444?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80'
#        ,0,'Skyler',4,12,
#         'Ewing');