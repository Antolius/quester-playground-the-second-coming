package com.quester.experiment.dagger2experiment.data.checkpoint.area;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public abstract class JsonAreaConverter extends StringBasedTypeConverter<Area> {

    public abstract Area parseJson(JsonParser parser) throws IOException;

    @Override
    public Area parse(JsonParser jsonParser) throws IOException {
        return parseJson(jsonParser);
    }

    public abstract void convert(Area object, JsonGenerator jsonGenerator) throws IOException;

    @Override
    public void serialize(Area object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        convert(object, jsonGenerator);
    }

    @Override
    public Area getFromString(String string) {
        throw new RuntimeException("This method should not be invoked");
    }

    @Override
    public String convertToString(Area area) {
        throw new RuntimeException("This method should not be invoked");
    }
}
