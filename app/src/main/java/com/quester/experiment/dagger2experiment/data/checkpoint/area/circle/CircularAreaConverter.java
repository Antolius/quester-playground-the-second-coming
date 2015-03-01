package com.quester.experiment.dagger2experiment.data.checkpoint.area.circle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Area;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.JsonAreaConverter;

import java.io.IOException;

public class CircularAreaConverter extends JsonAreaConverter {

    @Override
    public Area parseJson(JsonParser parser) throws IOException {

        Point center = new Point(0.0,0.0);
        double radius = 0.0;

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
                case "radius":
                    radius = parser.getDoubleValue();
                    break;
                case "latitude":
                    center.setLatitude(parser.getDoubleValue());
                    break;
                case "longitude":
                    center.setLongitude(parser.getDoubleValue());
                    break;
            }
            parser.skipChildren();
        }

        return new CircularArea(new Circle(center, radius));
    }

    @Override
    public void convert(Area object, JsonGenerator jsonGenerator) throws IOException {

        jsonGenerator.writeObjectFieldStart(CircularArea.CIRCLE_FIELD_NAME);

        Circle circle = ((CircularArea) object).getCircle();

        jsonGenerator.writeNumberField("radius", circle.getRadius());
        jsonGenerator.writeNumberField("latitude", circle.getCenter().getLatitude());
        jsonGenerator.writeNumberField("longitude", circle.getCenter().getLongitude());

        jsonGenerator.writeEndObject();
    }
}
