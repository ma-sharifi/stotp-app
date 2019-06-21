package ir.htsc.model;

import ir.htsc.serializer.GsonExcludeField;
import ir.htsc.serializer.GsonModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
//Copy From Paypal SDK

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreditCard extends GsonModel {

    private String skipMaskedNumber;//6037701234

    /**
     * ID of the credit card. This ID is provided in the response when storing credit cards. **Required if using a stored credit card.**
     */
    private String id;//must be UUID

    /**
     * Credit card number. Numeric characters only with no spaces or punctuation. The string must conform with modulo and value required by each credit card type. *Redacted in responses.*
     */
    @GsonExcludeField
    private String number;


    /**
     * Credit card type. Valid types are: `visa`, `mastercard`, `discover`, `amex`
     */
    private String type;

    /**
     * Expiration month with no leading zero. Acceptable values are 1 through 12.
     */
    private int expireMonth;

    /**
     * 4-digit expiration year.
     */
    private int expireYear;

    /**
     * 3-4 digit card validation code.
     */
    private String cvv2;

    /**
     * Cardholder's first name.
     */
    private String firstName;

    /**
     * Cardholder's last name.
     */
    private String lastName;

    /**
     * Billing Address associated with this card.
     */
    private Address billingAddress;

    /**
     * A unique identifier of the customer to whom this bank account belongs. Generated and provided by the facilitator. **This is now used in favor of `payer_id` when creating or using a stored funding instrument in the vault.**
     */
    private String externalCustomerId;

    /**
     * State of the credit card funding instrument.
     */
    private String state;

    /**
     * Funding instrument expiration date.
     */
    private String validUntil;

    /**
     *
     */
    private List<Links> links;

    /**
     * Payer ID
     */
    private String payerId;


    /**
     * Default Constructor
     */
    public CreditCard() {
    }

    public CreditCard(String number) {
        this.number = number;
        this.skipMaskedNumber = skipMasked();
    }

    public CreditCard(String number, int expireMonth, int expireYear) {
        this.number = number;
        this.skipMaskedNumber = skipMasked();
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
    }

    public boolean isCardValid() {
        if (!"".equals(number) && number != null && number.length() == 16) {
            return true;
        }
        return false;
    }

    private String skipMasked() {
        if (isCardValid()) {
            return number.substring(0, 6) + number.substring(12);//6037701012345678->603770105678
        } else if (number.length() == 10) return number;//603770105678
        throw new RuntimeException("card No format incorrect! card no: " + number);
    }

}
