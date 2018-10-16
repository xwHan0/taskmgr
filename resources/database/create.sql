--ALTER TABLE tasks RENAME TO temp;  -- Rename table

--drop table milestones; --- Do not need when rename Table using ALTER 

CREATE TABLE information
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  tid INTEGER default 0,
  type TEXT default "subtask",
  owner TEXT default "hanxinwei",
  description TEXT default "",
  start TIMESTAMP DEFAULT (datetime('now','localtime')),
  finish TIMESTAMP DEFAULT (datetime('now','localtime')),
  status TEXT default "collect"
);

--INSERT INTO descriptions(id,tid,type,owner,content,duration,date) select id,tid,type,owner,content,duration,date from temp;

--drop table temp;