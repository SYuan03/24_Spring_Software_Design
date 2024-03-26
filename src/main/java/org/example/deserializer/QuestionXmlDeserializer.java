package org.example.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.factory.question.QuestionFactory;
import org.example.model.dto.QuestionDTO;
import org.example.model.question.Question;

import java.io.IOException;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Slf4j
public class QuestionXmlDeserializer extends JsonDeserializer<Question> {

    @Override
    public Question deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        XmlMapper xmlMapper = (XmlMapper) jsonParser.getCodec();
        // 直接将XML解析成目标对象，而不是转换为JsonNode
        QuestionDTO questionDTO = xmlMapper.readValue(jsonParser, QuestionDTO.class);
//        log.info("Deserializing question: {}", questionDTO);

        // 调用QuestionFactory的createQuestion方法
        QuestionFactory questionFactory = QuestionFactory.getFactory(questionDTO.getType());
        return questionFactory.createQuestion(questionDTO);
    }
}