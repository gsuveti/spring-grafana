
    keytool -genkeypair -alias private -keyalg RSA -keypass private --keystore private.jks --storepass mypass

    keytool -list -rfc --keystore private.jks | openssl x509 -inform pem -pubkey
