package com.metflix.controller;


import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.metflix.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.Event;

import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;


@RestController()
@RequestMapping("/pay/")
public class PaymentController {
    private final String secretKey;
    private final SubscriptionService subscriptionService;

    public PaymentController(@Value("${STRIPE_SECRET_KEY}") String secretKey, SubscriptionService subscriptionService) {
        this.secretKey = secretKey;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("webhook")
    public ResponseEntity<String> handle(@RequestBody String payload, @RequestHeader(name = "Stripe-Signature") String sigHeader) {
        Event event = null;
        //System.out.println("PAYLOAD: " + payload);

        try {
            event = Webhook.constructEvent(payload, sigHeader, secretKey);
        } catch (JsonSyntaxException e) {
            System.out.println("Invalid payload: " + e.getMessage());
            return new ResponseEntity<>("Received invalid payload!", HttpStatus.BAD_REQUEST);
        } catch (SignatureVerificationException e) {
            System.out.println("Error verifying webhook signature: " + e.getMessage());
            return new ResponseEntity<>("Error verifying webhook signature!!", HttpStatus.BAD_REQUEST);
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            System.out.println("Deserialization failed, probably using wrong API!");
            return new ResponseEntity<>("Devs probably using wrong APIs, cannot proceed.", HttpStatus.BAD_REQUEST);
        }

        Subscription subscription;

        switch (event.getType()) {
            case "customer.subscription.deleted":

                subscription = (Subscription) stripeObject;

                subscriptionService.unsubscribeUser(subscription.getId());

                System.out.println("user canceled their subscription");
                return new ResponseEntity<>(HttpStatus.OK);


            case "checkout.session.completed":
                try {
                    JsonObject object = new Gson().fromJson(payload, JsonObject.class).getAsJsonObject("data")
                            .getAsJsonObject("object");

                     String subscriptionId;
                    int clientId;
                     try {
                         clientId = object.get("client_reference_id").getAsInt();
                         subscriptionId = object.get("subscription").getAsString();

                     } catch (Exception e) {
                         clientId = 1;
                         subscriptionId = "sub_fakeID";
                     }


                    subscriptionService.subscribeUser(clientId, subscriptionId);
                    System.out.format("Checkout successful. Client with id {%s} should be subscribed now.\n", clientId);
                    return new ResponseEntity<>("client is now subscribed", HttpStatus.OK);

                } catch (UnsupportedOperationException e) {
                    System.out.println("Can't find client ID or subscription ID!");
                    return new ResponseEntity<>("Didn't receive necessary information", HttpStatus.BAD_REQUEST);
                }
        }

        return new ResponseEntity<>("Got to the end without doing anything, feature not implemented", HttpStatus.I_AM_A_TEAPOT);
    }
}
