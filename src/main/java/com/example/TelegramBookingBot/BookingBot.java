package com.example.TelegramBookingBot;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class BookingBot extends AbilityBot {
    private final ResponseHandler responseHandler;

    public BookingBot(Environment env) {
        super(env.getProperty("BOT_TOKEN"),"MovieTimeBot");
        responseHandler = new ResponseHandler(silent,db);
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> responseHandler.replyToStart(ctx.chatId()))
                .build();
    }

    public Reply replyToButtons () {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToButtons(getChatId(upd),upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> responseHandler.userIsActive(getChatId(upd)));
    }

    @Override
    public long creatorId() {
        return 1L;
    }
}
