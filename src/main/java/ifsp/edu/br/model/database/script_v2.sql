create table cliente(
	id uuid not null,
	nome character varying not null,
	email character varying not null unique,
	senha character varying not null
);
alter table cliente add constraint pk_id_cliente primary key(id);
create table endereco(
	id uuid not null,
	logradouro character varying not null,
	numero integer not null,
	bairro character varying not null,
	cidade character varying not null,
	cep character varying not null
);
alter table endereco add constraint pk_id_endereco primary key(id);
create table cliente_endereco(
	id_cliente uuid not null,
	id_endereco uuid not null
);
alter table cliente_endereco add constraint fk_id_cliente foreign key(id_cliente) references cliente(id);
alter table cliente_endereco add constraint fk_id_endereco foreign key(id_endereco) references endereco(id);
alter table cliente_endereco add constraint pk_id_cliente_endereco primary key(id_cliente, id_endereco);
create table reciclagem(
	id uuid not null,
	id_endereco uuid not null,
	nome character varying not null,
	email character varying not null unique,
	senha character varying not null,
	telefone character varying not null unique
);
alter table reciclagem add constraint pk_id_reciclagem primary key(id);
alter table reciclagem add constraint fk_id_endereco_reciclagem foreign key(id_endereco) references endereco(id);
create table material(
	id uuid not null,
	nome character varying not null,
	descricao character varying not null
);
alter table material add constraint pk_id_material primary key(id);
create table lista(
	id uuid not null,
	id_cliente uuid not null,
	id_material uuid not null
);
alter table lista add constraint fk_id_cliente_lista foreign key(id_cliente) references cliente(id);
alter table lista add constraint fk_id_reciclagem_lista foreign key(id_material) references material(id);
alter table lista add constraint pk_id_lista primary key(id);
create table item(
	id_reciclagem uuid not null,
	id_material uuid not null,
	preco character varying
);
alter table item add constraint fk_id_reciclagem_item foreign key(id_reciclagem) references reciclagem(id);
alter table item add constraint fk_id_material_item foreign key(id_material) references material(id);
alter table item add constraint pk_id_item primary key(id_reciclagem, id_material);
create table compra(
	id_lista uuid not null,
	id_reciclagem uuid not null,
	data timestamp default current_timestamp not null
);
alter table compra add constraint fk_id_reciclagem_compra foreign key(id_reciclagem) references reciclagem(id);
alter table compra add constraint fk_id_lista_compra foreign key(id_lista) references lista(id);
alter table compra add constraint pk_id_compra primary key(id_lista, id_reciclagem);
create table administrador(
	id uuid not null,
	usuario character varying not null,
	senha character varying not null
);
alter table administrador add constraint pk_id_administrador primary key(id);

