package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.io.IOException;
import java.security.interfaces.XECPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.util.Optional;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.X448PrivateKeyParameters;

class BC11XDHPrivateKey
    extends BCXDHPrivateKey
    implements XECPrivateKey
{
    BC11XDHPrivateKey(AsymmetricKeyParameter privKey)
    {
        super(privKey);
    }

    BC11XDHPrivateKey(PrivateKeyInfo keyInfo)
        throws IOException
    {
        super(keyInfo);
    }

  @SuppressWarnings("Override")
  public AlgorithmParameterSpec getParams() {
        if (xdhPrivateKey instanceof X448PrivateKeyParameters)
        {
            return NamedParameterSpec.X448;
        }
        else
        {
            return NamedParameterSpec.X25519;
        }
    }

  @SuppressWarnings("Override")
  public Optional<byte[]> getScalar() {
        if (xdhPrivateKey instanceof X448PrivateKeyParameters)
        {
            return Optional.of(((X448PrivateKeyParameters)xdhPrivateKey).getEncoded());
        }
        else
        {
            return Optional.of(((X25519PrivateKeyParameters)xdhPrivateKey).getEncoded());
        }
    }
}
