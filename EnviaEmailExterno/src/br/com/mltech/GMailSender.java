package br.com.mltech;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

/**
 * GMailSender
 * 
 * @author maurocl
 * 
 */
public class GMailSender extends javax.mail.Authenticator {

	private String mailhost = "smtp.gmail.com";
	private String user; // usuário
	private String password; // senha
	private Session session; // sessão

	static {
		// Security.addProvider(new com.provider.JSSEProvider());
		Security.addProvider(new br.com.mltech.JSSEProvider());
	}

	/**
	 * GMailSender(String user, String password)
	 * 
	 * @param user Usuário
	 * @param password Senha
	 * 
	 */
	public GMailSender(String user, String password) {

		this.user = user;
		this.password = password;

		Properties props = new Properties();

		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		props.setProperty("mail.smtp.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
		
	}

	/**
	 * getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	/**
	 * sendMail(String subject, String body, String sender, String recipients)
	 * 
	 * @param subject
	 * @param body
	 * @param sender
	 * @param recipients
	 * 
	 * @throws Exception Lança uma Exception
	 * 
	 */
	public synchronized void sendMail(String subject, String body,
			String sender, String recipients) throws Exception {

		try {

			MimeMessage message = new MimeMessage(session);

			DataHandler handler = new DataHandler(new ByteArrayDataSource(
					body.getBytes(), "text/plain"));

			message.setSender(new InternetAddress(sender));
			message.setSubject(subject);
			message.setDataHandler(handler);

			if (recipients.indexOf(',') > 0) {
				
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipients));
				
			} else {
				
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients));
				
			}

			Transport.send(message);

		} catch (Exception e) {

		}
	}

	/**
	 * ByteArrayDataSource
	 * 
	 * Cria um DataSource a partir de um array de bytes
	 * 
	 * @author maurocl
	 * 
	 */
	public class ByteArrayDataSource implements DataSource {

		/**
		 * Array de bytes
		 */
		private byte[] data;

		/**
		 * Tipo
		 */
		private String type;

		/**
		 * ByteArrayDataSource(byte[] data, String type)
		 * 
		 * Construtor
		 * 
		 * @param data
		 *            Um array de bytes
		 * @param type
		 *            Tipo do array de bytes
		 * 
		 */
		public ByteArrayDataSource(byte[] data, String type) {
			super();
			this.data = data;
			this.type = type;
		}

		/**
		 * ByteArrayDataSource(byte[] data)
		 * 
		 * @param data
		 */
		public ByteArrayDataSource(byte[] data) {
			super();
			this.data = data;
		}

		/**
		 * setType(String type)
		 * 
		 * Configura o tipo do ...
		 * 
		 * @param type
		 *            Tipo do ...
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * getContentType()
		 * 
		 * @return String 
		 */
		public String getContentType() {
			if (type == null) {
				return "application/octet-stream";
			} else {
				return type;
			}
		}

		/**
		 * getInputStream()
		 * 
		 * @return 
		 * 
		 * @throws IOException
		 * 
		 */
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data);
		}

		/**
		 * getName()
		 * 
		 * @return String com o nome do DataSource
		 */
		public String getName() {
			return "ByteArrayDataSource";
		}

		/**
		 * getOutputStream()
		 * 
		 * @return OutputStream
		 * 
		 * @throws IOException
		 * 
		 */
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Not Supported");
		}

	}

}