package mn.landing.landing_service.Common;

public final class PathUtil {

    private PathUtil() {}

    // "/About Us" -> "/about-us"
    public static String normalize(String path) {
        if (path == null) return null;
        String p = path.trim().toLowerCase();

        if (!p.startsWith("/")) p = "/" + p;

        // spaces/underscore -> dash
        p = p.replaceAll("[\\s_]+", "-");

        // зөвшөөрөгдөх тэмдэг: a-z 0-9 / -
        p = p.replaceAll("[^a-z0-9/\\-]", "");

        // олон dash
        p = p.replaceAll("\\-+", "-");

        // "//" -> "/"
        p = p.replaceAll("/{2,}", "/");

        // "/" бол хэвээр, бусад нь төгсгөл "/" авна
        if (p.length() > 1 && p.endsWith("/")) p = p.substring(0, p.length() - 1);

        return p;
    }
}
