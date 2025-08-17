package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history = new TreeMap<>();

    @Override
    public void onUpdated(Message msg) {
        history.put(msg.getId(), msg.copy());
    }


    /**
     * Возвращает ранее сохраненое сообщение в виде Optional<Message> по идентификатору.
     * @param id идентификатор сообщения
     * @return Возращает Optional<Message>, как это заявлено в интерфейсе HistoryReader.
     */
    @Override
    public Optional<Message> findMessageById(long id) {
        try {
            return Optional.of(history.get(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
