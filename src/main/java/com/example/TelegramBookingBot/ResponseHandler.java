package com.example.TelegramBookingBot;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static com.example.TelegramBookingBot.Constants.START_TEXT;
import static com.example.TelegramBookingBot.KeyboardFactory.*;

public class ResponseHandler {
    private final SilentSender sender;
    private final Map<Long,UserState> chatStates;
     final LocalDate[] array = {first,second,third,fourth,fiveth,sixth,seventh};
     final LocalTime[] arrTime = {hour1,hour2,hour3};
     final String[] films = {film1,film2,film3};

    public ResponseHandler(SilentSender sender, DBContext db) {
        this.sender=sender;
        chatStates=db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);
        chatStates.put(chatId,UserState.AWAITING_NAME);
    }

    public void replyToButtons(long chatId, Message message) {

        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case AWAITING_NAME -> replyToName(chatId,message);
            case CHOOSING_DAY -> replyToDaySelection(chatId,message);
            case CHOOSING_TIME -> replyToTimeSelection(chatId,message);
            case CHOOSING_FILM -> replyToFilmSelection(chatId,message);
            case AWAITING_CONFIRMATION -> replyToOrder(chatId,message);
            default -> unexpectedMessage(chatId);
        }
    }

    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard YesOrNo, UserState awaitingReorder) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(YesOrNo);
        sender.execute(sendMessage);
        chatStates.put(chatId,awaitingReorder);
    }

    private void replyToName(long chatId, Message message) {
        promptWithKeyboardForState(chatId, "Hello " + message.getText() + ". When would you like to watch a film?",
                getSelectedDate(),
                UserState.CHOOSING_DAY);
    }

    private void replyToDaySelection(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String messageText = message.getText();
        boolean isValidDate = false;

        for (LocalDate date : array) {
            if (date.toString().equals(messageText)) {
                isValidDate = true;
                break;
            }
        }

        if (isValidDate) {
            promptWithKeyboardForState(chatId, "You selected that date " +
                            messageText + ".\nNow choose the showTime.", getSelectedTime(),
                    UserState.CHOOSING_TIME);
        } else {
            sendMessage.setText("Please select a valid date.");
            sender.execute(sendMessage);
        }
    }

    private void replyToTimeSelection(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String messageText = message.getText();
        boolean isValidTime = false;

        for (LocalTime time : arrTime) {
            if (time.toString().equals(messageText)) {
                isValidTime = true;
                break;
            }
        }

        if (isValidTime) {
            promptWithKeyboardForState(chatId, "You selected that time " +
                            messageText + ".\nNow choose the film.", getSelectedMovie(),
                    UserState.CHOOSING_FILM);
        } else {
            sendMessage.setText("Please select a valid time.");
            sender.execute(sendMessage);
        }
    }


    private void replyToFilmSelection(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String messageText = message.getText();
        boolean isValidFilm = false;

        for (String film : films) {
            if (film.equalsIgnoreCase(messageText)) {
                isValidFilm = true;
                break;
            }
        }

        if (isValidFilm) {
            promptWithKeyboardForState(chatId, "Great Choice! You selected that movie: " +
                            messageText + ".\nNow please confirm your intention.", getYesOrNo(),
                    UserState.AWAITING_CONFIRMATION);
        } else {
            sendMessage.setText("Please select a valid movie.");
            sender.execute(sendMessage);
        }
    }


    private void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("I did not expect that.");
        sender.execute(sendMessage);
    }
    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Thank you for choosing us. Enjoy your movie!\nPress /start to book again");
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }




    private void replyToOrder(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if ("yes".equalsIgnoreCase(message.getText())) {
            sendMessage.setText("The booking is done. Thank you!\nBooking another?");
            sendMessage.setReplyMarkup(getSelectedDate());
            sender.execute(sendMessage);
            chatStates.put(chatId,UserState.CHOOSING_DAY);
        } else if ("no".equalsIgnoreCase(message.getText())){
            stopChat(chatId);
        } else {
            sendMessage.setText("Please select yes or no:");
            sendMessage.setReplyMarkup(getYesOrNo());
            sender.execute(sendMessage);
        }
    }



    public boolean userIsActive (Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
