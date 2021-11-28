insert into author (id, name, password) values (null, 'shell', 'llll000');
insert into author (id, name, password) values (null, 'telegrambot', 'sdfq');
insert into author (id, name, password) values (null, 'spa', '1111');
insert into category (id, name) values (null, 'unclassified');
insert into category (id, name) values (null, 'todo');
insert into category (id, name) values (null, 'фразы');
insert into category (id, name) values (null, 'пригодится');
insert into note (id, text, author_id, category_id) values (null, 'Съешь же ещё этих мягких французских булок да выпей чаю.', select id from author where name like '%telegrambot%', select id from category where name like '%todo%');
insert into note (id, text, author_id, category_id) values (null, 'The five boxing wizards jump quickly.', select id from author where name like '%spa%', select id from category where name like '%пригодится%');
