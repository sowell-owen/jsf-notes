package Messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class NotesMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String content = ((TextMessage) message).getText();
                System.out.println("Received message: " + content);
                // Додайте обробку отриманого повідомлення від JMS
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}

