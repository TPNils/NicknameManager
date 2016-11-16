package be.spyproof.nickmanager.model;


import java.util.*;

/**
 * Created by Spyproof
 */
public class ImmutablePlayerData
{
    //TODO last seen
    protected final UUID uuid;
    protected final String name;
    protected final int tokensRemaining;
    protected final long lastChanged;
    protected final boolean readRules;
    protected final boolean acceptedRules;

    protected final String nickname;
    protected final List<String> pastNicknames;

    public static ImmutablePlayerData of(PlayerData playerData)
    {
        return new ImmutablePlayerData(
                playerData.getUuid(),
                playerData.getName(),
                playerData.getTokensRemaining(),
                playerData.getLastChanged(),
                playerData.readRules(),
                playerData.hasAcceptedRules(),
                playerData.getNickname().orElse(null),
                playerData.getPastNicknames()
        );
    }

    private ImmutablePlayerData(UUID uuid, String name, int tokensRemaining, long lastChanged, boolean readRules, boolean acceptedRules, String nickname, List<String> pastNicknames)
    {
        this.uuid = uuid;
        this.name = name;
        this.tokensRemaining = tokensRemaining;
        this.lastChanged = lastChanged;
        this.readRules = readRules;
        this.acceptedRules = acceptedRules;
        this.nickname = nickname;
        this.pastNicknames = Collections.unmodifiableList(pastNicknames);
    }

    /**
     * @return The name of the player
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The Unique ID of the player
     */
    public UUID getUuid()
    {
        return uuid;
    }

    /**
     * @return The amount of tokens the player owns
     */
    public int getTokensRemaining()
    {
        return this.tokensRemaining;
    }

    /**
     * @return If the player has a nickname, return the nickname of the player
     *         Otherwise return an empty object
     */
    public Optional<String> getNickname()
    {
        return Optional.ofNullable(this.nickname);
    }

    /**
     * @return If the player has accepted the rules
     */
    public boolean hasAcceptedRules()
    {
        return acceptedRules;
    }

    /**
     * @return The timestamp the last when the player changed his nickname himself
     */
    public long getLastChanged()
    {
        return lastChanged;
    }

    /**
     * @return All of the last nicknames the player had owned
     */
    public List<String> getPastNicknames()
    {
        return pastNicknames;
    }

    /**
     * @return If the player has read the rules
     */
    public boolean readRules()
    {
        return readRules;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutablePlayerData that = (ImmutablePlayerData) o;
        return tokensRemaining == that.tokensRemaining &&
                acceptedRules == that.acceptedRules &&
                lastChanged == that.lastChanged &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(pastNicknames, that.pastNicknames);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, name, tokensRemaining, acceptedRules, lastChanged, nickname, pastNicknames);
    }

    @Override
    public String toString()
    {
        return "PlayerData{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", tokensRemaining=" + tokensRemaining +
                ", lastChanged=" + lastChanged +
                ", readRules=" + readRules +
                ", acceptedRules=" + acceptedRules +
                ", nickname='" + nickname + '\'' +
                ", pastNicknames=" + pastNicknames +
                '}';
    }
}
