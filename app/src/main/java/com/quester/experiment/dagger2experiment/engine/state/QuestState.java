package com.quester.experiment.dagger2experiment.engine.state;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;

import java.util.LinkedList;

/**
 * Created by Josip on 14/01/2015!
 */
public class QuestState {

    private final QuestGraph questGraph;
    private final LinkedList<Checkpoint> visitedCheckpoints = new LinkedList<>();

    public QuestState(QuestGraph questGraph) {
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

    LinkedList<Checkpoint> getVisitedCheckpoints() {
        return visitedCheckpoints;
    }
}
