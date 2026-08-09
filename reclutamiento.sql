-- Proyecto: Análisis de Reclutamiento con SQL Server
-- Autor: Arud Rayn Sánchez Llerena
-- Datos ficticios para fines demostrativos

CREATE DATABASE ReclutamientoDB;
GO
USE ReclutamientoDB;
GO

CREATE TABLE areas (
    area_id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE vacantes (
    vacante_id INT IDENTITY(1,1) PRIMARY KEY,
    area_id INT NOT NULL,
    puesto VARCHAR(120) NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('Activa','Cerrada','Pausada')),
    fecha_apertura DATE NOT NULL,
    fecha_cierre DATE NULL,
    FOREIGN KEY (area_id) REFERENCES areas(area_id)
);

CREATE TABLE candidatos (
    candidato_id INT IDENTITY(1,1) PRIMARY KEY,
    vacante_id INT NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    etapa VARCHAR(30) NOT NULL,
    fuente VARCHAR(40) NOT NULL,
    fecha_postulacion DATE NOT NULL,
    FOREIGN KEY (vacante_id) REFERENCES vacantes(vacante_id)
);

INSERT INTO areas(nombre) VALUES ('Tecnología'),('Operaciones'),('Comercial'),('Finanzas');
INSERT INTO vacantes(area_id,puesto,estado,fecha_apertura,fecha_cierre) VALUES
(1,'Analista de Datos Jr.','Activa','2026-05-02',NULL),(1,'Desarrollador Java','Cerrada','2026-02-01','2026-02-19'),
(2,'Supervisor de Operaciones','Cerrada','2026-03-05','2026-04-03'),(3,'Ejecutivo Comercial','Cerrada','2026-04-01','2026-04-23'),
(4,'Analista Financiero','Cerrada','2026-01-12','2026-02-06');

INSERT INTO candidatos(vacante_id,nombre,etapa,fuente,fecha_postulacion) VALUES
(1,'Candidato 001','Entrevista','LinkedIn','2026-05-04'),
(1,'Candidato 002','Preselección','Portal laboral','2026-05-05'),
(1,'Candidato 003','Postulación','Web corporativa','2026-05-07'),
(2,'Candidato 004','Contratado','Referidos','2026-02-02'),
(2,'Candidato 005','Entrevista','LinkedIn','2026-02-04'),
(3,'Candidato 006','Contratado','Referidos','2026-03-07'),
(3,'Candidato 007','Finalista','Portal laboral','2026-03-09'),
(3,'Candidato 008','Preselección','LinkedIn','2026-03-11'),
(4,'Candidato 009','Contratado','LinkedIn','2026-04-02'),
(4,'Candidato 010','Entrevista','Portal laboral','2026-04-05'),
(5,'Candidato 011','Contratado','Referidos','2026-01-14'),
(5,'Candidato 012','Preselección','Web corporativa','2026-01-16');

-- Consulta 1: conversión por etapa
SELECT etapa, COUNT(*) AS candidatos,
CAST(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER() AS DECIMAL(5,1)) AS porcentaje
FROM candidatos GROUP BY etapa ORDER BY candidatos DESC;

-- Consulta 2: candidatos y contrataciones por área
SELECT a.nombre AS area, COUNT(c.candidato_id) AS candidatos,
SUM(CASE WHEN c.etapa='Contratado' THEN 1 ELSE 0 END) AS contratados
FROM areas a JOIN vacantes v ON v.area_id=a.area_id
JOIN candidatos c ON c.vacante_id=v.vacante_id
GROUP BY a.nombre ORDER BY candidatos DESC;

-- Consulta 3: tiempo promedio de cobertura
SELECT a.nombre AS area, AVG(DATEDIFF(DAY,v.fecha_apertura,v.fecha_cierre)) AS dias_promedio
FROM vacantes v JOIN areas a ON a.area_id=v.area_id
WHERE v.estado='Cerrada' GROUP BY a.nombre ORDER BY dias_promedio;
