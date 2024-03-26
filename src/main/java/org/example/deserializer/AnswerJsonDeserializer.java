package org.example.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.factory.answer.AnswerFactory;
import org.example.model.answer.Answer;
import org.example.model.dto.AnswerDTO;

import java.io.IOException;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Slf4j
public class AnswerJsonDeserializer extends JsonDeserializer<Answer> {

    @Override
    public Answer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode root = mapper.readTree(jsonParser);
        AnswerDTO answerDTO = mapper.treeToValue(root, AnswerDTO.class);
        log.info("Deserializing answer: {}", answerDTO);
        // 调用AnswerFactory的createAnswer方法
        AnswerFactory answerFactory = AnswerFactory.getFactory();
        return answerFactory.createAnswer(answerDTO);
    }
}
