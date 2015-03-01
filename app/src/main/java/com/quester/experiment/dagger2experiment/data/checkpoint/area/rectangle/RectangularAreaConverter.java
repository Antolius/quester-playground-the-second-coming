package com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Area;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.JsonAreaConverter;

import java.io.IOException;

public class RectangularAreaConverter extends JsonAreaConverter {

    @Override
    public Area parseJson(JsonParser parser) throws IOException {

        Point upperLeftCorner = new Point(0.0,0.0);
        Point lowerRightCorner = new Point(0.0,0.0);

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
                case "upperLeftCornerLatitude":
                    upperLeftCorner.setLatitude(parser.getDoubleValue());
                    break;
                case "upperLeftCornerLongitude":
                    upperLeftCorner.setLongitude(parser.getDoubleValue());
                    break;
                case "lowerRightCornerLongitude":
                    lowerRightCorner.setLatitude(parser.getDoubleValue());
                    break;
                case "lowerRightCornerLatitude":
                    lowerRightCorner.setLongitude(parser.getDoubleValue());
                    break;
            }
            parser.skipChildren();
        }

        return new RectangularArea(new Rectangle(upperLeftCorner, lowerRightCorner));
    }

    @Override
    public void convert(Area object, JsonGenerator jsonGenerator) throws IOException {

        jsonGenerator.writeObjectFieldStart(RectangularArea.RECTANGLE_FIELD_NAME);

        Rectangle rectangle = ((RectangularArea) object).getRectangle();

        jsonGenerator.writeNumberField(
                "upperLeftCornerLatitude", rectangle.getUpperLeftCorner().getLongitude());
        jsonGenerator.writeNumberField(
                "upperLeftCornerLongitude", rectangle.getUpperLeftCorner().getLongitude());
        jsonGenerator.writeNumberField(
                "lowerRightCornerLatitude", rectangle.getLowerRightCorner().getLatitude());
        jsonGenerator.writeNumberField(
                "lowerRightCornerLongitude", rectangle.getLowerRightCorner().getLongitude());

        jsonGenerator.writeEndObject();
    }
}
