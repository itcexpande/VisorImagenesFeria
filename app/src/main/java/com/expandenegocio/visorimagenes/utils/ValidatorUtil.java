package com.expandenegocio.visorimagenes.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Penlopjo on 01/04/2016.
 */
public class ValidatorUtil {

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate given email with regular expression.
     *
     * @param email email for validation
     * @return true valid email, otherwise false
     */
    public static boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean validateTel(String contact, String countrycode) {
        Phonenumber.PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countrycode));
        boolean isValid = false;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(contact, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);

        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return isValid;
    }

}
