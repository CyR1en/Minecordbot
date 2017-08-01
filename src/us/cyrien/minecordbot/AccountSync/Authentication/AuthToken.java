package us.cyrien.minecordbot.AccountSync.Authentication;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class AuthToken implements Comparable {

    private String token;
    private Player mcAcc;
    private String authID;

    private Cipher cipher;
    private boolean encrypted;
    private final String key = RandomStringUtils.randomAlphanumeric(16);
    private final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

    public AuthToken(Player mcAcc, String authID) {
        this.mcAcc = mcAcc;
        this.authID = authID;
        token = generateToken();
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public AuthToken(String token) {
        encrypted = true;
        this.token = token;
        AuthToken sudoToken = new AuthToken(null, null);
        sudoToken.setToken(token);
        this.decryptToken(sudoToken);
        String[] splitToken = sudoToken.token.split(".");
        if(splitToken.length == 3) {
            mcAcc = Bukkit.getPlayer(UUID.fromString(splitToken[0]));
            authID = splitToken[2];
        }
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private String generateToken() {
        token = mcAcc != null ? mcAcc.getUniqueId().toString() + "." + RandomStringUtils.randomAlphanumeric(16) + "." + authID :
                UUID.randomUUID().toString() + "." + RandomStringUtils.randomAlphanumeric(16) + "." + authID;
        encryptToken(this);
        return token;
    }

    public boolean authenticateToken(AuthToken token) {
        if (!compareToken(token))
            return false;
        if (token.isEncrypted())
            decryptToken(token);
        if (this.isEncrypted())
            decryptToken(this);
        String[] token1 = token.toString().split(".");
        String[] token0 = this.toString().split(".");
        return (token0[0].equals(token1[0]) && token0[2].equals(token1[2]));
    }

    private AuthToken encryptToken(AuthToken token) {
        if (!token.isEncrypted())
            try {
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                byte[] encryptedToken = cipher.doFinal(token.toString().getBytes());
                token.setEncrypted(encrypted);
                token.setToken(new String(encryptedToken));
                return token;
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        return token;
    }

    private AuthToken decryptToken(AuthToken token) {
        if (token.encrypted)
            try {
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
                String decrypted = new String(cipher.doFinal(token.toString().getBytes()));
                token.setToken(decrypted);
                token.setEncrypted(false);
                return token;
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        return token;
    }

    private void setEncrypted(boolean b) {
        encrypted = b;
    }

    private void setToken(String s) {
        token = s;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public AuthToken getToken() {
        return this;
    }

    public boolean compareToken(AuthToken token) {
        return this.compareTo(token) > 0;
    }

    public String toString() {
        return token;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof AuthToken || ((AuthToken) o).getToken() == this.getToken())
            return 1;
        return -1;
    }
}
