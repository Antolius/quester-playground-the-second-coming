package com.quester.experiment.dagger2experiment.data.game;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;

import java.util.LinkedList;

/**
 * Created by Josip on 14/01/2015!
 */
public class QuestState {

    private final long questId;
    private final QuestGraph questGraph;
    private final LinkedList<Checkpoint> visitedCheckpoints = new LinkedList<>();

    public QuestState(long questId, QuestGraph questGraph) {
        this.questId = questId;
        this.questGraph = questGraph;
    }

    public QuestGraph getQuestGraph() {
        return questGraph;
    }

    public void setCheckpointAsVisited(final Checkpoint checkpoint) {
        if (!questGraph.containsCheckpoint(checkpoint)) {
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
