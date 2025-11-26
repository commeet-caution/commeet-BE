INSERT INTO users (login_id, password, name, university, department, role)
VALUES
  ('2020123456', '$2a$10$sU.5hHA2Qz6/3XIPHMRCa.Zp8Sb2cyvyUfOOhZjt7Lu/PLybuBfFq', '홍길동', '상명대학교', '컴퓨터공학과', 'ROLE_STUDENT'),
  ('2020123457', '$2a$10$ilu28ZrWfsnZSrozCNCzbO5XjhD5UnFZMzgrEUG8qh.zRjEz/hx8C', '김철수', '상명대학교', '기계공학부', 'ROLE_STUDENT'),
  ('admin', 'admin123', '관리자', '상명대학교', '정보시스템과', 'ROLE_ADMIN');
