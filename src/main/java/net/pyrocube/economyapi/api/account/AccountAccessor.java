package net.pyrocube.economyapi.api.account;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Provides access to {@link Account}.
 */
public abstract class AccountAccessor {
    private UUID uniqueId;

    protected AccountAccessor() {
    }

    /**
     * Specifies the {@link UUID unique id} of the account owner
     *
     * @param uniqueId account owner uuid
     * @return this instance for chaining
     */
    @NotNull
    public AccountAccessor withUniqueId(@NotNull UUID uniqueId) {
        this.uniqueId = Objects.requireNonNull(uniqueId, "uniqueId");
        return this;
    }

    /**
     * Gets or creates the {@link AccountAccessor account} needed.
     *
     * @return a resulting account
     */
    @NotNull
    public CompletableFuture<Account> get() {
        return this.getOrCreate(uniqueId);
    }

    @NotNull
    protected abstract CompletableFuture<Account> getOrCreate(@NotNull UUID context);
}
