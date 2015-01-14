package com.quester.experiment.dagger2experiment.data.quest;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Josip on 11/01/2015!
 */
public class QuestGraph {
    private final Map<Checkpoint, Set<Checkpoint>> map;

    /**
     * The only way to make a new Quest Graph is to give it all the checkpoints in advance.
     * It is impossible to add new checkpoints later on.
     *
     * @param checkpoints must not be null nor contain null as an element
     */
    public QuestGraph(final Collection<Checkpoint> checkpoints) {
        if (checkpoints == null || checkpoints.contains(null)) {
            throw new IllegalArgumentException("Collection of checkpoints must not in itself be or contain null.");
        }

        map = new HashMap<Checkpoint, Set<Checkpoint>>(checkpoints.size());
        for (Checkpoint checkpoint : checkpoints) {
            map.put(checkpoint, new HashSet<Checkpoint>());
        }
    }

    /**
     * @return set of all Checkpoints in Quest Graph
     */
    public Set<Checkpoint> getAllCheckpoints() {
        return map.keySet();
    }

    /**
     * @param parent must be already contained in Quest Graph
     * @return Set of children checkpoints
     */
    public Set<Checkpoint> getChildren(final Checkpoint parent) throws IllegalStateException {
        assertCheckpointExists(parent);

        return map.get(parent);
    }

    /**
     * @param start must be already contained in Quest Graph
     * @param end   must be already contained in Quest Graph
     */
    public void addEdge(final Checkpoint start, final Checkpoint end) throws IllegalStateException {
        assertCheckpointExists(start);
        assertCheckpointExists(end);

        map.get(start).add(end);
    }

    /**
     * @param start must be already contained in Quest Graph
     * @param end   must be already contained in Quest Graph
     */
    public void removeEdge(final Checkpoint start, final Checkpoint end) throws IllegalStateException {
        assertCheckpointExists(start);
        assertCheckpointExists(end);

        map.get(start).remove(end);
    }

    /**
     * @param checkpoint
     * @return whether quest graph contains checkpoint
     */
    public boolean containsCheckpoint(Checkpoint checkpoint) {
        return map.containsKey(checkpoint);
    }

    private void assertCheckpointExists(final Checkpoint checkpoint) throws IllegalArgumentException {
        if (!containsCheckpoint(checkpoint)) {
            throw new IllegalArgumentException("Checkpoint does not exist in quest graph");
        }
    }

    @Override
    public String toString() {
        return "QuestGraph{" +
                "map=" + map +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestGraph that = (QuestGraph) o;

        if (map != null ? !map.equals(that.map) : that.map != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return map != null ? map.hashCode() : 0;
    }
}
