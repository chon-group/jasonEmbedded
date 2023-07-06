package jason.architecture;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class EMailMiddleware{

    private String protocol,host,port,login,password;
    private boolean auth, starttls, sslenable;
    private String ssltrust,sslprotocol;



    public Properties sslProps () {
        //Checks which properties are required for the connection / else uses the defaut
        Properties properties = new Properties();
        if(auth){
            properties.put("mail"+protocol+"auth", true);
        }
        if (starttls) {
    		properties.put("mail."+protocol+".starttls.enable", true);
    	}
    	if (sslenable) {
            properties.put("mail."+protocol+".ssl.enable", true);
    	}
    	if (ssltrust != null) {
            properties.put("mail."+protocol+".ssl.trust",ssltrust);
    	}
        return properties;
    }

    public Session connection() {
        Properties props = sslProps();
        try {
            props.put("mail.store.protocol", protocol);
            props.put("mail." + protocol + ".host", host);
            props.put("mail." + protocol + ".port", port);
            props.put("mail." + protocol + ".leaveonserver", false);

            if (protocol.equalsIgnoreCase("smtp")){
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            }
                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
                });
                return session;

                //Session defaultInstance = Session.getDefaultInstance(props);
                //return defaultInstance;
        }catch (Exception e){
            System.out.println("Erro na conexão:" + e);
            return null;
        }
    }
    
    public ArrayList<jason.asSemantics.Message> checkMail() {
        Session session = connection();
        ArrayList<jason.asSemantics.Message> jMsg = new ArrayList<jason.asSemantics.Message>();
        try {
            // Connect to the email server
            Store store = session.getStore();
            store.connect(login, password);
            
            // Open the inbox folder and get the messages
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            javax.mail.Message[] messages = inbox.getMessages();                       
            
            // Loop through the messages and printing info
            for (int i = 0; i < messages.length; i++) {
            
              	//Skip messages marked for deletion
            	if (messages[i].getFlags().contains(Flags.Flag.DELETED)){ continue;}
            	
            	jason.asSemantics.Message jasonMsgs = new jason.asSemantics.Message();
                jasonMsgs.setIlForce(messages[i].getSubject());
                jasonMsgs.setSender(convert(messages[i].getFrom()));
                jasonMsgs.setPropCont(messages[i].getContent());
                jasonMsgs.setReceiver(login);
                jMsg.add(jasonMsgs);
                                                            
                //mark message for deletion
                messages[i].setFlag(Flags.Flag.DELETED,false);
                            
            }
            if (protocol.contains("imap")){
            	inbox.expunge();
        	}
            
            // Close the folder and store objects
            inbox.close();
            store.close();


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return jMsg;
    }
           
       
    public static String convert(Address[] rawAddress) {
    	  	//Check if address is not null and convert it to regular addresses 
    		if (rawAddress != null) {
    	  		return rawAddress[0].toString();
    		} else return "null";
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setSslenable(boolean sslenable) {
        this.sslenable = sslenable;
    }

    public void setSsltrust(String ssltrust) {
        this.ssltrust = ssltrust;
    }

    public void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public void setSslprotocol(String sslprotocol) {
        this.sslprotocol = sslprotocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    
    public void sendMsg(String recipientEmail, String subject, String message) {
        Session session = connection();
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

			System.out.println("Sent successfully!");
		}catch (MessagingException e) {
    		System.out.println("Error sending email: " + e.getMessage());
		}

    }

}
