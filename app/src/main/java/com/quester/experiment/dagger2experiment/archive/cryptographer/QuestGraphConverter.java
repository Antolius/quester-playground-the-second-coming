package com.quester.experiment.dagger2experiment.archive.cryptographer;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestGraphConverter extends StringBasedTypeConverter<QuestGraph> {

    @Override
    public QuestGraph parse(JsonParser jsonParser) throws IOException {

        Map<Long, Checkpoint> checkpoints = new HashMap<>();
        Map<Long, List<Long>> links = new HashMap<>();

        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("checkpoints".equals(fieldName)) {
                checkpoints = parseCheckpoints(jsonParser);
            } else if ("links".equals(fieldName)){
                links = parseLinks(jsonParser);
            }
            jsonParser.skipChildren();
        }

        QuestGraph questGraph = new QuestGraph(checkpoints.values());
        for(Long parentId : links.keySet()){
            for(Long childId : links.get(parentId)){
                questGraph.addEdge(checkpoints.get(parentId), checkpoints.get(childId));
            }
        }

        return questGraph;
    }

    private Map<Long, List<Long>> parseLinks(JsonParser jsonParser) throws IOException {
        Map<Long, List<Long>> links = new HashMap<>();

        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            List<Long> children = parseChildIds(jsonParser);
            links.put(Long.valueOf(fieldName), children);
            jsonParser.skipChildren();
        }

        return links;
    }

    public List<Long> parseChildIds(JsonParser jsonParser) throws IOException {

        List<Long> children = new ArrayList<>();

        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            children.add(jsonParser.getLongValue());
            jsonParser.skipChildren();
        }

        return children;
    }

    private Map<Long, Checkpoint> parseCheckpoints(JsonParser jsonParser) throws IOException {

        Map<Long, Checkpoint> checkpoints = new HashMap<>();

        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            //String fieldName = jsonParser.getCurrentName();
            //jsonParser.nextToken();
            Checkpoint checkpoint = parseCheckpoint(jsonParser);
            checkpoints.put(checkpoint.getId(), checkpoint);
            jsonParser.skipChildren();
        }

        return checkpoints;
    }

    private Checkpoint parseCheckpoint(JsonParser jsonParser) throws IOException {

        Checkpoint checkpoint = new Checkpoint();

        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            switch (fieldName) {
                case "id":
                    checkpoint.setId(jsonParser.getValueAsLong());
                    break;
                case "name":
                    checkpoint.setName(jsonParser.getValueAsString());
                    break;
                case "html":
                    checkpoint.setViewHtmlFileName(jsonParser.getValueAsString());
                    break;
                case "script":
                    checkpoint.setEventsScriptFileName(jsonParser.getValueAsString());
                    break;
            }
            jsonParser.skipChildren();
        }
        return checkpoint;
    }

    @Override
    public void serialize(QuestGraph object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {

        jsonGenerator.writeObjectFieldStart(fieldName);

        jsonGenerator.writeArrayFieldStart("checkpoints");
        for (Checkpoint checkpoint : object.getAllCheckpoints()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", checkpoint.getId());
            jsonGenerator.writeStringField("name", checkpoint.getName());
            jsonGenerator.writeStringField("html", checkpoint.getViewHtmlFileName());
            jsonGenerator.writeStringField("script", checkpoint.getEventsScriptFileName());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeObjectFieldStart("links");
        for (Checkpoint checkpoint : object.getAllCheckpoints()) {
            jsonGenerator.writeArrayFieldStart(String.valueOf(checkpoint.getId()));
            for (Checkpoint child : object.getChildren(checkpoint)) {
                jsonGenerator.writeNumber(child.getId());
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }


    @Override
    public QuestGraph getFromString(String string) {
        throw new RuntimeException("This method should not be invoked");
    }

    @Override
    public String convertToString(QuestGraph graph) {
        throw new RuntimeException("This method should not be invoked");
    }
}
