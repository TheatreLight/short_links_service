import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class Link {
    private String originalLink;
    private String shortLink;
    private int clickLimit;
    private Timestamp expirationDate;

    private void generateShortLink() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(originalLink.getBytes());
        String shortUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        shortLink = shortUrl.substring(0, 8);
    }

    private void calcExpirationDate(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        expirationDate = Timestamp.valueOf(now.plus(minutes, ChronoUnit.MINUTES));
    }

    public Link(String orig) {
        originalLink = orig;
        shortLink = "";
        clickLimit = -1;
        calcExpirationDate(30);
    }

    public void setOriginalLink(String orig) {
        originalLink = orig;
    }

    public void setShortLink(String custom) {
        shortLink = custom;
    }

    public void setClickLLimit(int limit) {
        clickLimit = limit;
    }

    public void setExpirationDate(Timestamp exp) {
        expirationDate = exp;
    }

    public int getClicks() {
        return clickLimit;
    }

    public String getShortLink() throws NoSuchAlgorithmException {
        if (shortLink.isEmpty()) {
            generateShortLink();
        }
        return shortLink;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public boolean isExpired() {
        if (expirationDate == null)
            return false;
        return LocalDateTime.now().isAfter(expirationDate.toLocalDateTime());
    }

    public boolean isClickLimitGone() {
        return clickLimit == 0;
    }
}
