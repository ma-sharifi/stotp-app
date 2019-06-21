package ir.htsc.entity.convertor;

import ir.htsc.enums.OTPUsedFor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author MSH on 6/20/2019 at 06:02 PM.
 */
@Converter
public class OTPUsedForConvertor implements AttributeConverter<OTPUsedFor, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OTPUsedFor attribute) {
        if (attribute == null)
            return -1;
        return attribute.getValue();
    }

    @Override
    public OTPUsedFor convertToEntityAttribute(Integer value) {
        return OTPUsedFor.getEnum(value);
    }
}
