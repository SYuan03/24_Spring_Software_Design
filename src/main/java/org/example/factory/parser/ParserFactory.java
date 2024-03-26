package org.example.factory.parser;

import org.example.parser.JsonParser;
import org.example.parser.Parser;
import org.example.parser.XmlParser;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 简单工厂感觉就够了，不太会有太多的变化（也许）
 */
public class ParserFactory {
    // 根据后缀名
    public static Parser createParser(String fileName) {
        if (fileName.endsWith(".json")) {
            return new JsonParser();
        } else if (fileName.endsWith(".xml")) {
            return new XmlParser();
        } else {
            return null;
        }
    }
}
