package be.spyproof.nickmanager.model;


import java.util.*;

/**
 * Created by Spyproof
 */
public class NicknameData
{
    //TODO last seen
    protected final UUID uuid;
    protected String name;
    protected int tokensRemaining;
    protected long lastChanged;
    protected boolean readRules;
    protected boolean acceptedRules;

    protected String nickname;
    protected List<String> pastNicknames = new ArrayList<>();

    public static NicknameData of(ImmutablePlayerData immutablePlayerData)
    {
        NicknameData nicknameData = new NicknameData(immutablePlayerData.getName(), immutablePlayerData.getUuid());

        nicknameData.setTokensRemaining(immutablePlayerData.getTokensRemaining());
        nicknameData.setLastChanged(immutablePlayerData.getLastChanged());
        nicknameData.addPastNickname(immutablePlayerData.getPastNicknames());
        nicknameData.setReadRules(immutablePlayerData.readRules());
        if (immutablePlayerData.getNickname().isPresent())
            nicknameData.setNickname(immutablePlayerData.getNickname().get());

        return nicknameData;
    }

    public NicknameData(String name, UUID uuid)
    {
        this.name = name;
        this.uuid = uuid;
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

    /**
     * Set a new name to this player
     * @param playerName The new name of the player
     */
    public void setName(String playerName)
    {
        this.name = playerName;
    }

    /**
     * Set the remaining tokens of this player
     * @param tokensRemaining The new amount of tokens the player owns the player
     */
    public void setTokensRemaining(int tokensRemaining)
    {
        this.tokensRemaining = tokensRemaining;
    }

    /**
     * Set if the player has accepted the rules or not
     * @param acceptedRules True if the player has accepted the rules. False if not.
     */
    public void setAcceptedRules(boolean acceptedRules)
    {
        this.acceptedRules = acceptedRules;
    }

    /**
     * Set the new nickname of the player. If the player already had a nickname
     * It will be added to the past nicknames
     * @param nickname The new nickname of the player
     */
    public void setNickname(String nickname)
    {
        if (this.nickname != null && !this.nickname.equals(nickname))
            this.pastNicknames.add(this.nickname);
        this.nickname = nickname;
    }

    /**
     * Set the last time the player has changed their nickname to now
     */
    public void setLastChanged()
    {
        setLastChanged(System.currentTimeMillis());
    }

    /**
     * Set the last time the player has changed their nickname to the specified timestamp
     * @param timeStamp The timestamp when the player has changed their nickname last time
     */
    public void setLastChanged(long timeStamp)
    {
        this.lastChanged = timeStamp;
    }

    /**
     * Add additional past nicknames to the player.
     *
     * NOTE: setNickname(String) will add active nickname automatically if one is present
     *
     * @param nicknames Add these nicknames to the past nicknames
     */
    public void addPastNickname(String... nicknames)
    {
        Collections.addAll(this.pastNicknames, nicknames);
    }

    /**
     * Add additional past nicknames to the player.
     *
     * NOTE: setNickname(String) will add active nickname automatically if one is present
     *
     * @param nicknames Add these nicknames to the past nicknames
     */
    public void addPastNickname(Collection<String> nicknames)
    {
        this.pastNicknames.addAll(nicknames);
    }

    /**
     * Set if the player has read the rules or not
     * @param readRules True if the player has read the rules. False if not.
     */
    public void setReadRules(boolean readRules)
    {
        this.readRules = readRules;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NicknameData that = (NicknameData) o;
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
        return "NicknameData{" +
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
