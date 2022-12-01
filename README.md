# PaymentGatewayDemo

Demo project showing the Payment gateway integration with Razorpay. 

Visit <a href="https://razorpay.com/docs/payments/payment-gateway/android-integration/standard/build-integration/">Razorpay docs<a/>

       STEPS TO INTEGRATE RAZORPAY PAYMENT GATEWAY::
       1. Generate API key from Dashboard-> Settings 
       2. Copy the Key Id and Key Secret appear on a pop-up
       3. Use it to get the Order_id [from Postman or server]
       4. Add the Key Id in AndroidManifest in meta-data tag with name-> com.razorpay.ApiKey
       5. Set the Key Id in Checkout object in Activity
       6. Set the fields with proper details and call startPayment()
