package com.quester.experiment.dagger2experiment.archive;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;

import java.io.IOException;

public class QuestGraphConverter extends StringBasedTypeConverter<QuestGraph> {

    @Override
    public QuestGraph getFromString(String string) {
        return null;
    }

    @Override
    public String convertToString(QuestGraph graph) {
        return null;
    }

    @Override
    public QuestGraph parse(JsonParser jsonParser) throws IOException {


        jsonParser.

        jsonParser.get
        return getFromString(jsonParser.getValueAsString(null));
    }

    public void _parse(JsonParser jsonParser) throws IOException {

        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();

            parseField(fieldName, jsonParser);
            jsonParser.skipChildren();
        }
    }

    public static void parseField(Quest instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("name".equals(fieldName)) {
            instance.setName(jsonParser.getValueAsString(null));
        } else if ("questGraph".equals(fieldName)){
            instance.setQuestGraph(QUEST_GRAPH_CONVERTER.parse(jsonParser));
        } else if ("id".equals(fieldName)){
            instance.setId(jsonParser.getValueAsLong());
        }
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
}
