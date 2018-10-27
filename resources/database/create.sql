--ALTER TABLE tasks RENAME TO temp;  -- Rename table

--drop table milestones; --- Do not need when rename Table using ALTER 

CREATE TABLE Task
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  pid INTEGER default 0,
  title NCHAR(255) default "",
  typ NCHAR(32) default "",
  style NCHAR(64) default ""
  
);

-- INSERT INTO tmp(id,tid,type,owner,description,start,finish,status) select id,tid,type,owner,description,start,finish,status from information;

--drop table temp;