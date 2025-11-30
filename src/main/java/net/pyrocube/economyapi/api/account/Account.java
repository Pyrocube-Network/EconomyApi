package net.pyrocube.economyapi.api.account;

import net.pyrocube.economyapi.api.currency.Currency;
import net.pyrocube.economyapi.api.transaction.Transaction;
import net.pyrocube.economyapi.api.transaction.TransactionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * An Account is something that holds a balance and is associated with
 * an identifier.
 */
public interface Account {

    /**
     * Returns the identifier of this {@link Account}.
     *
     * @return the identifier of the account.
     */
    @NotNull UUID getIdentifier();

    /**
     * Sets the identifier for this {@link Account}.
     * <br/>
     * <b>TIP:</b> The best identifier it's the player UUID.
     *
     * @param identifier the new identifier for this account.
     * @return whether the name was changed
     */
    @NotNull CompletableFuture<UUID> setIdentifier(@NotNull UUID identifier);

    /**
     * Returns the name of this {@link Account}, if specified. Otherwise, an empty
     * {@link Optional} is returned.
     *
     * @return an optional, fulfilled with either a name or an empty optional.
     */
    @NotNull Optional<String> getName();

    /**
     * Sets a new name for this {@link Account}, which may be null.
     *
     * @param name the new name for this account.
     * @return whether the name was changed
     */
    @NotNull CompletableFuture<Boolean> setName(@Nullable String name);

    /**
     * Request the balance of the {@code Account}.
     *
     * @param currency the {@link Currency} of the balance being requested
     * @return a {@link BigDecimal} value representation of the balance
     */
    @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency);

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * @param amount   the amount the balance will be reduced by
     * @param currency the {@link Currency} of the balance being modified
     * @return see {@link #doTransaction(Transaction)}
     * @see Account#doTransaction(Transaction)
     */
    @NotNull
    default CompletableFuture<BigDecimal> withdrawBalance(
            @NotNull BigDecimal amount, @NotNull Currency currency
    ) {
        return withdrawBalance(amount, currency, null);
    }

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * @param amount   the amount the balance will be reduced byion
     * @param currency the {@link Currency} of the balance being modified
     * @param reason   the reason of why the balance is modified
     * @return see {@link #doTransaction(Transaction)}
     * @see Account#doTransaction(Transaction)
     */
    @NotNull
    default CompletableFuture<BigDecimal> withdrawBalance(
            @NotNull BigDecimal amount,
            @NotNull Currency currency,
            @Nullable String reason
    ) {
        return doTransaction(Transaction
                .newBuilder()
                .withCurrency(currency)
                .withReason(reason)
                .withAmount(amount)
                .withType(TransactionType.WITHDRAWAL)
                .build());
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * @param amount   the amount the balance will be increased by
     * @param currency the {@link Currency} of the balance being modified
     * @return see {@link #doTransaction(Transaction)}
     * @see Account#doTransaction(Transaction)
     */
    @NotNull
    default CompletableFuture<BigDecimal> depositBalance(
            @NotNull BigDecimal amount,
            @NotNull Currency currency
    ) {
        return depositBalance(
                amount,
                currency,
                null
        );
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * @param amount   the amount the balance will be increased by
     * @param currency the {@link Currency} of the balance being modified
     * @param reason   the reason of why the balance is modified
     * @return see {@link #doTransaction(Transaction)}
     * @see Account#doTransaction(Transaction)
     */
    @NotNull
    default CompletableFuture<BigDecimal> depositBalance(
            @NotNull BigDecimal amount,
            @NotNull Currency currency,
            @Nullable String reason
    ) {
        return doTransaction(Transaction
                .newBuilder()
                .withCurrency(currency)
                .withAmount(amount)
                .withReason(reason)
                .withType(TransactionType.DEPOSIT)
                .build());
    }

    /**
     * Does a {@link Transaction} on this account.
     *
     * @param economyTransaction the transaction that should be done
     * @return a {@link BigDecimal} value representing the new balance resulting from the transaction
     */
    @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull Transaction economyTransaction);

    /**
     * Reset the {@code Account} balance to its starting amount.
     *
     * @param currency the {@link Currency} of the balance being reset
     * @return see {@link #doTransaction(Transaction)}
     * @see #resetBalance(Currency, String)
     */
    @NotNull
    default CompletableFuture<BigDecimal> resetBalance(
            @NotNull Currency currency
    ) {
        return resetBalance(currency, null);
    }

    /**
     * Reset the {@code Account} balance to its starting amount.
     *
     * @param currency the {@link Currency} of the balance being reset
     * @param reason   the reset reason
     * @return see {@link #doTransaction(Transaction)}
     */
    @NotNull
    default CompletableFuture<BigDecimal> resetBalance(
            @NotNull Currency currency,
            @Nullable String reason
    ) {
        Objects.requireNonNull(currency, "currency");

        return doTransaction(Transaction
                .newBuilder()
                .withCurrency(currency)
                .withAmount(currency.getStartingBalance(this))
                .withReason(reason)
                .withType(TransactionType.SET)
                .build());
    }

    /**
     * Delete data stored for the {@code Account}.
     *
     * <p>Providers should consider storing backups of deleted accounts.
     *
     * @return whether the account was deleted
     */
    @NotNull CompletableFuture<Boolean> deleteAccount();

    /**
     * Returns the {@link Currency#getIdentifier()  Currencies} this {@code Account} holds balance for.
     *
     * @return a collection of held currencies in the form of currency ids
     */
    @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies();

}
