package fr.insee.bidbo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

public abstract class DateUtils {

    public static final String DATE_UNDEFINED = "date_undefined";
    public static final String DATE_NOW = "now";
    public static final SimpleDateFormat FORMAT_SECONDE = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final SimpleDateFormat FORMAT_JOUR = new SimpleDateFormat("dd/MM/yyyy");

    public static Date applyFormat(Date date, SimpleDateFormat dateFormat) throws ParseException {
	return dateFormat.parse(dateFormat.format(date));
    }

    public static String[] tabToString(Date[] dates, SimpleDateFormat dateFormat) {
	String[] datesString = new String[dates.length];
	for (int i = 0; i < dates.length; i++) {
	    datesString[i] = dateFormat.format(dates[i]);
	}
	return datesString;
    }

    public static Date[] tabToDate(String[] datesString, SimpleDateFormat dateFormat) throws ParseException {
	Date[] datesEmbargo = new Date[datesString.length];
	for (int i = 0; i < datesString.length; i++) {
	    datesEmbargo[i] = dateFormat.parse(datesString[i]);
	}
	return datesEmbargo;
    }

    public static boolean dateValide(String date, SimpleDateFormat dateFormat) {
	try {
	    Date d = dateFormat.parse(date);
	    String t = dateFormat.format(d);
	    return StringUtils.equals(t, date.substring(0, t.length()));
	} catch (Exception e) {
	    return false;
	}

    }

    public static boolean datesValides(Date debut, Date fin) {
	if (debut == null || fin == null) {
	    return false;
	} else {
	    if (debut.after(fin)) {
		return false;
	    } else {
		DateTime dateTimeDebut = new DateTime(debut);
		DateTime dateTimeFin = new DateTime(fin);
		if (dateTimeDebut.equals(dateTimeFin)) {
		    if (!estDebutDeMois(dateTimeDebut) && !estFinDeMois(dateTimeDebut)) {
			return false;
		    }
		} else {
		    if (!estDebutDeMois(dateTimeDebut) || !estFinDeMois(dateTimeFin)) {
			return false;
		    }
		}
	    }
	    return true;
	}
    }

    public static Date max(List<Date> dates) {
	return max(dates.toArray(new Date[dates.size()]));
    }

    public static Date max(Date... dates) {
	Date dateMax = null;
	for (Date date : dates) {
	    if (dateMax == null || (date != null && date.after(dateMax))) {
		dateMax = date;
	    }
	}
	return dateMax;
    }

    public static void change(Date date, int type, int nbr) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(type, nbr);
	date.setTime(cal.getTimeInMillis());
    }

    public static Date add(Date date, int type, int nbr) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(type, nbr);
	return cal.getTime();
    }

    public static int get(Date date, int type) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	return cal.get(type);
    }

    public static void set(Date date, int type, int nbr) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(type, nbr - cal.get(type));
	date.setTime(cal.getTimeInMillis());
    }

    public static String nomDuMois(int numero) {
	String[] nomMois = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre",
		"Octobre", "Novembre", "Décembre" };
	return nomMois[numero - 1];
    }

    private static boolean estDebutDeMois(DateTime date) {
	return date.dayOfMonth().withMinimumValue().equals(date);
    }

    private static boolean estFinDeMois(DateTime date) {
	return date.dayOfMonth().withMaximumValue().equals(date);
    }

    /**
     * Retourne la date du jour sans les heures ni minutes
     */
    public static Date maintenantSansHeureMinute() {
	Instant instant = new Instant(DateTime.now());
	return new LocalDate(instant).toDate();
    }

    public static Date parseDate(String date, SimpleDateFormat formatter) throws ParseException {
	return formatter.parse(date);
    }

    public static Date parseDate(String date, String format) throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat(format);
	return formatter.parse(date);
    }

    public static boolean equals(Date date1, Date date2) {
	if (date1 == null || date2 == null) {
	    return false;
	}
	return date1.equals(date2);
    }
}
