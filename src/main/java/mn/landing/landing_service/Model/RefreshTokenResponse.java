package mn.landing.landing_service.Model;

public class RefreshTokenResponse {
    public String accessToken;
    public String refreshToken;

    public RefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
