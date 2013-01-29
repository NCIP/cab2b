/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package cab2b.logger;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.util.StringUtils;

/**
 * @author Chandrakant Talele
 */
public class Cab2bMailLoggerForBuild extends DefaultLogger {
    long startTime;

    static String smtpAuthorizedUser;

    static String userPassword;

    static Properties props;

    static {
        String propertyfile = "Mail.properties";
        java.io.InputStream is = Cab2bMailLoggerForBuild.class.getClassLoader().getResourceAsStream(propertyfile);
        props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            System.out.println((new StringBuilder("Unable to load properties from : ")).append(propertyfile).toString());
            e.printStackTrace();
        }

        smtpAuthorizedUser = props.getProperty("MailLogger.user");
        userPassword = props.getProperty("MailLogger.password");

    }

    /**
     * @see org.apache.tools.ant.BuildListener#buildFinished(org.apache.tools.ant.BuildEvent)
     */
    public void buildFinished(BuildEvent event) {
        Throwable error = event.getException();
        StringBuffer message = new StringBuffer();
        String subject = "";
        if (error == null) {
            subject = props.getProperty("MailLogger.success.subject");
            message.append(subject);
        } else {
            subject = props.getProperty("MailLogger.failure.subject");
            message.append(subject);
            message.append("<br><br>");
            if (3 <= msgOutputLevel || !(error instanceof BuildException))
                message.append(StringUtils.getStackTrace(error));
            else if (error instanceof BuildException)
                message.append(error.toString()).append(lSep);
            else
                message.append(error.getMessage()).append(lSep);
        }
        String reportURL = props.getProperty("MailLogger.reportURL");

        message.append("<br><br>To see the reports, please visit : <a href=\"");
        message.append(reportURL);
        message.append("\">");
        message.append(reportURL);
        message.append("</a><br><br>Total time taken for build : ");
        message.append(formatTime(System.currentTimeMillis() - startTime));
        String msg = message.toString();
        if (error == null)
            printMessage(msg, out, 3);
        else
            printMessage(msg, err, 0);
        sendMail(subject, msg);
    }

    /**
     * @see org.apache.tools.ant.BuildListener#buildStarted(org.apache.tools.ant.BuildEvent)
     */
    public void buildStarted(BuildEvent event) {
        startTime = System.currentTimeMillis();
    }

    /**
     * @param subject
     * @param messageBody
     */
    private void sendMail(String subject, String messageBody) {
        String emailFromAddress = props.getProperty("MailLogger.from");
        String toList = props.getProperty("MailLogger.toList");
        String emailList[] = toList.split(";");
        try {
            postMail(emailList, subject, messageBody, emailFromAddress);
        } catch (MessagingException e) {
            System.out.println("Mail Sending Failed");
            e.printStackTrace();
        }
        System.out.println("------------------------------------------------");
        System.out.println("Sucessfully Sent mail to : " + toList);
        System.out.println("------------------------------------------------");
    }

    /**
     * @param recipients
     * @param subject
     * @param message
     * @param from
     * @throws MessagingException
     */
    public void postMail(String recipients[], String subject, String message, String from)
            throws MessagingException {
        boolean debug = false;
        Properties propertiesToPass = new Properties();
        String smtpHostName = props.getProperty("MailLogger.mailhost");
        propertiesToPass.put("mail.smtp.host", smtpHostName);
        propertiesToPass.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(propertiesToPass, auth);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        InternetAddress addressTo[] = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++)
            addressTo[i] = new InternetAddress(recipients[i]);

        msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setContent(message, "text/html");
        Transport.send(msg);
    }

}

/**
 * @author Chandrakant Talele
 */
class SMTPAuthenticator extends Authenticator {
    /**
     * @see javax.mail.Authenticator#getPasswordAuthentication()
     */
    public PasswordAuthentication getPasswordAuthentication() {
        String username = Cab2bMailLoggerForBuild.smtpAuthorizedUser;
        String password = Cab2bMailLoggerForBuild.userPassword;
        return new PasswordAuthentication(username, password);
    }
}