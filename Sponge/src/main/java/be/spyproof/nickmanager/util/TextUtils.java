package be.spyproof.nickmanager.util;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextUtils {
  private static final String NEW_LINE = "\n";

  /**
   * Split a message with every new line
   * @param text to be split
   * @return a list of texts
   */
  public static List<Text> splitLines(Text text) {
    return splitLines(text, null, null);
  }

  private static List<Text> splitLines(Text text, TextColor parentColor, TextStyle parentStyle) {
    List<Text.Builder> splitBuilders = new ArrayList<>();

    TextColor color = processColor(text.getColor(), parentColor);
    TextStyle style = processStyle(text.getStyle(), parentStyle);
    Collection<String> plainTexts = separateNewLines(text.toPlainSingle());

    for (String plainText : plainTexts) {
      splitBuilders.add(Text.builder(plainText).color(color).style(style));
    }

    Text.Builder lastBuilder = splitBuilders.get(splitBuilders.size()-1);
    for (Text child : text.getChildren()) {
      lastBuilder.append(splitLines(child, color, style));
    }

    List<Text> texts = new ArrayList<>();

    for (Text.Builder builder : splitBuilders) {
      texts.add(builder.build());
    }

    return texts;
  }

  private static TextColor processColor(TextColor selfColor, TextColor parentColor) {
    return selfColor == null ? parentColor : selfColor;
  }

  private static TextStyle processStyle(TextStyle selfStyle, TextStyle parentStyle) {
    if (selfStyle == null) {
      return parentStyle;
    }

    if (parentStyle == null) {
      return null;
    }

    return new TextStyle(
      selfStyle.isBold().orElse(parentStyle.isBold().orElse(null)),
      selfStyle.isItalic().orElse(parentStyle.isItalic().orElse(null)),
      selfStyle.hasUnderline().orElse(parentStyle.hasUnderline().orElse(null)),
      selfStyle.hasStrikethrough().orElse(parentStyle.hasStrikethrough().orElse(null)),
      selfStyle.isObfuscated().orElse(parentStyle.isObfuscated().orElse(null))
    );
  }

  private static Collection<String> separateNewLines(String text) {
    List<String> parts = new ArrayList<>();
    int nextNewLine;
    while (text.contains(NEW_LINE)) {
      nextNewLine = text.indexOf('\n');
      parts.add(text.substring(0, nextNewLine));
      if (text.length() > nextNewLine+1) {
        text = text.substring(nextNewLine+1);
      } else {
        text = "";
      }
    }

    if (text.length() > 0) {
      parts.add(text);
    }

    return parts;
  }
}
