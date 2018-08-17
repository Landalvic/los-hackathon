package fr.insee.bidbo.utils;

import java.util.regex.Pattern;

public abstract class StringBuilderUtils {

    public static void replaceAll(StringBuilder stringBuilder, String regex, String replacement) {
	String temp = Pattern.compile(regex).matcher(stringBuilder).replaceAll(replacement);
	stringBuilder.setLength(0);
	stringBuilder.append(temp);
    }
}
