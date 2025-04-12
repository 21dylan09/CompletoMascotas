package com.example.loginsignup.actividadesDueño;

import android.content.Context;
import android.util.Log;
import com.example.loginsignup.R;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    // Método estático que recibe el contexto como parámetro
    public static void enviarCorreo(Context context, String destinatario, String asunto, String cuerpo) {
        final String correo = "dylanf.lozanof2004@gmail.com"; // Cambia esto por tu correo
        final String claveApp = "huqi ufru vrio ljex";  // Usando la clave de Google Maps como clave de la app

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correo, claveApp);  // Tu correo y clave
            }
        });

        try {
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correo));  // Remitente
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));  // Destinatario
            message.setSubject(asunto);  // Asunto
            message.setText(cuerpo);  // Cuerpo del mensaje

            // Enviar el correo
            Transport.send(message);
            Log.d("MailSender", "Correo enviado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MailSender", "Error al enviar correo: " + e.getMessage());
        }
    }
}
