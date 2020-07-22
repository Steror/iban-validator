import ibanvalidator.Validator;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private static final Validator validator = new Validator();

    private static final List<String> IBAN_LIST = new ArrayList<>();
    private static final String INVALID_IBAN1 = "AA051245445454552117989";
    private static final String VALID_IBAN1 = "LT647044001231465456";
    private static final String VALID_IBAN2 = "LT517044077788877777";
    private static final String INVALID_IBAN2 = "LT227044077788877777";
    private static final String INVALID_IBAN3 = "CC051245445454552117989";

    private static final String VALID_IBAN3 = "AL35202111090000000001234567";
    private static final String VALID_IBAN4 = "AD1400080001001234567890";
    private static final String VALID_IBAN5 = "AT483200000012345864";
    private static final String VALID_IBAN6 = "AZ96AZEJ00000000001234567890";
    private static final String VALID_IBAN7 = "BH02CITI00001077181611";
    private static final String VALID_IBAN8 = "BY86AKBB10100000002966000000";
    private static final String VALID_IBAN9 = "BE71096123456769";
    private static final String VALID_IBAN10 = "BA393385804800211234";

    @BeforeAll
    static void Setup() {
        IBAN_LIST.add(INVALID_IBAN1);
        IBAN_LIST.add(VALID_IBAN1);
        IBAN_LIST.add(VALID_IBAN2);
        IBAN_LIST.add(INVALID_IBAN2);
        IBAN_LIST.add(INVALID_IBAN3);
    }

    @org.junit.jupiter.api.Test
    void testIsValidIbanTrue() {
        assertTrue(validator.isValidIban(VALID_IBAN1));
        assertTrue(validator.isValidIban(VALID_IBAN2));
        assertTrue(validator.isValidIban(VALID_IBAN3));
        assertTrue(validator.isValidIban(VALID_IBAN4));
        assertTrue(validator.isValidIban(VALID_IBAN5));
        assertTrue(validator.isValidIban(VALID_IBAN6));
        assertTrue(validator.isValidIban(VALID_IBAN7));
        assertTrue(validator.isValidIban(VALID_IBAN8));
        assertTrue(validator.isValidIban(VALID_IBAN9));
        assertTrue(validator.isValidIban(VALID_IBAN10));
    }

    @org.junit.jupiter.api.Test
    void testIsValidIbanFalse() {
        assertFalse(validator.isValidIban(INVALID_IBAN1));
        assertFalse(validator.isValidIban(INVALID_IBAN2));
        assertFalse(validator.isValidIban(INVALID_IBAN3));
    }

    @org.junit.jupiter.api.Test
    void testIsValidIbans() {
        Map<String, Boolean> results = validator.isValidIban(IBAN_LIST);
        assertEquals(5, results.size());
    }
}
