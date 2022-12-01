package `in`.dazzlingapps.paymentgatewaydemo

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultListener {

    /***
     * STEPS TO INTEGRATE RAZORPAY PAYMENT GATEWAY::
     * 1. Generate API key from Dashboard-> Settings
     * 2. Copy the Key Id and Key Secret appear on a pop-up page
     * 3. Use it to get the Order_id [from Postman or server]
     * 4. Add the Key Id in AndroidManifest in meta-data tag with name-> com.razorpay.ApiKey
     * 5. Set the Key Id in Checkout object in Activity
     * 6. Set the fields with proper details and call startPayment()
     */

    /**
     * WARNING: Do not store your Key Id, Key Secret or order_id in the app.
     * Instead it can be fetched from the server as the metadata of the app.
     * */
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */
        Checkout.preload(applicationContext)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID(getString(R.string.razorpay_api_key))

        findViewById<TextView>(R.id.buttonCheckout).setOnClickListener {
            startPayment()
        }
    }

    /***
     * Get the order id from server
     * It ties the Order with the payment and secures the request from being tampered.
     */
    private fun getOrderId(): String {
        return getString(R.string.razorpay_order_id)
    }

    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        var amount = "60".toFloat()*100
        try {
            val options = JSONObject()
            options.put("name", "Dazzling apps")
            options.put("description", "Description for the payment here")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("order_id", getOrderId())
            options.put("amount",  amount)//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "abc@example.com")
            prefill.put("contact", "9988776655")
            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d(TAG, "onPaymentError: $p1 ")
        Toast.makeText(this, "Error: $p1", Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        Checkout.clearUserData(this)
    }
}