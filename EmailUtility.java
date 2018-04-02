package utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import base.Constants;

/**
 *
 * ClassName: EmailUtility Description: This class is for sending the email
 */

public class EmailUtility {
	static PropertyUtility utilProps = new PropertyUtility();
	
	// Converts the svg image in Xslt report to png image
	public static void getPieChart() throws TranscoderException, IOException {
		XsltReport.createXsltReport();
		// Step -1: We read the input SVG document into Transcoder Input
		// We use Java NIO for this purpose
		String svg_URI_input = Paths
				.get(System.getProperty("user.dir")
						+ "/XSLT_Reports/overview-chart.svg").toUri().toURL()
				.toString();
		TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);
		
		// Step-2: Define OutputStream to PNG Image and attach to
		// TranscoderOutput
		OutputStream png_ostream = new FileOutputStream(
				System.getProperty("user.dir")
						+ "/XSLT_Reports/overview-chart.png");
		TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);
		
		// Step-3: Create PNGTranscoder and define hints if required
		PNGTranscoder my_converter = new PNGTranscoder();
		
		// Step-4: Convert and Write output
		my_converter.transcode(input_svg_image, output_png_image);
		
		// Step 5- close / flush Output Stream
		png_ostream.flush();
		png_ostream.close();
	}

	public static String getTestSummaryReport() {		
		final String htmlStr = PropertyUtility.getPropertyTemp("bodyContent");
		String testPassed = utilProps.getTestdata("temp.properties").get("TestsPassed");
		String testFailed = utilProps.getTestdata("temp.properties").get("TestsFailed");
		String testSkipped = utilProps.getTestdata("temp.properties").get("TestsSkipped");
		String testRun = utilProps.getTestdata("temp.properties").get("TestsRun");
		String mailText = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"1\" style=\"table-layout:fixed;font-size:13px;font-family:arial,sans,sans-serif;border-collapse:collapse;border:1px solid rgb(204,204,204)\">"
				+ "<colgroup><col width=\"249\"><col width=\"143\"></colgroup><tbody><tr style=\"height:21px\"><td style=\"padding:2px 3px;vertical-align:bottom;background-color:rgb(153,153,153)\" rowspan=\"1\" colspan=\"2\" align=\"center\"><b>Test Run Summary Report</b></td>"
				+ "</tr><tr style=\"height:21px\"><td style=\"vertical-align:bottom\" align=\"center\"><span style=\"font-family:arial;font-size:small\">Total Tests Run</span><br></td><td  style=\"vertical-align:bottom\" align=\"center\">"
				+ testRun
				+ "</td></tr><tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
				+ "<span style=\"font-family:arial;font-size:small\">Tests Passed</span></td><td  style=\"vertical-align:bottom\"align=\"center\">"
				+ testPassed
				+ "</td></tr><tr style=\"height:21px\">"
				+ "<td style=\"vertical-align:bottom\"align=\"center\"><span style=\"font-family:arial;font-size:small\">Tests Failed"
				+ "</span></td><td  style=\"vertical-align:bottom\" align=\"center\">"
				+ testFailed
				+ "</td></tr><tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
				+ "<span style=\"font-family:arial;font-size:small\">Tests Skipped</span></td><td  style=\"vertical-align:bottom\"align=\"center\">"
				+ testSkipped + "</td></tr></tbody></table>";
		String testStatusList = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"1\" style=\"table-layout:fixed;font-size:13px;font-family:arial,sans,sans-serif;border-collapse:collapse;border:1px solid rgb(204,204,204)\">"
				+ "<colgroup><col width=\"249\"><col width=\"143\"></colgroup><tbody><tr style=\"height:21px\"><td style=\"padding:2px 3px;vertical-align:bottom;background-color:rgb(153,153,153)\" rowspan=\"1\" colspan=\"2\" align=\"center\"><b>Test Status Report</b></td>"
				+ htmlStr + "</tbody></table>";
		return mailText + testStatusList;
	}

	/*
	 * Method Name: main() Description: This function uses the jmail api to send
	 * the mail. sender mail id, password and recipient mail id passed are read
	 * from config.properties file.
	 */
	
		public static void main(String[] args) throws TranscoderException, IOException {
		Log.addMessage("Test completed!");
		final String senderMailId;
		final String senderPassword;
		String receipientMailId;
		String logFileName;
		Properties props = new Properties();
		senderMailId = PropertyUtility.getProperty("SenderMailId");
		senderPassword = PropertyUtility.getProperty("Senderpassword");
		receipientMailId = PropertyUtility.getProperty("Recipientmailid");
		String[] emailList = receipientMailId.split(",");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(senderMailId,senderPassword);
					}
				});
		try {
			Log.addMessage("Test report sent via mail");
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			for(int i=0;i<emailList.length;i++){
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailList[i]));
				message.setSubject("Automation Test Summary Report  " + getDate());
				// create the message part
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				Multipart multipart = new MimeMultipart();
				logFileName = "Test Report";
				String fileAttachment = "test-output-extent/"+Constants.projectName+"_automation_report.html";
				StringBuffer body = new StringBuffer("<html>Hi, <br><br>Following is the Test Summary Report for "+Constants.projectName);
				body.append("<br><br>");
				body.append(getTestSummaryReport());
				body.append("<img src=\"cid:image\" width=\"100%\" height=\"100%\" /><br>");
				body.append("<br><br><br> Thanks,<br>Automation Team");
				body.append("</html>");
				messageBodyPart.setContent(body.toString(),"text/html; charset=utf-8");
				multipart.addBodyPart(messageBodyPart);
	
				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(fileAttachment);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(logFileName);
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
	
				//Send the message
				Transport.send(message);
			}
		} catch (MessagingException e) {
			System.out.println(e);
			throw new RuntimeException(e);
		}

	}

	public static void insertChartIntoMail(Multipart p)throws TranscoderException, IOException, MessagingException {
		getPieChart();
		Map<String, String> mapInlineImages = new HashMap<>();
		mapInlineImages.put("image", System.getProperty("user.dir")+ "/XSLT_Reports/overview-chart.png");
		if (mapInlineImages != null && mapInlineImages.size() > 0) {
			Set<String> setImageID = mapInlineImages.keySet();
			for (String contentId : setImageID) {
				MimeBodyPart imagePart = new MimeBodyPart();
				imagePart.setHeader("Content-ID", "<" + contentId + ">");
				imagePart.setDisposition(Part.INLINE);
				String imageFilePath = mapInlineImages.get(contentId);
				try {
					imagePart.attachFile(imageFilePath);
				}catch (IOException ex) {
					ex.printStackTrace();
				}
				p.addBodyPart(imagePart);
			}
		}
	}

	public static String getDate() {
		Date date = new Date();
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormatObject = new SimpleDateFormat(dateFormat);
		String formattedDate = simpleDateFormatObject.format(date);
		return formattedDate;
	}
}
