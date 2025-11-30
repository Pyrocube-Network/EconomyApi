package net.pyrocube.economyapi.api.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a {@link RuntimeException}, indicating failure, without a stacktrace.
 */
public final class EconomyException extends RuntimeException {

    private final Function<Locale, String> messageSupplier;

    /**
     * Create a new {@code EconomyException}
     *
     * @param message a message, representing a reason why failure occurred
     */
    public EconomyException(@NotNull String message) {
        super(message, null, false, false);
        this.messageSupplier = ($) -> message;
    }

    /**
     * Create a new {@code EconomyException} with a {@link Function} that provides a localized
     * message.
     * <p>{@link Locale#ENGLISH} message must be always provided.
     *
     * @param messageSupplier supplier of localized messages
     */
    public EconomyException(@NotNull Function<Locale, String> messageSupplier) {
        super(verifyEnglishMessage(messageSupplier), null, false, false);
        this.messageSupplier = messageSupplier;
    }

    @NotNull
    private static String verifyEnglishMessage(@NotNull Function<Locale, String> messageSupplier) {
        return Objects.requireNonNull(
                messageSupplier.apply(Locale.ENGLISH),
                "No Locale#ENGLISH message provided when creating a TreasuryException with a message supplier."
        );
    }

    /**
     * Get a localized message.
     *
     * @param locale the locale for which a message is needed
     * @return if the {@code EconomyException} object this method is called from is created via
     * {@link #EconomyException(Function)}, the return value might be null for the
     * specified locale. Otherwise, if it is created via
     * {@link #EconomyException(String)}, then it will always return the specified
     * non-null message without the specified locale affecting it.
     */
    @UnknownNullability
    public String getMessage(@NotNull Locale locale) {
        return this.messageSupplier.apply(locale);
    }

}