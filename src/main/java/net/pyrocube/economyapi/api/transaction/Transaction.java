package net.pyrocube.economyapi.api.transaction;

import net.pyrocube.economyapi.api.currency.Currency;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.Optional;

public class Transaction {

    /**
     * Creates a new {@link Transaction.Builder}
     *
     * @return new builder
     */
    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private final BigDecimal amount;
    private final String currencyId;
    private final Instant timestamp;
    private final TransactionType type;
    private final Optional<String> reason;

    /**
     * Creates a new account transaction object.
     *
     * @param currencyId the currency's {@link Currency#getIdentifier() identifier} the transaction was made into
     * @param timestamp  the time at which this transaction occurred. specifying null would mean "now"
     * @param type       the transaction type
     * @param reason     optional specification of a string message reason
     * @param amount     the amount which to deposit/withdraw
     */
    public Transaction(
            @NotNull String currencyId,
            @Nullable Temporal timestamp,
            @NotNull TransactionType type,
            @Nullable String reason,
            @NotNull final BigDecimal amount
    ) {
        this.currencyId = Objects.requireNonNull(currencyId, "currencyID");
        this.type = Objects.requireNonNull(type, "transactionType");
        this.reason = Optional.ofNullable(reason);
        this.amount = amount;
        this.timestamp = timestamp == null
                ? Instant.now()
                : (timestamp instanceof Instant ? (Instant) timestamp : Instant.from(timestamp));
    }

    /**
     * Get the transaction amount.
     *
     * @return transaction amount
     */
    @NotNull
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Returns the transaction type.
     *
     * @return transaction type
     */
    @NotNull
    public TransactionType getType() {
        return type;
    }

    /**
     * Returns the {@link net.pyrocube.economyapi.api.currency.Currency}'s {@link Currency#getIdentifier() identifier } with
     * which the transaction was made.
     *
     * <p>A {@code Currency} object is retrievable via {@link net.pyrocube.economyapi.api.EconomyProvider#findCurrency(String)} if
     * you need such.
     *
     * @return The currency {@link Currency#getIdentifier() identifier}.
     */
    @NotNull
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the time at which this {@code Transaction} was made.
     *
     * @return timestamp
     */
    @NotNull
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the reason message of why this transaction happened.
     *
     * @return reason string or empty optional if not present
     */
    @NotNull
    public Optional<String> getReason() {
        return reason;
    }

    /**
     * Represents a builder of {@link Transaction}
     *
     * @author MrIvanPlays, lokka30
     */
    public static class Builder {

        private String currencyId;
        private Temporal timestamp;
        private TransactionType type;
        private String reason;
        private BigDecimal amount;

        public Builder() {
        }

        /**
         * Creates a new {@link Builder} out of the specified {@code other}
         *
         * @param other the other builder to create a new builder from
         */
        public Builder(@NotNull Builder other) {
            this.currencyId = other.currencyId;
            this.timestamp = other.timestamp;
            this.type = other.type;
            this.reason = other.reason;
            this.amount = other.amount;
        }

        /**
         * Creates a copy of this {@code Builder}
         *
         * @return builder copy
         */
        @NotNull
        public Builder copy() {
            return new Builder(this);
        }

        /**
         * Specify the {@link Currency} the transaction was made in.
         *
         * @param currency currency
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withCurrency(@NotNull Currency currency) {
            this.currencyId = Objects.requireNonNull(currency, "currency").getIdentifier();
            return this;
        }

        /**
         * Specify the {@link Currency#getIdentifier() identifier} of a {@link Currency} the transaction was made in.
         *
         * @param currencyId currency id
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withCurrencyId(@NotNull String currencyId) {
            this.currencyId = Objects.requireNonNull(currencyId, "currencyId");
            return this;
        }

        /**
         * Specify the time when the transaction got triggered.
         *
         * @param timestamp timestamp
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withTimestamp(@Nullable Temporal timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Specify the {@link TransactionType} {@code transactionType} of the transaction.
         *
         * @param type transaction type
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withType(@NotNull TransactionType type) {
            this.type = Objects.requireNonNull(type, "type");
            return this;
        }

        /**
         * Specify a reason for this transaction.
         *
         * @param reason reason
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withReason(@Nullable String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Specify the amount this transaction is going to either {@link TransactionType#WITHDRAWAL} or
         * {@link TransactionType#DEPOSIT}
         *
         * @param amount transaction amount
         * @return this instance for chaining
         */
        @Contract("_ -> this")
        public Builder withAmount(@NotNull BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Builds the specified stuff into a new {@link Transaction}
         *
         * @return transaction object
         */
        @NotNull
        public Transaction build() {
            Objects.requireNonNull(currencyId, "currencyID");
            Objects.requireNonNull(type, "transactionType");
            Objects.requireNonNull(amount, "transactionAmount");
            return new Transaction(currencyId,
                    timestamp,
                    type,
                    reason,
                    amount
            );
        }

    }

}
