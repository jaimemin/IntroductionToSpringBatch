package com.tistory.jaimemin.springbatch.batch.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ProductVODeserializer extends JsonDeserializer<ProductVO> {

    @Override
    public ProductVO deserialize(JsonParser jsonParser
            , DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        long id = jsonNode.get("id").asLong();
        String name = jsonNode.get("name").asText();
        int price = jsonNode.get("price").asInt();
        String type = jsonNode.get("type").asText();

        return new ProductVO(id, name, price, type);
    }
}
