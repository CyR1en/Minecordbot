package us.cyrien.minecordbot.AccountSync.Authentication;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.AccountSync.exceptions.IIllegalAuthTokenFormatException;
import us.cyrien.minecordbot.AccountSync.exceptions.IllegalConfirmKeyException;
import us.cyrien.minecordbot.AccountSync.exceptions.IllegalConfirmRequesterException;
import us.cyrien.minecordbot.AccountSync.exceptions.IllegalConfirmSessionIDException;

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
    private final String key = RandomStringUtils.randomAlphanumeric(32);
    private final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

    public AuthToken(Player mcAcc, String authID) {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        this.mcAcc = mcAcc;
        this.authID = authID;
        token = generateToken();
    }

    public AuthToken(String token) throws IIllegalAuthTokenFormatException {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        encrypted = false;
        this.token = token;
        AuthToken sudoToken = new AuthToken(null, null);
        sudoToken.setToken(token);
        //this.decryptToken(sudoToken);
        String[] splitToken = sudoToken.getToken().toString().split("\\.");
        if(splitToken.length == 3) {
            mcAcc = Bukkit.getPlayer(UUID.fromString(splitToken[0]));
            authID = splitToken[2];
        } else {
            throw new IIllegalAuthTokenFormatException();
        }
    }

    private String generateToken() {
        token = mcAcc != null ? mcAcc.getUniqueId().toString() + "." + RandomStringUtils.randomAlphanumeric(32) + "." + authID :
                UUID.randomUUID().toString() + "." + RandomStringUtils.randomAlphanumeric(32) + "." + authID;
        //encryptToken(this);
        return token;
    }

    private AuthToken encryptToken(AuthToken token) {
        if (!token.isEncrypted())
            try {
                token.getCipher().init(Cipher.ENCRYPT_MODE, aesKey);
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
                token.getCipher().init(Cipher.DECRYPT_MODE, aesKey);
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

    public boolean authenticateToken(AuthToken token) throws IllegalConfirmRequesterException, IllegalConfirmKeyException, IllegalConfirmSessionIDException {
        if (!compareToken(token))
            return false;
        if (token.isEncrypted())
            token.decryptToken(token);
        if (this.isEncrypted())
            this.decryptToken(this);
        String[] token1 = token.toString().split("\\.");
        String[] token0 = this.toString().split("\\.");
        if(!token0[0].equals(token1[0])) {
            throw new IllegalConfirmRequesterException();
        } else if (!token0[1].equals(token1[1])) {
            throw new IllegalConfirmKeyException();
        } else if (!token0[2].equals(token1[2])) {
            throw new IllegalConfirmSessionIDException();
        }
        return true;
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

    public Player getMcAcc() {
        return mcAcc;
    }

    public String getAuthID() {
        return authID;
    }

    public Cipher getCipher() {
        return cipher;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof AuthToken || ((AuthToken) o).getToken() == this.getToken())
            return 1;
        return -1;
    }
}
