-- ALTER TABLE Task RENAME TO temp;

CREATE TABLE Task
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  pid INTEGER default 0,
  title NCHAR(255) default "",
  typ NCHAR(32) default "",
  style NCHAR(64) default "",
  sid INTEGER,
  eid INTEGET
);

INSERT INTO Task(id,pid,title,typ,style) SELECT id,pid,title,typ,style FROM temp;

--drop table temp;