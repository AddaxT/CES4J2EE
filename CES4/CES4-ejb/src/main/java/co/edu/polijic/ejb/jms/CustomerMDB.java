package co.edu.polijic.ejb.jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author AddaxT
 */
@MessageDriven(mappedName = "jms/TestQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = "jms/TestQueueConnectionFactory"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class CustomerMDB implements MessageListener {

    @Resource
    private MessageDrivenContext mdc;

    public CustomerMDB() {
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                System.out.println("Mensaje: " + tm.getText());
            } catch (JMSException ex) {
                Logger.getLogger(CustomerMDB.class.getName()).log(Level.SEVERE, "Error leyendo mensaje", ex);
                mdc.setRollbackOnly();
            }
        } else if (message instanceof ObjectMessage) {
            ObjectMessage om = (ObjectMessage) message;
            try {
                System.out.println("Mensaje: " + om.getObject());
            } catch (MessageFormatException ex) {
                Logger.getLogger(CustomerMDB.class.getName()).log(Level.SEVERE, null, ex);
                mdc.setRollbackOnly();
            } catch (JMSException ex) {
                Logger.getLogger(CustomerMDB.class.getName()).log(Level.SEVERE, null, ex);
                mdc.setRollbackOnly();
            }
        } else {
            Logger.getLogger(CustomerMDB.class.getName()).log(Level.SEVERE, "Tipo de mensaje no soportado");
            mdc.setRollbackOnly();
        }
    }
}
