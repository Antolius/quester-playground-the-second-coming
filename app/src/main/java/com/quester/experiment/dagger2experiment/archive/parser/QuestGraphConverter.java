package com.quester.experiment.dagger2experiment.archive.parser;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.CircularArea;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.CircularAreaConverter;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle.RectangularArea;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle.RectangularAreaConverter;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;

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

    private List<Long> parseChildIds(JsonParser jsonParser) throws IOException {

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
            Checkpoint checkpoint = parseCheckpoint(jsonParser);
            checkpoints.put(checkpoint.getId(), checkpoint);
            jsonParser.skipChildren();
        }

        return checkpoints;
    }

    private Checkpoint parseCheckpoint(JsonParser parser) throws IOException {

        Checkpoint checkpoint = new Checkpoint();

        if (parser.getCurrentToken() == null) {
            parser.nextToken();
        }
        if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
            parser.skipChildren();
            return null;
        }
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            parser.nextToken();
            switch (fieldName) {
                case "id":
                    checkpoint.setId(parser.getValueAsLong());
                    break;
                case "name":
                    checkpoint.setName(parser.getValueAsString());
                    break;
                case "html":
                    checkpoint.setViewHtmlFileName(parser.getValueAsString());
                    break;
                case "script":
                    checkpoint.setEventsScriptFileName(parser.getValueAsString());
                    break;
                case CircularArea.CIRCLE_FIELD_NAME:
                    checkpoint.setArea(new CircularAreaConverter().parseJson(parser));
                    break;
                case RectangularArea.RECTANGLE_FIELD_NAME:
                    checkpoint.setArea(new RectangularAreaConverter().parseJson(parser));
            }
            parser.skipChildren();
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
            if(checkpoint.getArea() instanceof CircularArea){
                new CircularAreaConverter().convert(checkpoint.getArea(), jsonGenerator);
            }
            if(checkpoint.getArea() instanceof RectangularArea){
                new RectangularAreaConverter().convert(checkpoint.getArea(), jsonGenerator);
            }

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
