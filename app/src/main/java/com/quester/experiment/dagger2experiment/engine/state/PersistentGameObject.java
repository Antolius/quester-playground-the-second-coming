package com.quester.experiment.dagger2experiment.engine.state;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Josip on 14/01/2015!
 */
public class PersistentGameObject {

    private final String stringRepresentation;

    private PersistentGameObject(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static PersistentGameObject fromString(String stringRepresentation) throws JSONException {
        new JSONObject(stringRepresentation);
        return new PersistentGameObject(stringRepresentation);
    }

    public static PersistentGameObject fromJSON(JSONObject jsonRepresentation) {
        return new PersistentGameObject(jsonRepresentation.toString());
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public JSONObject getJSONRepresentation() throws JSONException {
        return new JSONObject(stringRepresentation);
    }

}
