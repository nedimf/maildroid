package co.nedim.maildroidx

@DslMarker
annotation class EmailDsl

@EmailDsl
class MaildroidXBuilderDsl: MaildroidX.Builder()

@EmailDsl
class CallbackBuilderDsl: MaildroidX.onCompleteCallback.Builder()

/**
 * Builds and sends email.
 * <p>Simple dsl definition to provide an easier way of sending email without having
 * to directly instantiate [MaildroidX.Builder()] or call [MaildroidX.Builder().mail()].</p>
 *
 * <pre>
 *     sendEmail {
 *          smtp("smtp.mailtrap.io")
 *          smtpUsername("username")
 *          smtpPassword("password")
 *          smtpAuthentication(true)
 *          port("2525")
 *          type(MaildroidXType.HTML)
 *          to("johndoe@email.com")
 *          from("janedoen@email.com")
 *          subject("Hello!")
 *          body("email body")
 *          attachment("path_to_file/file.txt")
 *          callback {
 *              timeOut(3000)
 *              onSuccess {
 *                  Log.d("MaildroidX",  "SUCCESS")
 *              }
 *              onFail {
 *                  Log.d("MaildroidX",  "FAIL")
 *              }
 *          }
 *     }
 * </pre>
 *
 * @param buildMaildroidX lambda expression scoped as [MaildroidX.Builder]
 */
inline fun sendEmail(buildMaildroidX: MaildroidXBuilderDsl.() -> Unit) {
    val maildroidXBuilder = MaildroidXBuilderDsl()
    maildroidXBuilder.buildMaildroidX()
    maildroidXBuilder.mail()
}


/**
 * Builds the callbacks for the email response.
 *
 * <pre>
 *     callback {
 *          timeout(3000)
 *          onSuccess {
 *              Log.d("MaildroidX",  "SUCCESS")
 *          }
 *          onFail {
 *              Log.d("MaildroidX",  "FAIL")
 *          }
 *      }
 * </pre>
 *
 * @param buildCallBack lambda expression scoped as [CallbackBuilderDsl]
 */
inline fun MaildroidX.Builder.callback(buildCallBack: CallbackBuilderDsl.() -> Unit) {
    val callbackBuilder = CallbackBuilderDsl()
    callbackBuilder.buildCallBack()
    val callBack = callbackBuilder.build()
    this.onCompleteCallback(callBack)
}
