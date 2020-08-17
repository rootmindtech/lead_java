package com.rootmind.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class DateToWordsConversion {

	
	private static final String[] specialNamesMonthDay = {
        "",
        " first",
        " second",
        " third",
        " fourth",
        " fifth",
        " sixth",
        " seventh",
        " eighth",
        " nineth",
        " tenth",
        " eleventh",
        " twelveth",
        " thirteenth",
        " fourteenth",
        " fifteenth",
        " sixteenth",
        " seventeenth",
        " eighteenth",
        " nineteenth",
        " twenth",
        " twenty first",
        " twenty second",
        " twenty third",
        " twenty fourth",
        " twenty fifth",
        " twenty sixth",
        " twenty seventh",
        " twenty eighth",
        " twenty nineth",
        " thirty",
        " thirty first"

    };
    private static final String[] specialNames = {
        "",
        " thousand"

    };

    private static final String[] tensNames = {
        "",
        " ten",
        " twenty",
        " thirty",
        " forty",
        " fifty",
        " sixty",
        " seventy",
        " eighty",
        " ninety"
    };

    private static final String[] numNames = {
        "",
        " one",
        " two",
        " three",
        " four",
        " five",
        " six",
        " seven",
        " eight",
        " nine",
        " ten",
        " eleven",
        " twelve",
        " thirteen",
        " fourteen",
        " fifteen",
        " sixteen",
        " seventeen",
        " eighteen",
        " nineteen"
    };

    public String dateToWords(String inputDate) {
        //System.out.println("Pleaser date in dd/mm/yyyy format");
        //Scanner in = new Scanner(System.in);
        //String date = in.next();
    	String  dateToWords=null;
    	
        if (validateDate(inputDate)) {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            ParsePosition parsePosition = new ParsePosition(0);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormatter.parse(inputDate, new ParsePosition(0)));
            DateFormat format2 = new SimpleDateFormat("MMMMM ");

            int day = cal.get(Calendar.DATE);
            String strDateToWords = getMonthDay(day);
            strDateToWords += " "
            		+ "" + format2.format(cal.getTime());

            int year = cal.get(Calendar.YEAR);
            strDateToWords += "" + convert(year);
            dateToWords=strDateToWords.toUpperCase();
            System.out.println(strDateToWords.toUpperCase());
        } else {
        	dateToWords=inputDate;
            System.out.println("Wrong! Please enter date in dd/mm/yyyy format");
        }
        
        return dateToWords;
    }

    public String getMonthDay(int day) {
        return specialNamesMonthDay[day];
    }

    private String convertLessThanOneThousand(int number) {
        String current;

        if (number % 100 < 20) {
            current = numNames[number % 100];
            number /= 100;
        } else {
            current = numNames[number % 10];
            number /= 10;

            current = tensNames[number % 10] + current;
            number /= 10;
        }
        if (number == 0) {
            return current;
        }
        return numNames[number] + " hundred" + current;
    }

    public String convert(int number) {

        if (number == 0) {
            return "zero";
        }

        String prefix = "";

        String current = "";
        int place = 0;

        if (number >= 1 && number < 2000) {
            do {
                int n = number % 100;
                if (n != 0) {
                    String s = convertLessThanOneThousand(n);
                    current = s + current;
                }
                place++;
                number /= 100;
            } while (number > 0);
        } else {
            do {
                int n = number % 1000;
                if (n != 0) {
                    String s = convertLessThanOneThousand(n);
                    current = s + specialNames[place] + current;
                }
                place++;
                number /= 1000;
            } while (number > 0);
        }

        return (prefix + current).trim();
    }

    public boolean validateDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

   /* public static void main(String[] args) {
        new DateToWordsConversion().dateToWords("29/02/2015");

    }*/
}
