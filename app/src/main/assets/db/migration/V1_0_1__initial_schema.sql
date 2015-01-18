CREATE TABLE quests(
   id           INTEGER PRIMARY KEY     NOT NULL,
   name          TEXT     NOT NULL
);

CREATE TABLE checkpoints(
   id            INTEGER PRIMARY KEY   NOT NULL,
   quest_id      INTEGER   NOT NULL,
   name          TEXT     NOT NULL
);

CREATE TABLE connections(
   parent_id     INTEGER   NOT NULL,
   child_id      INTEGER   NOT NULL,
   FOREIGN KEY (parent_id) REFERENCES checkpoints(id),
   FOREIGN KEY (child_id) REFERENCES checkpoints(id),
   PRIMARY KEY (parent_id, child_id)
);