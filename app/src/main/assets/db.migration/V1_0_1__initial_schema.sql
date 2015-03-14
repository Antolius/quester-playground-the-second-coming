CREATE TABLE quests(
   id INTEGER PRIMARY KEY NOT NULL,
   name TEXT,
   original_id INTEGER,
   version INTEGER
);

CREATE TABLE checkpoints(
   id INTEGER PRIMARY KEY NOT NULL,
   name TEXT,
   root INTEGER,
   viewHtmlFileName TEXT,
   eventsScriptFileName TEXT
);

CREATE TABLE graph(
   id INTEGER PRIMARY KEY NOT NULL,
   quest_id INTEGER NOT NULL,
   parent_id INTEGER NOT NULL,
   child_id INTEGER NOT NULL
);