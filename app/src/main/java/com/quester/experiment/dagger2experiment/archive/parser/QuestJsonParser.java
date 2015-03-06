package com.quester.experiment.dagger2experiment.archive.parser;

import com.bluelinelabs.logansquare.LoganSquare;
import com.quester.experiment.dagger2experiment.data.quest.Quest;

import java.io.IOException;

public class QuestJsonParser {

    public Quest parseQuestJson(String json){

        try {
            return LoganSquare.parse(json, Quest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String serializeQuestToJson(Quest quest){

        try{
            return LoganSquare.serialize(quest);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
