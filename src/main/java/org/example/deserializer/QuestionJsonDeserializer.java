package org.example.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.factory.question.QuestionFactory;
import org.example.model.dto.QuestionDTO;
import org.example.model.question.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Slf4j
public class QuestionJsonDeserializer extends JsonDeserializer<Question> {

    @Override
    public Question deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode root = mapper.readTree(jsonParser);
//        log.debug("Root node: {}", root);
        QuestionDTO questionDTO = mapper.treeToValue(root, QuestionDTO.class);
//        log.info("Deserializing question: {}", questionDTO);
        // 调用QuestionFactory的createQuestion方法
        QuestionFactory questionFactory = QuestionFactory.getFactory(questionDTO.getType());
        return questionFactory.createQuestion(questionDTO);
    }
}
