package com.example.TelegramBookingBot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class KeyboardFactory {
    static final LocalDate first = LocalDate.now();
    static final LocalDate second = first.plusDays(1);
    static final LocalDate third = first.plusDays(2);
    static final LocalDate fourth = first.plusDays(3);
    static final LocalDate fiveth = first.plusDays(4);
    static final LocalDate sixth = first.plusDays(5);
    static final LocalDate seventh = first.plusDays(6);

    static final LocalTime hour1 = LocalTime.of(11,30,0);
    static final LocalTime hour2 = LocalTime.of(16,0,0);
    static final LocalTime hour3 = LocalTime.of(20,20,0);

    public static final String film1 = "Bad Boys 4";
    public static final String film2 = "Despicable 4";
    public static final String film3 = "Maverick";

    public static ReplyKeyboard getSelectedDate() {
        KeyboardRow row = new KeyboardRow();
        row.add(String.valueOf(first));
        row.add(String.valueOf(second));
        row.add(String.valueOf(third));
        row.add(String.valueOf(fourth));
        row.add(String.valueOf(fiveth));
        row.add(String.valueOf(sixth));
        row.add(String.valueOf(seventh));
        return  new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getSelectedTime() {
        KeyboardRow row = new KeyboardRow();
        row.add(String.valueOf(hour1));
        row.add(String.valueOf(hour2));
        row.add(String.valueOf(hour3));
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getSelectedMovie () {
        KeyboardRow row = new KeyboardRow();
        row.add(film1);
        row.add(film2);
        row.add(film3);
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getYesOrNo() {
        KeyboardRow row = new KeyboardRow();
        row.add("Yes");
        row.add("No");
        return new ReplyKeyboardMarkup(List.of(row));
    }
}
