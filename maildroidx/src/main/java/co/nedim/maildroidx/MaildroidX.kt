package co.nedim.maildroidx

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sun.mail.smtp.SMTPAddressFailedException
import com.sun.mail.smtp.SMTPAddressSucceededException
import com.sun.mail.smtp.SMTPSendFailedException
import com.sun.mail.smtp.SMTPSenderFailedException
import com.sun.mail.smtp.*
import javax.mail.*
import javax.mail.internet.*
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.io.IOException


class MaildroidX(
    val to: String?,
    val toRecipients: List<String>?,
    val cc: String?,
    val ccRecipients: List<String>?,
    val bcc: String?,
    val bccRecipients: List<String>?,
    val from: String?,
    val subject: String?,
    val body: String?,
    val isJavascriptEnabled: Boolean,
    val smtp: String?,
    val smtpUsername: String?,
    val smtpPassword: String?,
    val isStartTLSEnabled: Boolean,
    val port: String,
    val attachment: String?,
    val attachments: List<String>?,
    val type: String?,
    val successCallback: onCompleteCallback?,
    val mailSuccess: Boolean?


) {

    private constructor(builder: Builder) : this(
        builder.to,
        builder.toRecipients,
        builder.cc,
        builder.ccRecipients,
        builder.bcc,
        builder.bccRecipients,
        builder.from,
        builder.subject,
        builder.body,
        builder.isJavascriptDisabled,
        builder.smtp,
        builder.smtpUsername,
        builder.smtpPassword,
        builder.isStartTLSEnabled,
        builder.port,
        builder.attachment,
        builder.attachments,
        builder.type,
        builder.successCallback,
        builder.mailSuccess
    )


    open class Builder {
        var to: String? = null
            private set
        var toRecipients: List<String>? = null
            private set
        var cc: String? = null
            private set
        var ccRecipients: List<String>? = null
            private set
        var bcc: String? = null
            private set
        var bccRecipients: List<String>? = null
            private set
        var from: String? = null
            private set
        var subject: String? = null
            private set
        var body: String? = null
            private set
        var isJavascriptDisabled: Boolean = false
            private set
        var smtp: String = ""
            private set
        var smtpUsername: String? = null
            private set
        var smtpPassword: String? = null
            private set
        var isStartTLSEnabled: Boolean = false
            private set
        var port: String = ""
            private set
        var attachment: String? = null
            private set
        var attachments: List<String>? = null
            private set
        var type: String? = null
            private set
        var successCallback: onCompleteCallback? = null
            private set
        var mailSuccess: Boolean = false
            private set

        private var errorMessage: String? = null

        fun to(to: String) = apply { this.to = to }

        fun to(to: List<String>) = apply { this.toRecipients = to }

        fun cc(cc: String) = apply { this.cc = cc }

        fun cc(cc: List<String>) = apply { this.ccRecipients = cc }

        fun bcc(bcc: String) = apply { this.bcc = bcc }

        fun bcc(bcc: List<String>) = apply { this.bccRecipients = bcc }

        fun from(from: String) = apply { this.from = from }

        fun subject(subject: String) = apply { this.subject = subject }

        fun body(body: String) = apply { this.body = body }

        fun isJavascriptDisabled(isJavascriptDisabled: Boolean) =
            apply { this.isJavascriptDisabled = isJavascriptDisabled }

        fun smtp(smtp: String) = apply { this.smtp = smtp }

        fun smtpUsername(smtpUsername: String) = apply { this.smtpUsername = smtpUsername }

        fun smtpPassword(smtpPassword: String) = apply { this.smtpPassword = smtpPassword }

        fun isStartTLSEnabled(isStartTLSEnabled: Boolean) =
            apply { this.isStartTLSEnabled = isStartTLSEnabled }

        fun port(port: String) = apply { this.port = port }

        fun attachment(attachment: String) = apply { this.attachment = attachment }

        fun attachments(attachments: List<String>) = apply { this.attachments = attachments }

        fun type(type: MaildroidXType) = apply { this.type = type.toString() }

        /**
         * onCompleteCallback
         * Version v0.0.3 ,errorMessage is provided for user in form of String
         */

        fun onCompleteCallback(successCallback: onCompleteCallback?) = apply {
            this.successCallback = successCallback
        }

        private fun callOnCompleteCallback() {
            // Get the Handler of the UI Thread to run the success callback
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (mailSuccess) {
                    successCallback?.onSuccess()
                } else {
                    errorMessage?.let { successCallback?.onFail(it) }
                }
            }, successCallback?.timeout ?: 0)
        }

        /**
         * method mail() from version v0.0.2 is calling send()
         */

        fun mail() {
            send() //Check for internet connection is DEPRECATED in version 0.0.3
        }


        /**
         * method send() is sending email through SMTP server
         */
        fun send(): Boolean {


            val typeHTML: String = "text/html; charset=utf-8"
            val typePLAIN: String = "text/plain"

            if (type.equals("HTML"))
                type = typeHTML
            else
                type = typePLAIN

            AppExecutors().diskIO().execute {
                val props = System.getProperties()

                if (smtp.isEmpty())
                    throw IllegalArgumentException("MaildroidX detected that you didn't pass [smtp] value in to the builder!")

                if (port.isEmpty())
                    throw IllegalArgumentException("MaildroidX detected that you didn't pass [port] value to the builder!")

                if (smtpUsername == null)
                    throw AuthenticationFailedException("MaildroidX detected that you didn't pass [smtpUsername] or [smtpPassword] to the builder!")

                if (smtpPassword == null)
                    throw AuthenticationFailedException("MaildroidX detected that you didn't pass [smtpUsername] or [smtpPassword] to the builder!")

                if (isJavascriptDisabled) {

                    Log.e(
                        "isJavascriptDisabled",
                        "This setting to true can cause distortion problem with CSS in E-mail layout. It should be only used when CSS is not required. "
                    )
                    body = body?.let { strapOfUnwantedJS(it) }

                }

                props.put("mail.smtp.host", smtp)
                props.put("mail.smtp.socketFactory.port", port)
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                props.put("mail.smtp.auth", true)
                props.put("mail.smtp.port", port)

                if (isStartTLSEnabled) {
                    Log.i(
                        "isStartTLSEnabled",
                        "Your SMTP server has to support STARTTLS, to use this option"
                    )
                    props.put("mail.smtp.starttls.enable", true)
                } else {
                    Log.i("isStartTLSEnabled", "MaildroidX: STARTTLS is disabled")
                    props.put("mail.smtp.starttls.enable", false)
                }

                val session = Session.getInstance(props,
                    object : Authenticator() {
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


                    /**
                     * Checking if there is any to recipients in to constructor.
                     * If there is to recipients in toRecipients, add them to the message
                     * If there is no to recipients processed with single to recipient if it's not null
                     */
                    if (toRecipients != null && toRecipients!!.isNotEmpty() && toRecipients!!.size > 1) {
                        for (email in toRecipients!!) {
                            message.addRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse(email.trim())
                            )
                        }
                    } else {
                        // Set To: header field of the header.
                        to?.let { email ->
                            message.addRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse(email.trim())
                            )
                        }
                    }

                    /**
                     * Checking if there is any cc recipients in cc constructor.
                     * If there is cc recipients in ccRecipients, add them to the message
                     * If there is no cc recipients processed with single cc recipient if it's not null
                     */
                    if (ccRecipients != null && ccRecipients!!.isNotEmpty() && ccRecipients!!.size > 1) {
                        for (email in ccRecipients!!) {
                            message.addRecipients(
                                Message.RecipientType.CC,
                                InternetAddress.parse(email.trim())
                            )
                        }
                    } else {
                        // Set CC: header field of the header.
                        cc?.let { email ->
                            message.addRecipients(
                                Message.RecipientType.CC,
                                InternetAddress.parse(email.trim())
                            )
                        }
                    }

                    /**
                     * Checking if there is any bcc recipients in bcc constructor.
                     * If there is bcc recipients in bccRecipients, add them to the message
                     * If there is no bcc recipients processed with single bcc recipient if it's not null
                     */
                    if (bccRecipients != null && bccRecipients!!.isNotEmpty() && bccRecipients!!.size > 1) {
                        for (email in bccRecipients!!) {
                            message.addRecipients(
                                Message.RecipientType.BCC,
                                InternetAddress.parse(email.trim())
                            )
                        }
                    } else {
                        // Set BCC: header field of the header.
                        bcc?.let { email ->
                            message.addRecipients(
                                Message.RecipientType.BCC,
                                InternetAddress.parse(email.trim())
                            )
                        }
                    }

                    // Set Subject: header field
                    message.subject = subject

                    // Create the message part
                    var messageBodyPart: BodyPart = MimeBodyPart()

                    // Now set the actual message
                    messageBodyPart.setContent(body, type)

                    // Create a multipart message
                    val multipart = MimeMultipart()

                    // Set text message part
                    multipart.addBodyPart(messageBodyPart)

                    // Part two is attachment
                    messageBodyPart = MimeBodyPart()

                    /**
                     * Checking if there is any files in attachment constructor.
                     * If there is files in attachments ,make multipart for each of it
                     * Add it to multipart for sending
                     * If there is no attachments processed with single attachment if it s not null
                     */
                    if (attachments != null && attachments!!.isNotEmpty() && attachments!!.size > 1) {

                        for (i in attachments!!) {
                            val attachmentPart = MimeBodyPart()
                            attachmentPart.attachFile(i)
                            multipart.addBodyPart(attachmentPart)
                        }
                    } else {
                        attachment?.let { filename ->
                            messageBodyPart.attachFile(filename)
                            multipart.addBodyPart(messageBodyPart)
                        }
                    }

                    // Send the complete message parts
                    message.setContent(multipart)

                    // Send message
                    Transport.send(message)



                    mailSuccess = true

                    Log.i("Success", "Success, mail sent [STATUS: $mailSuccess]")

                    /**
                     *
                     * @exception MessagingException The base class for all exceptions thrown by the Messaging classes
                     * @exception SMTPAddressSucceededException extends MessagingException
                    This exception is chained off a SendFailedException when the mail.smtp.reportsuccess property is true. It indicates an address to which the message was sent. The command will be an SMTP RCPT command and the return code will be the return code from that command.
                     * @exception SMTPAddressFailedException extends MessagingException
                     * This exception is thrown when the message cannot be sent.
                    The exception includes those addresses to which the message could not be sent as well as the valid addresses to which the message was sent and valid addresses to which the message was not sent.
                     * @exception SMTPSendFailedException
                    This exception will usually appear first in a chained list of exceptions, followed by SMTPAddressFailedExceptions and/or SMTPAddressSucceededExceptions, * one per address. This exception corresponds to one of the SMTP commands used to send a message, such as the MAIL, DATA, and "end of data" commands, but not including the RCPT command.
                     * @exception SMTPSenderFailedException
                    The exception includes the sender's address, which the mail server rejected.
                     */

                } catch (e: MessagingException) {
                    Log.e("MessagingException", e.toString())
                    errorMessage = e.toString()
                } catch (e: SMTPAddressSucceededException) {
                    Log.e("SMTPAddressSEx", e.toString())
                    errorMessage = e.toString()

                } catch (e: SMTPAddressFailedException) {
                    Log.e("SMTPAddressFEx", e.toString())
                    errorMessage = e.toString()


                } catch (e: SMTPSendFailedException) {
                    Log.e("SMTPSendFEx", e.toString())
                    errorMessage = e.toString()


                } catch (e: SMTPSenderFailedException) {
                    Log.e("SMTPSenderFEx", e.toString())
                    errorMessage = e.toString()

                } catch (e: IOException) {
                    Log.e("IOException", "IOException " + e.printStackTrace())
                    errorMessage = e.toString()

                } finally {
                    // Callback the onCompleteCallback when the email was sent or there was
                    // an error. The callback will be call in the UI thread
                    callOnCompleteCallback()
                }
            }
            return false
        }

        fun strapOfUnwantedJS(body: String): String {

            var strappedString = Jsoup.clean(body, Whitelist.relaxed().addTags("style"))
            return strappedString

        }


    }

    interface onCompleteCallback {
        fun onSuccess()
        fun onFail(errorMessage: String)
        val timeout: Long


        open class Builder(
            private var onSuccess: (() -> Unit)? = null,
            private var onFail: (() -> Unit)? = null,
            private var timeout: Long = 1000
        ) {

            fun timeOut(timeout: Long) = apply {
                this.timeout = timeout
            }

            fun onSuccess(onSuccess: () -> Unit) = apply {
                this.onSuccess = onSuccess
            }

            fun onFail(onFail: () -> Unit) = apply {
                this.onFail = onFail
            }

            fun build(): onCompleteCallback = object : onCompleteCallback {
                override val timeout: Long = this@Builder.timeout

                override fun onSuccess() {
                    this@Builder.onSuccess?.invoke()
                }

                override fun onFail(errorMessage: String) {
                    this@Builder.onFail?.invoke()
                }

            }
        }
    }


}
