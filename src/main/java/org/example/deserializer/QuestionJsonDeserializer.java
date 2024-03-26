package org.example.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.ProgrammingQuestion;
import org.example.model.question.Question;
import org.example.model.question.SingleChoiceQuestion;

import java.io.IOException;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class QuestionJsonDeserializer extends JsonDeserializer<Question> {
    @Override
    public Question deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);



    }
}
