package dev.azuuure.fundamentals.api.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public final class ValidationUtils {

    public static final Pattern DISCORD_INVITE_PATTERN
            = Pattern.compile("(https?://)?(www\\.)?((discordapp\\.com/invite)|(discord\\.gg))/(\\w+)");

    public static final Pattern URL_PATTERN
            = Pattern.compile("https?://(www.)?[a-zA-Z]+(\\.[a-zA-Z]+)+(/(\\w|[-_%.#?=&+])+)+");

    private ValidationUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isDiscordInvite(String invite) {
        return DISCORD_INVITE_PATTERN.matcher(invite).matches();
    }

    public static boolean isValidURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
