package org.example.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.dto.QuestionDTO;
import org.example.model.question.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.factory.question.QuestionFactory;

import java.io.IOException;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class QuestionJsonDeserializer extends JsonDeserializer<Question> {

    private static final Logger logger = LoggerFactory.getLogger(QuestionJsonDeserializer.class);
    @Override
    public Question deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);
        QuestionDTO questionDTO = mapper.treeToValue(root, QuestionDTO.class);
        logger.info("Deserializing question: {}", questionDTO);
        // 调用QuestionFactory的createQuestion方法
        QuestionFactory questionFactory = QuestionFactory.getFactory(questionDTO.getType());
        return questionFactory.createQuestion(questionDTO);
    }
}
