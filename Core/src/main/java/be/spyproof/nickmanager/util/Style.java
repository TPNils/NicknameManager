package be.spyproof.nickmanager.util;

/**
 * Created by Spyproof on 29/10/2016.
 * <p>
 * Refers to the default minecraft chat styles and their matching codes
 */
public enum Style {
  OBFUSCATED('k'),
  BOLD('l'),
  STRIKETHOUGH('m'),
  UNDERLINE('n'),
  ITALIC('o'),
  RESET('r');

  private char colourChar;

  Style(char colourChar) {
    this.colourChar = colourChar;
  }

  /**
   * @return The character associated with the style
   */
  public char getColourChar() {
    return colourChar;
  }

  /**
   * @return The default chat string that will be used to identify a style
   */
  @Override
  public String toString() {
    return new String(new char[]{'&', this.colourChar});
  }
}
