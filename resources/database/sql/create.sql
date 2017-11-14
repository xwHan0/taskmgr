---ALTER TABLE tasks RENAME TO temp;

---declare @tbl_name text;

---set @tbl_name status;

drop table descriptions;

CREATE TABLE descriptions
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  tid INTEGER default -1,  --No task id
  --cid INTEGER,
  --pid INTEGER DEFAULT 0,
  --status,due,title  --tasks
  --complete INTEGER,
  --status integer
  --type integer --comments
  --status, type, class, owner  --select_lists
  type integer, owner integer, date TIMESTAMP DEFAULT (datetime('now','localtime')), content, duration default 0.5 --descriptions
);

---INSERT INTO tasks () select tbl from temp;

---drop table temp;