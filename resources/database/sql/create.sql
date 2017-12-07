--ALTER TABLE tasks RENAME TO temp;  -- Rename table

--drop table tasks; --- Do not need when rename Table using ALTER 

CREATE TABLE milestones
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  cid INTEGER default 0,
  tid INTEGER default 0,
  type,class

  -- tasks Table
  --pid INTEGER default 0, title

  -- descriptions Table
  --tid INTEGER default 0, type, owner, content,duration default 0.5,
  --date TIMESTAMP DEFAULT (datetime('now','localtime'))

  --tid INTEGER default -1,  --No task id
  --status,due,title  --tasks
  --complete INTEGER,
  --status integer
  --type integer --comments
  --status, type, class, owner  --select_lists
  --type integer, owner integer, date TIMESTAMP DEFAULT (datetime('now','localtime')), content, duration default 0.5 --descriptions
);

--INSERT INTO descriptions(id,tid,type,owner,content,duration,date) select id,tid,type,owner,content,duration,date from temp;

--drop table temp;