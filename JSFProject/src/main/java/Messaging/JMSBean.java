package Messaging;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Named
@RequestScoped
public class JMSBean {

    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/myQueue")
    private Queue queue;

    private String messageText;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void sendMessage() {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(queue)) {

            connection.start();

            TextMessage message = session.createTextMessage();
            message.setText(messageText);

            producer.send(message);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
