create table data_penduduk (
  nik             character varying(64)  not null primary key,
  nama_identitias character varying(100) not null,
  tanggal_lahir   date                   not null
);

insert into data_penduduk (nik, nama_identitias, tanggal_lahir)
values
  ('123456', 'Dimas Maryanto', '1992-03-28'),
  ('123457', 'Muhamad Yusuf', '1992-03-28'),
  ('123458', 'Hadi', '1992-03-28'),
  ('123459', 'Rega', '1992-03-28'),
  ('123441', 'Adib', '1992-03-28'),
  ('123442', 'Gufron', '1992-03-28'),
  ('123443', 'Hari Sapto Adi', '1992-03-28'),
  ('123444', 'Abdul', '1992-03-28'),
  ('123445', 'Dewa', '1992-03-28'),
  ('123446', 'Ihsan', '1992-03-28'),
  ('123447', 'Lala', '1992-03-28'),
  ('123448', 'Fitri', '1992-03-28'),
  ('123449', 'Deni Sutisna', '1992-03-28'),
  ('123440', 'Andri', '1992-03-28');
