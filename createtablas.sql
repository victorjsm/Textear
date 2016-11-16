-- Script de borrado y creacion de tablas y esquemas
-- necesarias para el funcionamiento de TrinityKronos


/************** EELIMINA LAS TABLAS **************/
-- DROP TABLE textear.usuario;
DROP TABLE opciones_pregunta;
DROP TABLE preguntas_encuesta;
DROP TABLE t_encuesta;
DROP TABLE t_consulta;
DROP TABLE t_inscripcion;
DROP TABLE t_sistema;
DROP TABLE precio_canal_prefijo;

DROP TABLE prefijo;
DROP TABLE t_mensaje;
DROP TABLE grupo;
DROP TABLE abonado;
DROP TABLE bandeja;
DROP TABLE usuario;
DROP TABLE mensaje_env;
DROP TABLE mensaje_rec;
DROP TABLE canal;
DROP TABLE empresa;




/************** ELIMINA LOS ESQUEMAS **************/ 
-- DROP SCHEMA textear;


/************** CREACION DE LOS ESQUEMAS **************/
-- CREATE SCHEMA textear;

/************** CREACION DE LA TABLA USUARIO **************/
/*
 * Tabla que almacena los usuarios del sistema
 *
 * Atributos:
 * - ID   			: ID de ingreso al sistema.
 * - Password  		: Contrasena de ingreso al sistema.
 * - RIF_Empresa    : RIF de la empresa asociada al usuario.
 * - Nombre         : Nombre del usuario.
 * - Email       	: Correo electronico del usuario.
 * - Telefono      	: Telefono de contacto del usuario.
 * - Fax       		: Fax del usuario.
 * - Tipo       	: Tipo del usuario.
 *   -- 0: Cliente.
 *   -- 1: Abonado.
 *   -- 2: Administrador.
 */
-- CREATE TABLE textear.usuario
-- (
--   ID character varying(255) NOT NULL,
--   Password character varying(255) NOT NULL,
--   RIF_Empresa character varying(255) NOT NULL, 
--   Nombre character varying(255) NOT NULL,
--   Email character varying(255) NOT NULL,
--   Telefono integer NOT NULL,
--   Fax integer,
--   Tipo integer NOT NULL DEFAULT 0,
--   PRIMARY KEY (ID)
-- );

CREATE TABLE prefijo
(
  Codigo      varchar(20)   NOT NULL,
  CostoMT     NUMERIC(6,2) NOT NULL,
  PRIMARY KEY (Codigo)
);

CREATE TABLE canal
(
  Codigo          varchar(20)    NOT NULL,
  Descripcion     text          NOT NULL,
  Longitud        int           NOT NULL,
  Precio_recibir  NUMERIC(6,2)  NOT NULL,
  Precio_enviar   NUMERIC(6,2)  NOT NULL,
  PRIMARY KEY (Codigo)
);

CREATE TABLE precio_canal_prefijo
(
  Codigo_prefijo  varchar(20)   NOT NULL,
  Codigo_canal    varchar(20)   NOT NULL,
  Precio          NUMERIC(6,2)  NOT NULL,
  FOREIGN KEY (Codigo_prefijo) REFERENCES prefijo (Codigo),
  FOREIGN KEY (Codigo_canal) REFERENCES canal (Codigo),
  PRIMARY KEY (Codigo_prefijo,Codigo_canal)  
);

CREATE TABLE empresa
(
  RIF character varying(255) NOT NULL,
  NIT character varying(255) NOT NULL,
  Nombre character varying(255) NOT NULL,
  Direccion text NOT NULL,
  Telefono varchar(20) NOT NULL,
  PaginaWeb varchar(40),
  Acronimo  varchar(5)   UNIQUE NOT NULL,
  Saldo real NOT NULL DEFAULT 0,
  PRIMARY KEY (RIF)
);

