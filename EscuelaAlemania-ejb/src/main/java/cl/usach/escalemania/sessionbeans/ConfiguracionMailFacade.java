/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.escalemania.sessionbeans;

import cl.usach.escalemania.entities.ConfiguracionMail;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Rodrigo Rivas
 */
@Stateless
public class ConfiguracionMailFacade extends AbstractFacade<ConfiguracionMail> implements ConfiguracionMailFacadeLocal {

    @PersistenceContext(unitName = "cl.usach.escalemania_EscuelaAlemania-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private ParametroSistemaFacadeLocal parametroSistemaFacade;
    @EJB
    private ConfiguracionMailFacadeLocal configuracionMailFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionMailFacade() {
        super(ConfiguracionMail.class);
    }

    @Override
    public ConfiguracionMail obtenerConfiguracionMailPorNombre(String nombreParametro) {
        Query query=em.createNamedQuery("ConfiguracionMail.findByName").setParameter("tipo", nombreParametro);
        return (ConfiguracionMail)query.getSingleResult();
    }
    
    @Override
    public String enviarMail(List<String> destinos, String mensaje, String asunto){
        final String mailInstitucion=parametroSistemaFacade.obtenerListaParametros("Mail Institucion").get(0).getValor();
        final String passMailInstitucion=parametroSistemaFacade.obtenerListaParametros("Contraseña Institucion").get(0).getValor();
        if(mailInstitucion.isEmpty() || passMailInstitucion.isEmpty())
            return "Falta informacion respecto al correo electrónico de la institución";
        List<ConfiguracionMail> configuracionMails=configuracionMailFacade.findAll();
        if(configuracionMails==null)
            return "No existen datos para la confirguración del envío de correos electrónicos";
        Properties props = new Properties();
        for(ConfiguracionMail cm:configuracionMails)
            props.put(cm.getTipo(), cm.getValor());
        String destino="";
        for(int i=0;i<destinos.size();i++)
            if(i==destinos.size()-1)
                destino=destino+destinos.get(i);
            else
                destino=destino+destinos.get(i)+",";
            
        try {
            Authenticator authenticator = new Authenticator() {
                                        @Override
                                        protected PasswordAuthentication getPasswordAuthentication(){
                                                return new PasswordAuthentication(mailInstitucion, passMailInstitucion);}}; 
            Session session=Session.getInstance(props,authenticator);
            
            Message message=new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport.send(message);
            return "Mail enviado exitosamente";
        } catch (Exception e) {
            return "Error al enviar el correo electrónico";
        }
        
    }
}
