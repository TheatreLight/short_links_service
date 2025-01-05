import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Link {
    private String originalLink;
    private String shortLink;
    private int clickLimit;
    private LocalDateTime expirationDate;

    private void generateShortLink() {
        shortLink = "mockedShortLink";
    }

    public Link(String orig) {
        originalLink = orig;
        shortLink = "";
        clickLimit = -1;
        expirationDate = null;
    }

    public void setExpirationDate(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        expirationDate = now.plus(minutes, ChronoUnit.MINUTES);
    }

    public void setClickLLimit(int limit) {
        clickLimit = limit;
    }

    public String getShortLink() {
        if (shortLink.isEmpty()) {
            generateShortLink();
        }
        return shortLink;
    }

    public boolean isExpired() {
        if (expirationDate == null)
            return false;
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public boolean isClickLimitGone() {
        return clickLimit == 0;
    }
}
