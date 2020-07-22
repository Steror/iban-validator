package ibanvalidator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This class check the validity of given IBAN numbers and sends back true/false as response
// Algorithm for validating the IBAN is taken from https://en.wikipedia.org/wiki/International_Bank_Account_Number#Validating_the_IBAN
@Service
public class Validator {

    private final Map<String, Integer> CODE_LENGTHS;

    public Validator() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("AL", 28); map.put("AD", 24); map.put("AT", 20); map.put("AZ", 28); map.put("BH", 22); // Countries from the main list at https://www.iban.com/structure
        map.put("BY", 28); map.put("BE", 16); map.put("BA", 20); map.put("BR", 29); map.put("BG", 22);
        map.put("CR", 22); map.put("HR", 21); map.put("CY", 28); map.put("CZ", 24); map.put("DK", 18);
        map.put("DO", 28); map.put("EG", 29); map.put("SV", 28); map.put("EE", 20); map.put("FO", 18);
        map.put("FI", 18); map.put("FR", 27); map.put("GE", 22); map.put("DE", 22); map.put("GI", 23);
        map.put("GR", 27); map.put("GL", 18); map.put("GT", 28); map.put("VA", 22); map.put("HU", 28);
        map.put("IS", 26); map.put("IQ", 23); map.put("IE", 22); map.put("IL", 23); map.put("IT", 27);
        map.put("JO", 30); map.put("KZ", 20); map.put("XK", 20); map.put("KW", 30); map.put("LV", 21);
        map.put("LB", 28); map.put("LI", 21); map.put("LT", 20); map.put("LU", 20); map.put("MT", 31);
        map.put("MR", 27); map.put("MU", 30); map.put("MD", 24); map.put("MC", 27); map.put("ME", 22);
        map.put("NL", 18); map.put("MK", 19); map.put("NO", 15); map.put("PK", 24); map.put("PS", 29);
        map.put("PL", 28); map.put("PT", 25); map.put("QA", 29); map.put("RO", 24); map.put("LC", 32);
        map.put("SM", 27); map.put("ST", 25); map.put("SA", 24); map.put("RS", 22); map.put("SC", 31);
        map.put("SK", 24); map.put("SI", 19); map.put("ES", 24); map.put("SE", 24); map.put("CH", 21);
        map.put("TL", 23); map.put("TN", 24); map.put("TR", 26); map.put("UA", 29); map.put("AE", 23);
        map.put("GB", 22); map.put("VG", 24);
        CODE_LENGTHS = map;
    }

    public boolean isValidIban(String number) {
        String iban = number.toUpperCase().replaceAll("[^A-Z0-9]", ""); // Remove any non alpha-numeric characters
        Pattern pattern = Pattern.compile("^([A-Z]{2})(\\d{2})([A-Z\\d]+)$"); // match and capture (1) the country code, (2) the check digits, and (3) the rest
        Matcher code = pattern.matcher(iban);                                   //
        if (!code.matches()) {
            return false;
        }
        // Check if code contains all 3 groups
        if (code.groupCount() != 3) {
            return false;
        }
        // Check if country code matches valid country codes
        if (!CODE_LENGTHS.containsKey(code.group(1))) {
            return false;
        }
        // Check if IBAN length matches country code
        if (iban.length() != CODE_LENGTHS.get(code.group(1))) {
            return false;
        }
        String rearrangedDigits = code.group(3) + code.group(1) + code.group(2);
        StringBuilder sbDigits = new StringBuilder();
        for (int i=0; i<rearrangedDigits.length(); i++) {
            // If character is a letter, it gets replaced with two digits, expanding the string, where A = 10, B = 11, ..., Z = 35
            // Letters are replaced with digits by converting them to their ASCII value and subtracting 55
            if (String.valueOf(rearrangedDigits.charAt(i)).matches("[A-Z]")) {
                sbDigits.append((((int) rearrangedDigits.charAt(i)) - 55));
            }
            // Else just add the digit to the rest of the string
            else {
                sbDigits.append(rearrangedDigits.charAt(i));
            }
        }
        // Convert StringBuilder to String and perform modulo by 97
        // If checksum is equal to 1, the IBAN is valid, return true
        String digits = sbDigits.toString();
        return mod97(digits) == 1;
    }

    public Map<String, Boolean> isValidIban(List<String> ibanList) {
        System.out.println("Number of lines " + ibanList.size());
        Map<String, Boolean> ibanResult = new HashMap<>();
        for (String s : ibanList) {
            ibanResult.put(s, isValidIban(s));
        }
        return ibanResult;
    }

    // This method performs modulo by 97 operation and returns the checksum
    private int mod97 (String string) {
        int checksum = Integer.parseInt(string.substring(0, 2));
        StringBuilder fragment;
        // First operation is done with 9 digits directly from string
        // After that concatenate the result with the next 7 digits, the last operation is completed with whatever is left
        for (int offset=2; offset<string.length(); offset+=7) {
            // If the string no longer has 7 digits remaining, concatenate the string with whatever is left
            if (string.substring(offset).length() < 7) {
                fragment = new StringBuilder(checksum + string.substring(offset, offset + string.substring(offset).length()));
            }
            // Else concatenate remainder with the next 7 digits
            else {
                fragment = new StringBuilder(checksum + string.substring(offset, offset + 7));
            }
            checksum = Integer.parseInt(fragment.toString()) % 97;
        }
        return checksum;
    }
}