CREATE TABLE usuario
(
  ID character varying(255) NOT NULL,
  Password character varying(255) NOT NULL,
  RIF_Empresa character varying(255) NOT NULL, 
  Nombre character varying(255) NOT NULL,
  Email character varying(255) NOT NULL,
  Telefono varchar(20) NOT NULL,
  Fax varchar(20),
  Tipo character varying(15) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (RIF_Empresa) REFERENCES empresa (RIF)
);

CREATE TABLE mensaje_env
(
  Acronimo      varchar(5)              NOT NULL,
  Clave         character varying(255)  NOT NULL,
  Canal         varchar(20)             NOT NULL,
  Mensaje       text                    NOT NULL,
  Telefono      varchar(20)             NOT NULL,
  PRIMARY KEY (Acronimo, Clave, Telefono)
);
CREATE TABLE mensaje_rec
(
  Acronimo      varchar(5)              NOT NULL,
  Clave         character varying(255)  NOT NULL,
  Canal         varchar(20)             NOT NULL,
  Mensaje       text                    NOT NULL,
  Telefono      varchar(20)             NOT NULL,
  PRIMARY KEY (Acronimo, Clave, Telefono)
);


CREATE TABLE bandeja
(
  RIF_Empresa character varying(255) NOT NULL,
  Nombre character varying(255) NOT NULL,
  Tipo character varying(15) NOT NULL,
  FOREIGN KEY (RIF_Empresa) REFERENCES empresa (RIF),
  PRIMARY KEY (Nombre, RIF_Empresa)
);

CREATE TABLE abonado
(
  RIF_Empresa character varying(255) NOT NULL,
  Nombre character varying(255),
  Telefono varchar(20) NOT NULL,
  CI varchar(25),
  negra boolean,
  FOREIGN KEY (RIF_Empresa) REFERENCES empresa (RIF),
  PRIMARY KEY (Telefono, RIF_Empresa)
);



CREATE TABLE grupo
(
  RIF_Empresa character varying(255) NOT NULL,
  Nombre character varying(255) NOT NULL,
  Telefono varchar(20) NOT NULL,
  FOREIGN KEY (Telefono, RIF_Empresa) REFERENCES abonado (Telefono, RIF_Empresa),
  PRIMARY KEY (Nombre, RIF_Empresa, Telefono)
);

CREATE TABLE t_mensaje
(
  RIF_Empresa       character varying(255)  NOT NULL,
  Telefono          varchar(20)             NOT NULL,
  Nombre            character varying(255)  NOT NULL,
  Codigo_canal      varchar(20)             NOT NULL,
  Estado            varchar(25)             NOT NULL,
  Fecha_envio       timestamp               NOT NULL,
  Fecha_creacion    timestamp               NOT NULL,
  Fecha_expiracion  timestamp               NOT NULL,
  Mensaje           text                    NOT NULL,
  Bandeja           character varying(255)  NOT NULL,
  FOREIGN KEY (Telefono, RIF_Empresa) REFERENCES abonado (Telefono, RIF_Empresa),
  FOREIGN KEY (Codigo_canal) REFERENCES canal (Codigo),
  PRIMARY KEY (Nombre,Telefono, RIF_Empresa)
);

CREATE TABLE t_encuesta
(
  RIF_Empresa       character varying(255)  NOT NULL,
  Telefono                varchar(20)             NOT NULL,
  Nombre            character varying(255)  NOT NULL,
  Codigo_canal      varchar(20)             NOT NULL,
  Estado            varchar(25)             NOT NULL,
  Fecha_envio       timestamp               NOT NULL,
  Fecha_creacion    timestamp               NOT NULL,
  Fecha_expiracion  timestamp               NOT NULL,
  Bienvenida        text                    NOT NULL,
  Ayuda             text                    NOT NULL,
  Bandeja           character varying(255)  NOT NULL,
  FOREIGN KEY (Telefono, RIF_Empresa) REFERENCES abonado (Telefono, RIF_Empresa),
  FOREIGN KEY (Codigo_canal) REFERENCES canal (Codigo),
  PRIMARY KEY (Nombre,Telefono, RIF_Empresa)
);

