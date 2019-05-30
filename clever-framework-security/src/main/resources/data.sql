insert into sys_role (id,code,name,create_user_id,create_time,last_update_user_id,last_update_time)
values (1,'admin','管理员',1,'2019-06-06 11:11:11',1,'2019-06-06 11:11:11');

insert into sys_role (id,code,name,create_user_id,create_time,last_update_user_id,last_update_time)
values (2,'demo','演示角色',1,'2019-06-06 11:11:11',1,'2019-06-06 11:11:11');

insert into sys_user (id,login_name,user_name,password,email,enabled,last_password_reset_date,create_user_id,create_time,last_update_user_id,last_update_time)
values (1,'admin','admin','admin','admin@qq.com',true,'2019-06-06 11:11:11',1,'2019-06-06 11:11:11',1,'2019-06-06 11:11:11');

insert into sys_user_role (user_id,role_id) values (1,1);