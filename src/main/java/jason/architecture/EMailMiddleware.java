package jason.architecture;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import javax.mail.*;
import javax.mail.internet.*;

public class EMailMiddleware{
    private String Shost,Rhost;
    private String Sport,Rport;
    private String Sprotocol,Rprotocol;
    private String login;
    private String password;
    private boolean Sauth, Sstarttls, Ssslenable,Rauth, Rstarttls, Rsslenable;
    private String Sssltrust,Ssslprotocol,Rssltrust,Rsslprotocol;

    private boolean RHostEnable = false;
    private boolean RPropsEnable = false;

    private long lastChecked = 0;

    private String mailerName = "Mailer";

    public Properties sslProps () {
        //Checks which properties are required for the connection / else uses the defaut
        Properties properties = new Properties();
        if(Sauth){
            properties.put("mail"+Sprotocol+"auth", true);
        }
        if (Sstarttls) {
            properties.put("mail."+Sprotocol+".starttls.enable", true);
        }
        if (Ssslenable) {
            properties.put("mail."+Sprotocol+".ssl.enable", true);
        }
        if (Sssltrust != null) {
            properties.put("mail."+Sprotocol+".ssl.trust",Sssltrust);
        }
        if (Ssslprotocol != null) {
            properties.put("mail."+Sprotocol+".ssl.protocols",Ssslprotocol);
        }
        if(Rauth){
            properties.put("mail"+Rprotocol+"auth", true);
        }
        if (Rstarttls) {
            properties.put("mail."+Rprotocol+".starttls.enable", true);
        }
        if (Rsslenable) {
            properties.put("mail."+Rprotocol+".ssl.enable", true);
        }
        if (Rssltrust != null) {
            properties.put("mail."+Rprotocol+".ssl.trust",Rssltrust);
        }
        if (Rsslprotocol != null) {
            properties.put("mail."+Rprotocol+".ssl.protocols",Rsslprotocol);
        }

        return properties;
    }
    
    public ArrayList<jason.asSemantics.Message> checkEMail() {
        ArrayList<jason.asSemantics.Message> jMsg = new ArrayList<jason.asSemantics.Message>();
        if (System.currentTimeMillis() - this.lastChecked > 60000) {
            try{
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 15001));
            }catch (InterruptedException ex){
                //ex.printStackTrace();
            }
            System.out.println("["+this.mailerName +"] Cheking mailbox:"+this.login);
            this.lastChecked = System.currentTimeMillis();

            Session session;
            Properties props = sslProps();
            try {
                props.put("mail.store.protocol", Rprotocol);
                props.put("mail." + Rprotocol + ".host", Rhost);
                props.put("mail." + Rprotocol + ".port", Rport);
                props.put("mail." + Rprotocol + ".leaveonserver", false);

                session = Session.getDefaultInstance(props);

            } catch (Exception e) {
                System.out.println("["+this.mailerName +"] Connection error:" + e);
                return null;
            }

            try {
                // Connect to the email server
                Store store = session.getStore();
                store.connect(login, password);

                // Open the inbox folder and get the messages
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_WRITE);
                javax.mail.Message[] messages = inbox.getMessages();

                // Loop through the messages and printing info
                for (Message message : messages) {
                    System.out.println("["+this.mailerName +"] New e-mail received!");

                    //Skip messages marked for deletion
                    if (message.getFlags().contains(Flags.Flag.DELETED)) {
                        continue;
                    }

                    jason.asSemantics.Message jasonMsgs = new jason.asSemantics.Message(
                            message.getSubject(),
                            addressToString(message.getFrom()),
                            null,
                            message.getContent());
                        /*
                        jason.asSemantics.Message jasonMsgs = new jason.asSemantics.Message();
                        jasonMsgs.setIlForce(message.getSubject());
                        jasonMsgs.setSender(addressToString(message.getFrom()));
                        jasonMsgs.setPropCont(message.getContent());
                        */
                    Error:
                    //jasonMsgs.setReceiver(login);
                    jMsg.add(jasonMsgs);

                    //mark message for deletion
                    message.setFlag(Flags.Flag.DELETED, true);

                }
                if (Rprotocol.contains("imap")) {
                    inbox.expunge();
                }

                // Close the folder and store objects
                inbox.close();
                store.close();


            } catch (Exception e) {
                System.out.println("["+this.mailerName +"] Error: " + e.getMessage());
            }
        }
        return jMsg;
    }
           
       
    public static String addressToString(Address[] rawAddress) {
    	  	//Check if address is not null and convert it to regular addresses 
    		if (rawAddress != null) {
    	  		return rawAddress[0].toString();
    		} else return "null";
    }

    public void sendMsg(String recipientEmail, String subject, String message) {
        Session session;
        Properties props = sslProps();

        try {
            props.put("mail.store.protocol", Sprotocol);
            props.put("mail." + Sprotocol + ".host", Shost);
            props.put("mail." + Sprotocol + ".port", Sport);
            props.put("mail." + Sprotocol + ".leaveonserver", false);
            props.put("mail.smtp.socketFactory.port", Sport);
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password);
                }
            });
        }catch (Exception e){
            System.out.println("["+this.mailerName+"] Connection error:" + e);
            return;
        }

        try {
            // Create a new message
			Message msg = new MimeMessage(session);

			// Set the recipient, subject, and message content
			msg.setFrom(new InternetAddress(login));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			msg.setSubject(subject);
			msg.setText(message);

            // Send the message
			Transport.send(msg,login,password);

			System.out.println("["+this.mailerName +"] Email to "+recipientEmail+" sent successfully!");
		}catch (MessagingException e) {
    		System.out.println("["+this.mailerName +"] Error sending email: " + e.getMessage());
		}

    }

    //public void setSendProps(String sprotocol,String sport, String shost) {
    public void setSendProps(String shost,String sprotocol, String sport) {
        this.Sprotocol = sprotocol;
        this.Sport = sport;
        this.Shost = shost;
    }

    public void setReceiverProps(String rhost, String rprotocol, String rport) {
        this.Rprotocol = rprotocol;
        this.Rport = rport;
        this.Rhost = rhost;
        this.RHostEnable = true;
    }

    public void setSendAuth(boolean sauth,boolean sstarttls, boolean ssslenable, String sssltrust, String ssslprotocol) {
        if(sssltrust.equals("null")){
            sssltrust = null;
        }
        if(ssslprotocol.equals("null")){
            ssslprotocol = null;
        }
        this.Sauth = sauth;
        this.Sstarttls = sstarttls;
        this.Ssslenable = ssslenable;
        this.Sssltrust = sssltrust;
        this.Ssslprotocol = ssslprotocol;
    }

    public void setRAuth(boolean rauth,boolean rstarttls, boolean rsslenable, String rssltrust, String rsslprotocol) {
        if(rssltrust.equals("null")){
            rssltrust = null;
        }
        if(rsslprotocol.equals("null")){
            rsslprotocol = null;
        }
        this.Rauth = rauth;
        this.Rstarttls = rstarttls;
        this.Rsslenable = rsslenable;
        this.Rssltrust = rssltrust;
        this.Rsslprotocol = rsslprotocol;
        this.RPropsEnable = true;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRHostEnable() {
        return RHostEnable;
    }

    public boolean isRPropsEnable() {
        return RPropsEnable;
    }

    public void setMailerName(String mailerName){
        this.mailerName = mailerName;
    }
}

