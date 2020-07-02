package vazkii.botania.client.core.helper;

import java.text.NumberFormat;

public class I18NHelper {
    private static final NumberFormat numberFormat = NumberFormat.getIntegerInstance();

    public static String formatInteger(int value) {
        return numberFormat.format(value);
    }
}
