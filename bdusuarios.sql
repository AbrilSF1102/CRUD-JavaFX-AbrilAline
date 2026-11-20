create database bdusuarios;
use bdusuarios;

create table sexo (
    id int auto_increment not null primary key,
    sexo varchar(50)
);

insert into sexo (sexo) values ("Masculino");
insert into sexo (sexo) values ("Femenino");

select * from sexo;

create table usuarios (
    id int auto_increment not null primary key,
    nombre varchar(100),
    apellidos varchar(100),
    fksexo int,
    foreign key (fksexo) references sexo (id) ON DELETE CASCADE ON UPDATE CASCADE
);
