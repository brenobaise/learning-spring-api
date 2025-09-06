package com.baisebreno.learning_spring_api.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {


    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param page       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     *                    serializing Objects value contains, if any.
     */
    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("content", page.getContent());
        gen.writeNumberField("size" , page.getSize());
        gen.writeNumberField("totalElements" , page.getTotalElements());
        gen.writeNumberField("totalPages" , page.getTotalPages());
        gen.writeNumberField("number" , page.getNumber());
        gen.writeEndObject();
    }
}
