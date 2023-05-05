create table cliente (
	id uuid not null,
	nome character varying not null,
	email character varying not null unique,
	senha character varying not null
);

alter table cliente add constraint PK_ID_CLIENTE primary key (id);

create table cliente_endereco (
	id_cliente uuid not null,
	id_endereco uuid not null
);

alter table cliente_endereco add constraint PK_ID_CLIENTE_ENDERECO primary key (id_cliente, id_endereco);

alter table cliente_endereco add constraint FK_ID_CLIENTE foreign key (id_cliente) references cliente (id);

alter table cliente_endereco add constraint FK_ID_ENDERECO  foreign key (id_endereco) references endereco (id);

create table endereco (
	id uuid not null,
	cep character varying not null,
	cidade character varying not null,
	logradouro character varying not null,
	numero integer not null,
	bairro character varying not null,
	uf character varying not null
);

alter table endereco add constraint PK_ID_ENDERECO primary key (id);