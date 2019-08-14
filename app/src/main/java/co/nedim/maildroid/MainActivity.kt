package co.nedim.maildroid

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import co.nedim.maildroidx.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toMail: EditText = findViewById(R.id.mail)
        val text:EditText = findViewById(R.id.text)
        val send:Button = findViewById(R.id.save);

        save.setOnClickListener(View.OnClickListener {
            val to = toMail.text.toString()
            val text = text.text.toString()

            send(to,text)

        })


    }

    private fun sendDsl(to:String,text:String) {
        sendEmail {
            smtp("smtp.mailtrap.io")
            smtpUsername("")
            smtpPassword("")
            smtpAuthentication(true)
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

    fun send(to:String,text:String){

        val pd:ProgressDialog = ProgressDialog(this@MainActivity)
        pd.setTitle("Send email")
        pd.setMessage("Sending...")
        pd.show()

        MaildroidX.Builder()
            .smtp("smtp.mailtrap.io")
            .smtpUsername("")
            .smtpPassword("")
            .smtpAuthentication(true)
            .port("2525")
            .type(MaildroidXType.HTML)
            .to(to)
            .from("someoneover@interenet.com")
            .subject("Hello v1")
            .body(text)
            .attachment(this@MainActivity.filesDir.path +  "/abc.txt")
            .onCompleteCallback(object : MaildroidX.onCompleteCallback{
                override val timeout: Long = 3000

                override fun onSuccess() {
                    Toast.makeText(this@MainActivity,"Mail sent!",Toast.LENGTH_SHORT).show()
                    pd.cancel()

                }

                override fun onFail() {

                    Toast.makeText(this@MainActivity,"Error!",Toast.LENGTH_SHORT).show()
                    pd.cancel()
                }
            })
            .mail()
    }

    companion object {
        val TAG = MainActivity::class.java.name
    }

}