CREATE TABLE preguntas_encuesta
(
  T_nombre        character varying(255)  NOT NULL,
  T_telefono            varchar(20)             NOT NULL,
  T_rif           character varying(255)  NOT NULL,
  ID              int                     NOT NULL,
  Pregunta        text                    NOT NULL,
  Respuesta       text,
  FOREIGN KEY (T_nombre,T_telefono,T_rif) REFERENCES t_encuesta (Nombre,Telefono, RIF_Empresa),
  PRIMARY KEY (T_nombre,T_telefono,T_rif,ID)
);

CREATE TABLE opciones_pregunta
(
  T_nombre        character varying(255)  NOT NULL,
  T_telefono            varchar(25)             NOT NULL,
  T_rif           character varying(255)  NOT NULL,
  Pregunta_ID     int                     NOT NULL,
  ID              int                     NOT NULL,
  Opcion          text                    NOT NULL,
  FOREIGN KEY (T_nombre,T_telefono,T_rif, Pregunta_ID) REFERENCES preguntas_encuesta (T_nombre,T_telefono,T_rif,ID),
  PRIMARY KEY (T_nombre,T_telefono,T_rif,Pregunta_ID, ID)
);

CREATE TABLE t_consulta
(
  RIF_Empresa       character varying(255)  NOT NULL,
  Telefono                varchar(20)             NOT NULL,
  Nombre            character varying(255)  NOT NULL,
  Codigo_canal      varchar(20)             NOT NULL,
  Estado            varchar(25)             NOT NULL,
  Fecha_envio       timestamp               NOT NULL,
  Fecha_creacion    timestamp               NOT NULL,
  Fecha_expiracion  timestamp               NOT NULL,
  Bienvenida        text                    NOT NULL,
  Ayuda             text                    NOT NULL,
  Respuesta         text,
  Bandeja           character varying(255)  NOT NULL,
  Pregunta          text                    NOT NULL,
  FOREIGN KEY (Telefono, RIF_Empresa) REFERENCES abonado (Telefono, RIF_Empresa),
  FOREIGN KEY (Codigo_canal) REFERENCES canal (Codigo),
  PRIMARY KEY (Nombre,Telefono, RIF_Empresa)
);

CREATE TABLE t_inscripcion
(
  RIF_Empresa       character varying(255)  NOT NULL,
  Telefono          varchar(20)             NOT NULL,
  Nombre            character varying(255)  NOT NULL,
  Codigo_canal      varchar(20)             NOT NULL,
  Estado            varchar(25)             NOT NULL,
  Fecha_envio       timestamp               NOT NULL,
  Fecha_creacion    timestamp               NOT NULL,
  Fecha_expiracion  timestamp               NOT NULL,
  Respuesta         text,
  Bandeja           character varying(255)  NOT NULL,
  FOREIGN KEY (Telefono, RIF_Empresa) REFERENCES abonado (Telefono, RIF_Empresa),
  FOREIGN KEY (Codigo_canal) REFERENCES canal (Codigo),
  PRIMARY KEY (Nombre,Telefono, RIF_Empresa)
);

CREATE TABLE t_sistema
(
  RIF_Empresa       character varying(255)  NOT NULL,
  Telefono          varchar(20)             NOT NULL,
  Nombre            character varying(255)  NOT NULL,
  Codigo_canal      varchar(20)             NOT NULL,
  Estado            varchar(25)             NOT NULL,
  Fecha_envio       timestamp               NOT NULL,
  Fecha_creacion    timestamp               NOT NULL,
  Fecha_expiracion  timestamp               NOT NULL,
  Bandeja           character varying(255)  NOT NULL,
  FOREIGN KEY (Telefono, RIF_Empresa) REFERENCES abonado (Telefono, RIF_Empresa),
  FOREIGN KEY (Codigo_canal) REFERENCES canal (Codigo),
  PRIMARY KEY (Nombre,Telefono, RIF_Empresa)
);