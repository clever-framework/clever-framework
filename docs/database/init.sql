insert into sys_menu (id, menu_code, menu_name, menu_level, sort_num, parent_id, parent_ids, has_children,
                      create_user_id, create_date_time,
                      update_user_id, update_date_time, deleted)
values (101, 'system', '系统管理', 1, 1, 1, '1', true, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false),
       (10101, 'user', '用户管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false),
       (10102, 'role', '角色管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false),
       (10103, 'menu', '菜单管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false),
       (10104, 'log', '日志管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false),
       (10105, 'config', '配置管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false);

insert into sys_role (id, role_name, create_user_id, create_date_time, update_user_id, update_date_time, deleted)
values (1, '管理员', 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false),
       (2, '普通用户', 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', false);

insert into sys_user (id, user_name, nick_name, password, email, user_status, change_password_date_time, create_user_id,
                      create_date_time, update_user_id, update_date_time, deleted)
values (1, 'admin', 'admin', '$2a$10$71U/mNIYhw8qH8z9YURdpOgxqFVcOFucWl6TGCYTaKRuruqnQ7kRO', 'admin@qq.com', 1,
        '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', 1,
        '2019-06-06 11:11:11', false);

insert into sys_user_permission (id, user_id, role_id, create_date_time, update_date_time, deleted)
values (1, 1, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false),
       (2, 1, 2, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false);

insert into sys_role_menu(id, menu_id, role_id, create_date_time, update_date_time, deleted)
values (1, 101, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false),
       (2, 10101, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false),
       (3, 10102, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false),
       (4, 10103, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false),
       (5, 10104, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false),
       (6, 10105, 1, '2019-06-06 11:11:11', '2019-06-06 11:11:11', false);
