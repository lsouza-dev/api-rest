alter table medicos drop column ativo;
alter table medicos add ativo boolean default true;