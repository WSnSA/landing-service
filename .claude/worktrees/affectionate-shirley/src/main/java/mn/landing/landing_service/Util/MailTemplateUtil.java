package mn.landing.landing_service.Util;

public class MailTemplateUtil {

    public static String forgotPassword(String token) {
        return """
            <html>
              <body style="font-family:Arial">
                <h2>Нууц үг сэргээх</h2>
                <p>Доорх товчоор орж нууц үгээ солино уу.</p>
                <a href="http://103.87.255.135:3000/reset-password?token=%s"
                   style="padding:10px 16px;
                          background:#2563EB;
                          color:white;
                          text-decoration:none;
                          border-radius:6px;">
                  Нууц үг сэргээх
                </a>
                <p>Энэ холбоос 30 минут хүчинтэй.</p>
              </body>
            </html>
        """.formatted(token);
    }
}