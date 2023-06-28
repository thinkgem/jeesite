package org.jasig.cas.client.proxy;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Provides encryption capabilities. Not entirely safe to configure since we have no way of controlling the
 * key and cipher being set.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.2.0
 */
public abstract class AbstractEncryptedProxyGrantingTicketStorageImpl implements ProxyGrantingTicketStorage {

    public static final String DEFAULT_ENCRYPTION_ALGORITHM = "DESede";

    private Key key;

    private String cipherAlgorithm = DEFAULT_ENCRYPTION_ALGORITHM;

    public final void setSecretKey(final String key) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        this.key = SecretKeyFactory.getInstance(this.cipherAlgorithm).generateSecret(new DESedeKeySpec(key.getBytes()));
    }

    public final void setSecretKey(final Key key) {
        this.key = key;
    }

    /**
     * Note: you MUST call this method before calling setSecretKey if you're not using the default algorithm.  You've been warned.
     *
     * @param cipherAlgorithm the cipher algorithm.
     */
    public final void setCipherAlgorithm(final String cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
    }

    public final void save(final String proxyGrantingTicketIou, final String proxyGrantingTicket) {
        saveInternal(proxyGrantingTicketIou, encrypt(proxyGrantingTicket));
    }

    public final String retrieve(final String proxyGrantingTicketIou) {
        return decrypt(retrieveInternal(proxyGrantingTicketIou));
    }

    protected abstract void saveInternal(String proxyGrantingTicketIou, String proxyGrantingTicket);

    protected abstract String retrieveInternal(String proxyGrantingTicketIou);

    private String encrypt(final String value) {
        if (this.key == null) {
            return value;
        }

        if (value == null) {
            return null;
        }

        try {
            final Cipher cipher = Cipher.getInstance(this.cipherAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, this.key);
            return new String(cipher.doFinal(value.getBytes()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decrypt(final String value) {
        if (this.key == null) {
            return value;
        }

        if (value == null) {
            return null;
        }

        try {
            final Cipher cipher = Cipher.getInstance(this.cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, this.key);
            return new String(cipher.doFinal(value.getBytes()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
