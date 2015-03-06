package com.quester.experiment.dagger2experiment.data.game;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;

import java.util.LinkedList;

/**
 * Created by Josip on 14/01/2015!
 */
public class QuestState {

    private final long questId;
    private final Quest quest;
    private final LinkedList<Checkpoint> visitedCheckpoints = new LinkedList<>();

    public QuestState(long questId, Quest quest) {
        this.questId = questId;
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }

    public QuestGraph getQuestGraph() {
        return quest.getQuestGraph();
    }

    public void setCheckpointAsVisited(final Checkpoint checkpoint) {
        if (!quest.getQuestGraph().containsCheckpoint(checkpoint)) {
            throw new IllegalArgumentException("Checkpoint does not exist in quest graph");
        }

        visitedCheckpoints.add(checkpoint);
    }

    public Checkpoint getActiveCheckpoint() {
        return visitedCheckpoints.peekLast();
    }

    public LinkedList<Checkpoint> getVisitedCheckpoints() {
        return visitedCheckpoints;
    }

    public long getQuestId() {
        return questId;
    }
}
