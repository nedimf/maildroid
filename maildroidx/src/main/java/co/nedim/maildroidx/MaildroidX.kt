package co.nedim.maildroidx

import android.os.Handler
import android.util.Log
import com.sun.mail.smtp.SMTPAddressFailedException
import java.io.IOException
import java.lang.IllegalArgumentException
import javax.activation.DataHandler
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart
import javax.activation.FileDataSource
import javax.mail.BodyPart



class MaildroidX(
    val to:String?,
    val from:String?,
    val subject:String?,
    val body:String?,
    val smtp:String?,
    val smtpUsername:String?,
    val smtpPassword:String?,
    val port: String,
    val smtpAuthentication:Boolean,
    val attachments: String?,
    val type:String?,
    val successCallback: onCompleteCallback?,
    val mailSuccess:Boolean?


)
{


    private constructor(builder: Builder) : this(
        builder.to,
        builder.from,
        builder.subject ,
        builder.body ,
        builder.smtp    ,
        builder.smtpUsername,
        builder.smtpPassword,
        builder.port,
        builder.smtpAuthentication,
        builder.attachments,
        builder.type,
        builder.successCallback,
        builder.mailSuccess)



    class Builder {
        var to:String? = null
            private set
        var from:String? = null
            private set
        var subject:String? = null
            private set
        var body:String? = null
            private set
        var smtp:String = ""
            private set
        var smtpUsername:String? = null
            private set
        var smtpPassword:String? = null
            private set
        var port:String = ""
            private set
        var smtpAuthentication:Boolean = false
            private set
        var attachments:String? = null
            private set
        var type:String? = null
            private set
        var successCallback: onCompleteCallback? = null
            private set
        var mailSuccess:Boolean = false
            private set


        fun to(to: String) = apply { this.to = to }

        fun from(from: String) = apply { this.from = from }

        fun subject(subject: String ) = apply { this.subject = subject }

        fun body(body: String) = apply { this.body = body }

        fun smtp(smtp: String) = apply { this.smtp = smtp }

        fun smtpUsername(smtpUsername: String) = apply { this.smtpUsername = smtpUsername }

        fun smtpPassword(smtpPassword: String) = apply { this.smtpPassword = smtpPassword }

        fun port(port: String) = apply { this.port = port }

        fun smtpAuthentication(smtpAuthentication: Boolean) = apply { this.smtpAuthentication = smtpAuthentication }

        fun attachment(attachments: String) = apply { this.attachments = attachments }

        fun type(type: MaildroidXType) = apply { this.type = type.toString() }
        

        fun onCompleteCallback(successCallback: onCompleteCallback?, timeout:Long) = apply {

            Handler().postDelayed({
                if(mailSuccess) {
                    successCallback?.onSuccess()
                }else{
                    successCallback?.onFail()
                }
            }, timeout)
            }

            fun mail(): Boolean {

            val typeHTML:String = "text/html"
            val typePLAIN:String = "text/plain"

            if(type.equals("HTML"))
                type= typeHTML
            else
                type = typePLAIN

            AppExecutors().diskIO().execute{
                val props = System.getProperties()

                if(smtp.isEmpty())
                    throw IllegalArgumentException("MaildroidX detected that you didn't pass [smtp] value in to the builder!")

                if(!smtpAuthentication)
                   throw IllegalArgumentException("MaildroidX detected that you didn't pass [smtpAuthentication] value to the builder!")

                if(port.isEmpty())
                    throw IllegalArgumentException("MaildroidX detected that you didn't pass [port] value to the builder!")



                if(smtpUsername == null)
                    throw AuthenticationFailedException("MaildroidX detected that you didn't pass [smtpUsername] or [smtpPassword] to the builder!")

                if(smtpPassword == null)
                    throw AuthenticationFailedException("MaildroidX detected that you didn't pass [smtpUsername] or [smtpPassword] to the builder!")


                    props.put("mail.smtp.host", smtp)
                    props.put("mail.smtp.socketFactory.port", port)
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                    props.put("mail.smtp.auth", smtpAuthentication)
                    props.put("mail.smtp.port", port)



                val session =  Session.getInstance(props,
                    object : javax.mail.Authenticator() {
                        //Authenticating the password
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(smtpUsername, smtpPassword)
                        }
                    })

                try {
                    // Create a default MimeMessage object.
                    val message = MimeMessage(session)

                    // Set From: header field of the header.
                    message.setFrom(InternetAddress(from))

                    // Set To: header field of the header.
                    message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(to)
                    )

                    // Set Subject: header field
                    message.subject = subject



                    if(attachments !== null) {

                        // Create the message part
                        var messageBodyPart: BodyPart = MimeBodyPart()

                        // Now set the actual message
                        messageBodyPart.setContent(body,type)

                        // Create a multipart message
                        val multipart = MimeMultipart()

                        // Set text message part
                        multipart.addBodyPart(messageBodyPart)

                        // Part two is attachment
                        messageBodyPart = MimeBodyPart()

                        val filename = attachments

                        messageBodyPart.attachFile(filename)
                        multipart.addBodyPart(messageBodyPart)


                        // Send the complete message parts
                        message.setContent(multipart)
                    }else{
                        //If there is no multipart setContent body and type
                        message.setContent(body,type)
                    }

                    // Send message
                    Transport.send(message)

                    mailSuccess = true
                    Log.w("Success", "Success, mail sent [STATUS: $mailSuccess]")


                } catch (e: MessagingException) {
                    e.printStackTrace()
                } catch (e: SMTPAddressFailedException){
                    Log.i("SMTPAddressFailed","SMTPAddressFailedException: Mail is not sent!")

                }
                catch (e: MessagingException){
                    e.printStackTrace()
                }catch (e: IOException){
                    Log.i("IOException","IOException " + e.printStackTrace())
                }
            }
                return false
        }





        //fun build() = MaildroidX(context,to,from,subject,body,smtp,smtpUsername,smtpPassword,port,smtpAuthentication,attachments,type,onCompleteCallback, onFailureCallback)
    }

    interface onCompleteCallback {
        fun onSuccess()
        fun onFail()
    }


}



