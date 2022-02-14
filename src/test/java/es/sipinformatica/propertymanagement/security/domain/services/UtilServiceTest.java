package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UtilServiceTest {

    @Test
    void validateNIF() {

        // DNI que no son válidos por el Ministerio de interior
        assertFalse(UtilService.validateNIF("00000000T"));
        assertFalse(UtilService.validateNIF("00000001R"));
        assertFalse(UtilService.validateNIF("99999999R"));

        // Strings que no cumplen la expresion regular
        assertFalse(UtilService.validateNIF("0123"));
        assertFalse(UtilService.validateNIF("01234a67Z"));
        assertFalse(UtilService.validateNIF("012345678-"));
        assertFalse(UtilService.validateNIF("0123456789"));

        // DNI que si cumplen todas las validaciones
        assertTrue(UtilService.validateNIF("12345678Z"));
        assertTrue(UtilService.validateNIF("45673254S"));
        assertTrue(UtilService.validateNIF("72849506L"));
        assertTrue(UtilService.validateNIF("72849506l"));

        // NIF Empresa que si cumplen todas las validaciones
        assertFalse(UtilService.validateNIF("A08001851"));

    }
    @Test
    void validateNIE() {

        // NIE que no son válidos por el Ministerio de interior
        assertFalse(UtilService.validateNIE("X12345678Z"));
        assertFalse(UtilService.validateNIE("Y12345678Z"));
        assertFalse(UtilService.validateNIE("Z12345678Z"));
        assertFalse(UtilService.validateNIE("412345678Y"));

        assertFalse(UtilService.validateNIE("72849506L"));

        assertTrue(UtilService.validateNIE("Y8237411K"));
        assertTrue(UtilService.validateNIE("X1837261K"));
        assertTrue(UtilService.validateNIE("x1837261k"));
        assertTrue(UtilService.validateNIE("Z2725419Q"));

    }

    @Test
    void validateNifBusiness() {

        assertFalse(UtilService.validateNifBusiness("123456789"));
        assertFalse(UtilService.validateNifBusiness("123456789A"));

        // DNI que si cumplen todas las validaciones
        assertFalse(UtilService.validateNifBusiness("72849506L"));

        assertTrue(UtilService.validateNifBusiness("A08001851"));
        assertTrue(UtilService.validateNifBusiness("A29268166"));
        assertTrue(UtilService.validateNifBusiness("a29268166"));
    }

}
