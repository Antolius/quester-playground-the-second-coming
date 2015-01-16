package com.quester.experiment.dagger2experiment.data.quest;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
public class QuestGraph {

    private final HashMap<Checkpoint, HashSet<Checkpoint>> questGraphMap;

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

        questGraphMap = new HashMap<Checkpoint, HashSet<Checkpoint>>(checkpoints.size());
        for (Checkpoint checkpoint : checkpoints) {
            questGraphMap.put(checkpoint, new HashSet<Checkpoint>());
        }
    }

    /**
     * This constructor is not to be used for purposes other than instantiating
     * QuestGraph object from parcel.
     *
     * @param questGraphMap
     */
    @ParcelConstructor
    public QuestGraph(HashMap<Checkpoint, HashSet<Checkpoint>> questGraphMap) {
        this.questGraphMap = questGraphMap;
    }

    /**
     * This getter is not to be used for purposes other that creating a parcel
     *
     * @return
     */
    public HashMap<Checkpoint, HashSet<Checkpoint>> getQuestGraphMap() {
        return questGraphMap;
    }

    /**
     * @return set of all Checkpoints in Quest Graph
     */
    public Set<Checkpoint> getAllCheckpoints() {
        return questGraphMap.keySet();
    }

    /**
     * @param parent must be already contained in Quest Graph
     * @return Set of children checkpoints
     */
    public Set<Checkpoint> getChildren(final Checkpoint parent) throws IllegalStateException {
        assertCheckpointExists(parent);

        return questGraphMap.get(parent);
    }

    /**
     * @param start must be already contained in Quest Graph
     * @param end   must be already contained in Quest Graph
     */
    public void addEdge(final Checkpoint start, final Checkpoint end) throws IllegalStateException {
        assertCheckpointExists(start);
        assertCheckpointExists(end);

        questGraphMap.get(start).add(end);
    }

    /**
     * @param start must be already contained in Quest Graph
     * @param end   must be already contained in Quest Graph
     */
    public void removeEdge(final Checkpoint start, final Checkpoint end) throws IllegalStateException {
        assertCheckpointExists(start);
        assertCheckpointExists(end);

        questGraphMap.get(start).remove(end);
    }

    /**
     * @param checkpoint
     * @return whether quest graph contains checkpoint
     */
    public boolean containsCheckpoint(Checkpoint checkpoint) {
        return questGraphMap.containsKey(checkpoint);
    }

    private void assertCheckpointExists(final Checkpoint checkpoint) throws IllegalArgumentException {
        if (!containsCheckpoint(checkpoint)) {
            throw new IllegalArgumentException("Checkpoint does not exist in quest graph");
        }
    }

    @Override
    public String toString() {
        return "QuestGraph{" +
                "questGraphMap=" + questGraphMap +
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

        if (questGraphMap != null ? !questGraphMap.equals(that.questGraphMap) : that.questGraphMap != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return questGraphMap != null ? questGraphMap.hashCode() : 0;
    }
}
