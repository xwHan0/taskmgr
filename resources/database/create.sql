--ALTER TABLE tasks RENAME TO temp;  -- Rename table

--drop table milestones; --- Do not need when rename Table using ALTER 

CREATE TABLE tmp
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  tid INTEGER default 0,
  type TEXT default "progress",
  owner TEXT default "hanxinwei",
  description TEXT default "",
  start TIMESTAMP DEFAULT (datetime('now','localtime')),
  finish TIMESTAMP DEFAULT (datetime('now','localtime')),
  status TEXT default "collect",
  complete INTEGER default 0
);

INSERT INTO tmp(id,tid,type,owner,description,start,finish,status) select id,tid,type,owner,description,start,finish,status from information;

--drop table temp;