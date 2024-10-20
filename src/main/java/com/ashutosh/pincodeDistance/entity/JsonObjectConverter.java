package com.ashutosh.pincodeDistance.entity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JsonObjectConverter implements AttributeConverter<JsonObject, String> {

        private static final Gson gson = new Gson();

        @Override
        public String convertToDatabaseColumn(JsonObject attribute) {
            // Convert JsonObject to its JSON string representation
            return attribute != null ? gson.toJson(attribute) : null;
        }

        @Override
        public JsonObject convertToEntityAttribute(String dbData) {
            // Convert JSON string back to JsonObject
            return dbData != null ? gson.fromJson(dbData, JsonObject.class) : null;
        }
}
