package net.pyrocube.economyapi.api;

import net.pyrocube.economyapi.api.account.Account;
import net.pyrocube.economyapi.api.account.AccountAccessor;
import net.pyrocube.economyapi.api.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Implementors providing and managing economy data create a class which implements this
 * interface to be registered in Treasury's {@link me.lokka30.treasury.api.common.service
 * Services API}.
 *
 * @author lokka30, Jikoo, MrIvanPlays, NoahvdAa, creatorfromhell
 * @since v1.0.0
 */
public interface EconomyProvider {

    /**
     * Returns the {@link AccountAccessor} of this {@code EconomyProvider}.
     * <p>The account accessor serves the purpose of creating or retrieving existing accounts.
     * Please check its documentation for more in-depth explanation.
     *
     * @return account accessor
     * @see AccountAccessor
     * @since 1.0.0
     */
    @NotNull AccountAccessor accountAccessor();

    /**
     * Request all {@link UUID UUIDs} with associated {@link Account Accounts}.
     *
     * @return a collection of account uuids
     */
    @NotNull CompletableFuture<Collection<UUID>> retrieveAccountIds();

    /**
     * Get the primary or main {@link Currency} of the economy.
     *
     * @return the primary currency
     */
    @NotNull Currency getPrimaryCurrency();

    /**
     * Used to find a currency based on a specific identifier.
     *
     * @param identifier The {@link Currency#getIdentifier()} of the {@link Currency} we are searching for.
     * @return The {@link Optional} containing the search result. This will contain the
     * resulting {@link Currency} if it exists, otherwise it will return {@link Optional#empty()}.
     */
    @NotNull Optional<Currency> findCurrency(@NotNull String identifier);

    /**
     * Used to find a currency based on its display name.
     * <p>We <b>strongly</b> encourage economy implementors to override this and provide a better
     * implementation.
     *
     * @param displayName the {@link Currency#getDisplayName(BigDecimal, Locale)} of the
     *                    {@link Currency} we are searching for.
     * @param value       whether we're going to compare against a singular or a plural display name of a
     *                    currency
     * @param locale      a locale
     * @return the {@link Optional} containing the search result. This will contain the resulting
     * {@link Currency} if it exists, otherwise it will return {@link Optional#empty()}
     */
    @NotNull
    default Optional<Currency> findCurrencyByDisplayName(
            @NotNull String displayName, @NotNull BigDecimal value, @Nullable Locale locale
    ) {
        for (Currency currency : getCurrencies()) {
            if (currency.getDisplayName(value, locale).equalsIgnoreCase(displayName)) {
                return Optional.of(currency);
            }
        }
        return Optional.empty();
    }

    /**
     * Used to get a set of every  {@link Currency} object for the server.
     *
     * @return A set of every {@link Currency} object that is available for the server.
     */
    @NotNull Set<Currency> getCurrencies();

    /**
     * Get the String identifier of the primary or main {@link Currency} of the economy.
     *
     * @return the String identifier identifying the primary currency
     */
    @NotNull
    default String getPrimaryCurrencyId() {
        return getPrimaryCurrency().getIdentifier();
    }

    /**
     * Used to register a currency with the {@link EconomyProvider} to be utilized by
     * other plugins.
     *
     * @param currency The currency to register with the {@link EconomyProvider}.
     * @return {@code true} if the currency was successfully registered, {@code false} otherwise.
     */
    @NotNull CompletableFuture<Boolean> registerCurrency(@NotNull Currency currency);

    /**
     * Used to un-register a currency with the {@link EconomyProvider}.
     *
     * @param currency The currency to un-register with the {@link EconomyProvider}.
     * @return {@code true} if the currency was successfully unregistered, {@code false} otherwise or if the currency
     * is already not registered.
     */
    @NotNull CompletableFuture<Boolean> unregisterCurrency(@NotNull Currency currency);

}