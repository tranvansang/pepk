package org.bouncycastle.crypto.tls;

/**
 * @deprecated Migrate to the (D)TLS API in org.bouncycastle.tls (bctls jar).
 */
public interface TlsCredentials
{
    Certificate getCertificate();
}
