package com.xuno.message.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

@Component
public class MessageEncrypt implements AttributeConverter<String, String> {

    final
    EncryptionUtil encryptionUtil;

    public MessageEncrypt(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptionUtil.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptionUtil.decrypt(s);
    }
}
