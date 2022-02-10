package es.sipinformatica.propertymanagement.security.domain.services;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceForbiddenException;

@Service
public class UtilService {

    private static final Pattern NIEREGEXP = Pattern.compile("[XYZ][0-9]{7}[A-Z]");
    private static final Pattern NIFREGEXP = Pattern.compile("[0-9]{8}[A-Z]");
    private static final String DIGITO_CONTROL = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String[] INVALIDOS = new String[] { "00000000T", "00000001R", "99999999R" };

    private static final Pattern cifPattern = Pattern.compile("[[A-H][J-N][P-S]UVW][0-9]{7}[0-9A-J]");
    private static final String CONTROL_SOLO_NUMEROS = "ABEH"; // Sólo admiten números como caracter de control
    private static final String CONTROL_SOLO_LETRAS = "KPQS"; // Sólo admiten letras como caracter de control
    private static final String CONTROL_NUMERO_A_LETRA = "JABCDEFGHI"; // Conversión de dígito a letra de control.

    public static boolean validateNIF(String dni) {
        dni = dni.toUpperCase();
        return Arrays.binarySearch(INVALIDOS, dni) < 0
                && NIFREGEXP.matcher(dni).matches()
                && dni.charAt(8) == DIGITO_CONTROL.charAt(Integer.parseInt(dni.substring(0, 8)) % 23);
    }

    public static boolean validateNIE(String nie) {
        nie = nie.toUpperCase();
        if (NIEREGEXP.matcher(nie).matches()) {
            switch (nie.charAt(0)) {
                case 'X':
                    nie = nie.replace('X', '0');
                    return validateNIF(nie);
                case 'Y':
                    nie = nie.replace('Y', '1');
                    return validateNIF(nie);
                case 'Z':
                    nie = nie.replace('Z', '2');
                    return validateNIF(nie);
            }
        }
        return false;
    }

    public static boolean validateNifBusiness(String nif) {
        nif = nif.toUpperCase();
        try {
            if (!cifPattern.matcher(nif).matches()) {
                // No cumple el patrón
                return false;
            }

            int parA = 0;
            for (int i = 2; i < 8; i += 2) {
                final int digito = Character.digit(nif.charAt(i), 10);
                if (digito < 0) {
                    return false;
                }
                parA += digito;
            }

            int nonB = 0;
            for (int i = 1; i < 9; i += 2) {
                final int digito = Character.digit(nif.charAt(i), 10);
                if (digito < 0) {
                    return false;
                }
                int nn = 2 * digito;
                if (nn > 9) {
                    nn = 1 + (nn - 10);
                }
                nonB += nn;
            }

            final int parcialC = parA + nonB;
            final int digitoE = parcialC % 10;
            final int digitoD = (digitoE > 0) ? (10 - digitoE) : 0;
            final char letraIni = nif.charAt(0);
            final char caracterFin = nif.charAt(8);

            final boolean esControlValido =
                    // ¿el caracter de control es válido como letra?
                    (CONTROL_SOLO_NUMEROS.indexOf(letraIni) < 0
                            && CONTROL_NUMERO_A_LETRA.charAt(digitoD) == caracterFin)
                            ||
                            // ¿el caracter de control es válido como dígito?
                            (CONTROL_SOLO_LETRAS.indexOf(letraIni) < 0 && digitoD == Character.digit(caracterFin, 10));
            return esControlValido;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateNieNifNifBusiness(String dni) {
        if (Boolean.FALSE.equals(UtilService.validateNIF(dni)) && Boolean.FALSE.equals(UtilService.validateNIE(dni))
                && Boolean.FALSE.equals(UtilService.validateNifBusiness(dni))) {            
            return false;
        }
        return true;

    }

}
