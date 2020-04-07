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
        val send:Button = findViewById(R.id.sendBTN)

        send.setOnClickListener(View.OnClickListener {
            val recipient = toMail.text.toString()
            val text = text.text.toString()

            if(recipient.isNotEmpty() || text.isNotEmpty()){
                send(recipient,text)
            }else{
                Toast.makeText(this@MainActivity,"Please check your input",Toast.LENGTH_SHORT).show()
            }


        })
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
            .port("2525")
            .type(MaildroidXType.HTML)
            .to(to)
            .from("someoneover@interenet.com")
            .subject("Hello v1")
            .body(text)
            .isJavascriptDisabled(false)
            //.attachment(this@MainActivity.filesDir.path +  "/abc.txt")
            .onCompleteCallback(object : MaildroidX.onCompleteCallback{
                override val timeout: Long = 4000 //Add timeout accordingly

                override fun onSuccess() {
                    Toast.makeText(this@MainActivity,"Mail sent!",Toast.LENGTH_SHORT).show()
                    pd.cancel()

                }

                override fun onFail(errorMessage: String) {

                    Toast.makeText(this@MainActivity, errorMessage,Toast.LENGTH_SHORT).show()
                    pd.cancel()
                }

            })
            .mail()
    }

    companion object {
        val TAG = MainActivity::class.java.name
    }

    private fun sendDsl(to:String,text:String) {
        sendEmail {
            smtp("smtp.mailtrap.io")
            smtpUsername("246d1daf3ff04f")
            smtpPassword("1e8ba2dbf19f4f")
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
