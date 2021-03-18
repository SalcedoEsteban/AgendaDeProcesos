insert into especialidad (esp_nombre) values ('Esp_super_admin');

insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado_super_admin', 1);
 
insert into usuario (usu_username, usu_create_at, usu_password, usu_enabled, usu_nombre, usu_apellido, usu_cargo, juz_id_usu) values ('super_admin', '04-05-2020', '$2a$10$GjxzoHk3UNy/d5p3ylBKr.eUaGFdDp25SxVK0MzgzPXxSp4Z2zVX2', 1, 'Miller Eduardo', 'Muñoz Chicangana', 'Super Admin', 1);

insert into rol (rol_nombre, usu_id_rol) values ('ROLE_USER', 1);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_ADMIN', 1);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_SUPER_ADMIN', 1);