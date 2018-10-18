--ALTER TABLE tasks RENAME TO temp;  -- Rename table

--drop table milestones; --- Do not need when rename Table using ALTER 



-- task table
CREATE TABLE task
(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  pid INTEGER default 0,
  title TEXT default "" NOT NULL,
  class NCHAR(20) default "work",
  style TEXT default ""
);
