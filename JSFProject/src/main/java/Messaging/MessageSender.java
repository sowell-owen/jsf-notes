package Messaging;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import java.util.logging.Logger;

@ApplicationScoped
public class MessageSender {

    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    private static final Logger LOGGER = Logger.getLogger(MessageSender.class.getName());

    @Resource(lookup = "jms/Queue")
    private Queue queue;

    public void sendMessage(String messageContent) {
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(queue, messageContent);
            LOGGER.info("Sending message: " + messageContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
