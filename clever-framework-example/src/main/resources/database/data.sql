-- 插入菜单
insert into sys_menu (id, menu_code, menu_name, menu_level, sort_num, parent_id, parent_ids, has_children, create_user_id, create_time,
                      last_update_user_id, last_update_time)
values (1, 'root', '根菜单', 0, 1, null, null, true, null, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (101, 'system', '系统管理', 1, 1, 1, '1', true, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (10101, 'user', '用户管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (10102, 'role', '角色管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (10103, 'menu', '菜单管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (10104, 'log', '日志管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (10105, 'config', '配置管理', 2, 1, 101, '1,101', false, 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11');

-- 插入角色
insert into sys_role (id, role_name, create_user_id, create_time, last_update_user_id, last_update_time)
values (1, '管理员', 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11'),
       (2, '普通用户', 1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11');

-- 插入角色菜单映射
insert into sys_role_menu (id, role_id, menu_id, deleted, create_time, last_update_time)
values (1, 1, 10101, false, '2020-11-11 11:11:11', '2020-11-11 11:11:11');
insert into sys_role_menu (id, role_id, menu_id, deleted, create_time, last_update_time)
values (2, 1, 10102, false, '2020-11-11 11:11:11', '2020-11-11 11:11:11');
insert into sys_role_menu (id, role_id, menu_id, deleted, create_time, last_update_time)
values (3, 1, 10103, false, '2020-11-11 11:11:11', '2020-11-11 11:11:11');
insert into sys_role_menu (id, role_id, menu_id, deleted, create_time, last_update_time)
values (4, 1, 10104, false, '2020-11-11 11:11:11', '2020-11-11 11:11:11');
insert into sys_role_menu (id, role_id, menu_id, deleted, create_time, last_update_time)
values (5, 1, 10105, false, '2020-11-11 11:11:11', '2020-11-11 11:11:11');


-- 插入行政区
insert into sys_area (id, create_time, create_user_id, last_update_time, last_update_user_id,
                      area_code, deleted, has_children, area_level, area_name, parent_id, parent_ids,
                                            parent_code, parent_codes, sort_num)
values (1, '2019-06-06 11:11:11', null, '2019-06-06 11:11:11', 1, '37', false, true, 0, '山东省', null, null, null, null,
        1);


-- 插入用户
insert into sys_user (id, create_time, create_user_id, last_update_time, last_update_user_id, email,
                      change_password_date_time, nick_name, password, phone, enabled,
                      user_name)
values (1, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', 1, 'admin@qq.com', '2019-06-06 11:11:11', 'admin',
        '$2a$10$z5htSCYs.GdnYDJXIR/tC.tIQjHobYo8AqsMIiClxZlgMX4CNAv.u', null, true, 'admin');
insert into sys_user (id, create_time, create_user_id, last_update_time, last_update_user_id,
                      email, change_password_date_time, nick_name, password, phone, enabled,
                      user_name)
values (2, '2019-06-06 11:11:11', 1, '2019-06-06 11:11:11', 1, 'user1@qq.com', '2019-06-06 11:11:11', 'user1',
        '$2a$10$z5htSCYs.GdnYDJXIR/tC.tIQjHobYo8AqsMIiClxZlgMX4CNAv.u', null, true, 'user1');

-- 插入用户权限
insert into sys_user_permission (id, area_id, user_id, role_id, deleted, create_time, last_update_time)
values (1, 1, 1, 1, false, '2020-11-11 11:11:11', '2020-11-11 11:11:11');




