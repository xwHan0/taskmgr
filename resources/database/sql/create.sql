--ALTER TABLE tasks RENAME TO temp;  -- Rename table

--drop table milestones; --- Do not need when rename Table using ALTER 

CREATE TABLE relations
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  tid INTEGER default 0,
  pid INTEGER default 0,
  type TEXT default "subtask"

  -- tasks Table
  --tid INTEGER default 0, 
  --title,
  -- due TIMESTAMP DEFAULT (datetime('now','localtime')),
  --type default "tr",
  --class

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