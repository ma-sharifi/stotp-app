package ir.htsc.entity.convertor;

import ir.htsc.model.CreditCard;
import ir.htsc.security.AES;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Base64;

/**
 * Created by MSH on 5/27/2019.
 */
@Converter
public class CreditCardConverterForPersist implements AttributeConverter<CreditCard, String> {
    private static final String CARD_AES_KEY = "kalSD^vEaM79e4!V";

    @Override
    public String convertToDatabaseColumn(CreditCard creditCard) {
        String result = "";
        if (creditCard!=null && !"".equals(creditCard.getNumber()) && creditCard.getNumber() != null &&  creditCard.getNumber().length()>15)
            result=AES.encrypt(CARD_AES_KEY, creditCard.getNumber());
        return result;
    }

    @Override
    public CreditCard convertToEntityAttribute(String number) {
        //if you have any secure convert should be here
        CreditCard creditCard = null;
        if (!"".equals(number) && number != null && number.length()>15) {
            creditCard = new CreditCard(AES.decryptString(CARD_AES_KEY, Base64.getDecoder().decode(number)));
        }
        return creditCard;
    }
}
