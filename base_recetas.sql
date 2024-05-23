drop table usuarios cascade constraints;
drop table recetas cascade constraints;
drop table etiquetas cascade constraints;
drop table rece_eti cascade constraints;

create table usuarios(
    nombre_usu varchar2(500)  primary key,
    contraseña varchar2(50) not null,
    mail varchar2(50) unique not null,
    tipo number(1,0)
);
   
create table recetas(
    ident integer generated always as identity (Start with 1 increment by 1) primary key, 
    nombre varchar2(500) not null,
    creador varchar2(500) not null,
    p_descripcion varchar2(500) not null,
    ingredientes varchar2(500) not null,
    descripcion varchar2(500) not null ,
    punt_tot number(3,1) default 0,
    n_punt number(2,0) default 0,
    
    constraint ce_crea_usu foreign key (creador) references usuarios(nombre_usu)
); 

create table etiquetas(
    ident_e integer generated always as identity (Start with 1 increment by 1) primary key, 
    nom_etiq varchar2(20) not null   
);
create table rece_eti(
    ident integer,
    ident_e integer,
    constraint ce_rece foreign key (ident) references recetas(ident),
    constraint ce_eti foreign key (ident_e) references etiquetas(ident_e),
    constraint cp_eti primary key (ident, ident_e)   
);
    
create or replace trigger tr_inc after update of punt_tot on recetas for each row
begin
    update n_punt receta set receta.n_punt = receta.n_punt+1 where receta.ident = ident;
end;
drop trigger tr_inc;
    
insert into usuarios values('Pablo','contraseña1','pablo@gmail.com',1); 
insert into usuarios values('Juande','contraseña2','juan@gmail.com',1); 

insert into recetas(nombre, creador, p_descripcion, ingredientes, descripcion) values ('Tortilla', 'Juande', 'asdfasdfasdf', 'patatas, huevos', 'Pela las papas y rompe los huevos');
insert into recetas(nombre, creador, p_descripcion, ingredientes, descripcion) values ('Tortilla2', 'Juande', 'asdfasdfasdf', 'patatas, huevos', 'Pela las papas y rompe los huevos');

insert into etiquetas(nom_etiq) values('frito');
insert into etiquetas(nom_etiq) values('dulce');
insert into etiquetas(nom_etiq) values('salado');
insert into etiquetas(nom_etiq) values('agrio');
insert into etiquetas(nom_etiq) values('amargo');
insert into etiquetas(nom_etiq) values('agridulce');
insert into etiquetas(nom_etiq) values('picante');
insert into etiquetas(nom_etiq) values('italiana');
insert into etiquetas(nom_etiq) values('francesa');
insert into etiquetas(nom_etiq) values('queso');
insert into etiquetas(nom_etiq) values('árabe');
insert into etiquetas(nom_etiq) values('americana');
insert into etiquetas(nom_etiq) values('china');
insert into etiquetas(nom_etiq) values('japonesa');
insert into etiquetas(nom_etiq) values('coreana');
insert into etiquetas(nom_etiq) values('oriental');
insert into etiquetas(nom_etiq) values('pasta');
insert into etiquetas(nom_etiq) values('arroz');
insert into etiquetas(nom_etiq) values('carne');
insert into etiquetas(nom_etiq) values('pescado');
insert into etiquetas(nom_etiq) values('marisco');
insert into etiquetas(nom_etiq) values('rebozado');
insert into etiquetas(nom_etiq) values('vegana');
insert into etiquetas(nom_etiq) values('vegetariana');

insert into rece_eti values(1, 2);
insert into rece_eti values(1, 3);
insert into rece_eti values(1, 7);
insert into rece_eti values(2, 2);


select nombre, creador, p_descripcion, ingredientes, descripcion from recetas, rece_eti
where recetas.ident = rece_eti.ident
and ident_e = 2 and ident IN (select r.ident from recetas r, rece_eti
where recetas.ident = rece_eti.ident
and ident_e = 7);

commit;
