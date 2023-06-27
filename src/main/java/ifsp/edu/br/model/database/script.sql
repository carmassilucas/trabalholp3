create table cliente(
	id uuid not null,
	nome character varying not null,
	email character varying not null unique,
	senha character varying not null
);

alter table cliente
add constraint pk_id_cliente primary key(id);

create table endereco(
	id uuid not null,
	cep character varying not null,
	cidade character varying not null,
	logradouro character varying not null,
	numero integer not null,
	bairro character varying not null,
	uf character(2) not null
);

alter table endereco
add constraint pk_id_endereco primary key(id);

create table cliente_endereco(
	id_cliente uuid not null,
	id_endereco uuid not null
);

alter table cliente_endereco
add constraint fk_id_cliente foreign key(id_cliente)
references cliente(id);

alter table cliente_endereco
add constraint fk_id_endereco foreign key(id_endereco)
references endereco(id);

alter table cliente_endereco
add constraint pk_id_cliente_endereco
primary key(id_cliente, id_endereco);

create table reciclagem(
	id uuid not null,
	id_endereco uuid not null,
	nome character varying not null,
	usuario character varying not null unique,
	senha character varying not null
);

alter table reciclagem
add constraint pk_id_reciclagem primary key(id);

alter table reciclagem
add constraint fk_id_endereco_reciclagem foreign key(id_endereco) references endereco(id);

create table material(
	id uuid not null,
	nome character varying not null,
	descricao character varying not null
);

alter table material
add constraint pk_id_material primary key(id);

create table material_reciclagem (
	id_material uuid not null,
	id_reciclagem uuid not null,
	preco float not null
);

alter table material_reciclagem
add constraint pk_id_material_reciclagem primary key (id_material, id_reciclagem);

alter table material_reciclagem
add constraint fk_id_material_reciclagem_material foreign key (id_material)
references material(id);

alter table material_reciclagem
add constraint fk_id_material_reciclagem_reciclagem foreign key (id_reciclagem)
references reciclagem(id);

create table lista(
	id_cliente uuid not null,
	id_material uuid not null,
	id_reciclagem uuid not null
);

alter table lista
add constraint pk_lista primary key (id_cliente, id_material);

alter table lista
add constraint fk_lista_cliente foreign key (id_cliente)
references cliente (id);

alter table lista
add constraint fk_lista_material_reciclagem
foreign key (id_material, id_reciclagem)
references material_reciclagem (id_material, id_reciclagem);

create table administrador(
	id uuid not null,
	usuario character varying not null unique,
	senha character varying not null
);

alter table administrador
add constraint pk_administrador primary key (id);

create table item (
	id uuid not null,
	id_material uuid not null,
	preco float not null
);

alter table item
add constraint pk_item primary key (id);

alter table item
add constraint fk_item foreign key (id_material)
references material (id);

create table negociacao(
	id_cliente uuid not null,
	id_item uuid not null,
	id_reciclagem uuid not null,
	datahora timestamp not null,
	total float not null
);

alter table negociacao
add constraint pk_negociacao primary key (id_cliente, id_item, id_reciclagem);

alter table negociacao
add constraint fk_negociacao_cliente foreign key (id_cliente)
references cliente (id);

alter table negociacao
add constraint fk_negociacao_item foreign key (id_item)
references item (id);

alter table negociacao
add constraint fk_negociacao_reciclagem foreign key (id_reciclagem)
references reciclagem (id);