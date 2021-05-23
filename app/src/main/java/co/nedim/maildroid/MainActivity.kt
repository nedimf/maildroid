package co.nedim.maildroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import co.nedim.maildroid.databinding.ActivityMainBinding
import co.nedim.maildroidx.*
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bind Layout
        val binding = ActivityMainBinding.inflate(layoutInflater)



        setContentView(binding.root)

        binding.btnSend.setOnClickListener {

            val sbSendEmail = Snackbar.make(binding.root, "Sending email...", Snackbar.LENGTH_INDEFINITE)
            sbSendEmail.show()

            //Make sure all fields are filled out
            for(view in binding.root.children){
                if(view.id == R.id.etCC || view.id == R.id.etBCC) continue
                if(view is EditText){
                    if(view.text.toString() == ""){
                        sbSendEmail.dismiss()
                        view.requestFocus()
                        Snackbar.make(binding.root, "Please fill out all fields!", Snackbar.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }
            }

            val emailType = if (binding.rbHTML.isChecked) MaildroidXType.HTML else MaildroidXType.PLAIN
            val toEmails = binding.etTO.text.toString().split(";")
            val ccEmails = binding.etCC.text.toString().split(";")
            val bccEmails = binding.etBCC.text.toString().split(";")

            MaildroidX.Builder()
                .smtp(binding.etSMTP.text.toString())
                .port(binding.etSMTPPort.text.toString())
                .smtpUsername(binding.etSMTPUserFrom.text.toString())
                .smtpPassword(binding.etSMTPPass.text.toString())
                .isStartTLSEnabled(binding.tbStartTLS.isChecked)
                .isJavascriptDisabled(!binding.tbJavascript.isChecked)
                .type(emailType)
                .from(binding.etSMTPUserFrom.text.toString())
                .to(toEmails)
                .cc(ccEmails)
                .bcc(bccEmails)
                .subject(binding.etSubject.text.toString())
                .body(binding.etBody.text.toString())
                .onCompleteCallback(object : MaildroidX.onCompleteCallback{
                    override fun onSuccess() {
                        sbSendEmail.dismiss()
                        Snackbar.make(binding.root, "Email Sent!", Snackbar.LENGTH_LONG).show()
                    }

                    override fun onFail(errorMessage: String) {
                        sbSendEmail.dismiss()
                        Snackbar.make(binding.root, "Error Sending Email! $errorMessage", Snackbar.LENGTH_LONG).show()
                    }

                    override val timeout: Long
                        get() = 4000
                })
                .mail()


        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.name
    }

    private fun sendDsl(to:String,text:String) {
        sendEmail {
            smtp("smtp.mailtrap.io")
            smtpUsername("")
            smtpPassword("")
            port("465")
            type(MaildroidXType.HTML)
            to(to)
            from("someoneover@interenet.com")
            subject("Hello v1")
            body(text)
            callback {
                timeOut(3000)
                onSuccess {
                    Toast.makeText(this@MainActivity,"Mail sent!",Toast.LENGTH_SHORT).show()
                    Log.d(TAG,  "SUCCESS")
                }
                onFail {
                    Toast.makeText(this@MainActivity,"Error!",Toast.LENGTH_SHORT).show()
                    Log.d(TAG,  "ERROR")
                }
            }
        }
    }

}
