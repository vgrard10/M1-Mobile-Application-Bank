package com.example.mybankactivity;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore cle;
    private KeyGenerator cleGenerateur;
    private Intent acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        acc = new Intent(this, Compte.class);
        Button accountPage = findViewById(R.id.buttonAccount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyguardManager cleManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                try {
                    generateKey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if (initCipher()) { //there is bug with the initCipher of my application
                if(1==1){
                    accountPage.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                startActivity(acc);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Please authenticate to access to your accounts", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }

    private void generateKey() {
        try {
            cle = KeyStore.getInstance("AndroidKeyStore");
            cleGenerateur = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            cle.load(null);
            cleGenerateur.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            cleGenerateur.generateKey();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException exc) {
            exc.printStackTrace();
        }
    }

    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed", e);
        }
        try {
            cle.load(null);
            SecretKey key = (SecretKey) cle.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}
