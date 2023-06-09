package de.sirvierl0ffel.viswiz.util;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewUtil {

    public static SpannableString makeSpannableString(String input) {
        return makeSpannableString(input, false);
    }

    public static SpannableString makeSpannableString(String input, boolean applyMargins) {
        input = input.replace("\t", "  ");
        Pattern formatPattern = Pattern.compile("\\\\\\w\\{[\\w:]+\\}");
        Matcher formatMatcher = formatPattern.matcher(input);
        StringBuilder builder = new StringBuilder(input.length());
        int last = 0;
        Map<int[], Character> styles = new HashMap<>();
        while (formatMatcher.find()) {
            builder.append(input, last, formatMatcher.start());
            char style = input.charAt(formatMatcher.start() + 1);
            int[] range = {builder.length(), 0};
            builder.append(input, formatMatcher.start() + 3, formatMatcher.end() - 1);
            range[1] = builder.length();
            if (range[0] != range[1]) styles.put(range, style);
            last = formatMatcher.end();
        }
        builder.append(input, last, input.length());

        Map<int[], LeadingMarginSpan> margins = new HashMap<>();
        if (applyMargins) {
            String unformatted = builder.toString();
            builder = new StringBuilder(unformatted.length());
            for (String line : unformatted.split("(\r\n)|(\n\r)|(\r)|(\n)", -1)) {
                Matcher indentMatcher = Pattern.compile("^[\t ]+").matcher(line);
                int indentAmount = 0;
                String group = "";
                if (indentMatcher.find()) {
                    group = indentMatcher.group();
                    for (char c : group.toCharArray()) indentAmount += c == ' ' ? 1 : 4;
                }
                int[] range = {builder.length(), 0};
                for (int i = 0; i < group.length(); i++) builder.append('\u2062'); // oh hell no
                if (!line.isEmpty()) builder.append(line, group.length(), line.length());
                builder.append(System.lineSeparator());
                range[1] = builder.length();
                if (indentAmount > 0 && range[0] != range[1]) margins.put(range, new LeadingMarginSpan.Standard(8 * indentAmount));
            }
        }

        SpannableString str = new SpannableString(builder.toString());

        margins.forEach((range, span) -> str.setSpan(span, range[0], range[1], 0));

        for (Map.Entry<int[], Character> entry : styles.entrySet()) {
            int[] range = entry.getKey();
            char style = entry.getValue();
            switch (style) {
                case 'h':
                    // str.setSpan(new StyleSpan(Typeface.BOLD), range[0], range[1], 0);
                    str.setSpan(new ForegroundColorSpan(0xff000000), range[0], range[1], 0);
                    str.setSpan(new RelativeSizeSpan(1.2f), range[0], range[1], 0);
                    break;
                case 'k':
                    str.setSpan(new StyleSpan(Typeface.BOLD), range[0], range[1], 0);
                    str.setSpan(new ForegroundColorSpan(0xffc00080), range[0], range[1], 0);
                    break;
                case 'a':
                    str.setSpan(new ForegroundColorSpan(0xff006080), range[0], range[1], 0);
                    break;
                case 'v':
                    str.setSpan(new StyleSpan(Typeface.BOLD), range[0], range[1], 0);
                    str.setSpan(new ForegroundColorSpan(0xff102060), range[0], range[1], 0);
                    break;
                default:
                    str.setSpan(new ForegroundColorSpan(0xffff0000), range[0], range[1], 0);
            }
        }
        return str;
    }

}
