package rolevax.testsecuritylib;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Created by gfp on 12/7/15.
 */
public class TestSecLib {
    public static void test(String taint, boolean leak) {
        if (leak) { // try a raw sending as a reference
            raw(taint);
        }

        digest(taint, leak);
        mac(taint, leak);
        privateKey(taint, leak);
        publicKey(taint, leak);
        digitalCertificate(taint, leak);
    }

    public static void raw(String taint) {
        try {
            send(("raw taint: " + taint).getBytes());
        } catch (Exception e) {
            Log.e("wtx", "exception when making socket");
        }
    }

    public static void digest(String taint, boolean leak) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(taint.getBytes());
            byte[] digest = messageDigest.digest();
            if (leak) {
                send(("digest " + new String(digest)).getBytes());
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("wtx", "no MD% algorithm");
        }
    }

    public static void mac(String taint, boolean leak) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            SecretKey MD5key = keyGen.generateKey();
            mac.init(MD5key);
            mac.update(taint.getBytes());
            byte[] macBytes = mac.doFinal();
            if (leak)
                send(("mac" + new String(macBytes)).getBytes());
        } catch (Exception e) {
            Log.e("wtx", "exception in mac");
        }
    }

    public static void privateKey(String taint, boolean leak) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            Key key = keyGen.generateKey();
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            System.out.println( "\n" + cipher.getProvider().getInfo());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(taint.getBytes());
            if (leak)
                send(("private key cipher " + new String(cipherText)).getBytes());

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] newPlainText = cipher.doFinal(cipherText);
            if (leak)
                send(("private key plain " + new String(newPlainText)).getBytes());
        } catch (Exception e) {
            Log.e("wtx", "exception in symmetric key encryption");
        }
    }

    public static void publicKey(String taint, boolean leak) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
            byte[] cipherText = cipher.doFinal(taint.getBytes());
            if (leak)
                send(("public-key cipher " + new String(cipherText)).getBytes());

            cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
            byte[] newPlainText = cipher.doFinal(cipherText);
            if (leak)
                send(("public-key plain " + new String(newPlainText)).getBytes());
        } catch (Exception e) {
            Log.e("wtx", "exception in public key encryption");
        }
    }

    public static void digitalCertificate(String taint, boolean leak) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);

            KeyPair key = keyGen.generateKeyPair();

            Signature sig = Signature.getInstance("MD5WithRSA");
            sig.initSign(key.getPrivate());
            sig.update(taint.getBytes());
            byte[] signature = sig.sign();
            if (leak)
                send(("signature " + new String(signature)).getBytes());

            sig.initVerify(key.getPublic());
            sig.update(taint.getBytes());

            if (sig.verify(signature))
                Log.i("haha", "signature verified");
        } catch (Exception e) {
            Log.e("wtx", "exception in digital signature");
        }
    }

    private static void send(final byte[] data) {
        Log.i("send", "sending " + new String(data));
        new Thread() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    String name = "8.8.8.8";
                    InetAddress address = InetAddress.getByName(name);
                    DatagramPacket packet = new DatagramPacket(data, data.length,
                            address, 4445);
                    socket.send(packet);
                    socket.close();
                    Log.i("haha", "@ sent to " + name);
                } catch (Exception e) {
                    Log.e("wtx", "exception when sending");
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
