package mn.landing.landing_service.Common;

import java.util.Locale;

public final class SlugUtil {

    private SlugUtil() {}

    public static String normalize(String input) {
        if (input == null) return null;

        String s = input.trim().toLowerCase(Locale.ROOT);

        // space/underscore -> dash
        s = s.replaceAll("[\\s_]+", "-");

        // зөвшөөрөгдөх тэмдэгүүд: a-z 0-9 -
        s = s.replaceAll("[^a-z0-9\\-]", "");

        // олон dash -> нэг
        s = s.replaceAll("\\-+", "-");

        // эх/төгсгөл dash арилгах
        s = s.replaceAll("^\\-+", "").replaceAll("\\-+$", "");

        return s;
    }

    public static boolean isReserved(String slug) {
        if (slug == null) return false;
        return switch (slug) {
            case "api", "www", "admin", "mail", "static", "assets", "cdn" -> true;
            default -> false;
        };
    }
}
