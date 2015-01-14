package com.quester.experiment.dagger2experiment.data.quest;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import java.util.HashSet;

/**
 * Created by Josip on 14/01/2015!
 */
public class QuestGraphUtils {

    public static HashSet<Checkpoint> getRootCheckpoints(final QuestGraph questGraph) {
        HashSet<Checkpoint> rootCheckpoints = new HashSet<>();

        for (Checkpoint checkpoint : questGraph.getAllCheckpoints()) {
            if (checkpoint.isRoot()) {
                rootCheckpoints.add(checkpoint);
            }
        }

        return rootCheckpoints;
    }

}
