package com.mohit.azureKeyVault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;

import com.microsoft.azure.keyvault.models.SecretBundle;

//for jceks
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.alias.CredentialProvider;
import org.apache.hadoop.security.alias.CredentialProviderFactory;
import java.io.IOException;

public class AzureKeyVaultDemo {

  public static void main(String[] args) {

    System.out.println("args : " + args[0] + "\n" + args[1] + "\n" + args[2] + "\n");
    String clientId = "";//args[0]; 
    String clientKey = "";//args[1];  
    String vaultURI = args[2];

    HashMap<String, String> keymap = new HashMap<String, String>();
    try {
      keymap = getKeysFromJcek("clientId,clientCredential");
      System.out.println("jcek keyPasses = " + keymap);
    } catch (Exception e) {
      System.out.println("jcek Exception : " + e);
    }

    clientId = keymap.get("clientId");
    clientKey = keymap.get("clientCredential");

    /* Get keyVault client by providing authorized credentials as read from jceks */
    KeyVaultClient client = new KeyVaultClient(new ClientSecretKeyVaultCredential(clientId, clientKey));
    //System.out.println(client);
    
    //Keys to be extracted from KeyVault
    ArrayList<String> keys = new ArrayList<String>();
    keys.add("endpointUrl");
    keys.add("clientId");
    keys.add("clientCredential");
    for (String key : keys) {
      SecretBundle secret = client.getSecret(vaultURI, key);
      System.out.println("===> " + key + " : " + secret.value());
    }
  }

  public static HashMap<String, String> getKeysFromJcek(String csvKeys) throws IOException {

    HashMap<String, String> keyPasses = new HashMap<String, String>();
    String[] keys = csvKeys.split(",");
    System.out.println(Arrays.toString(keys));

    System.out.println("Fetch password from configured credential provider path");
    Configuration c = new Configuration();
    c.set(CredentialProviderFactory.CREDENTIAL_PROVIDER_PATH, "localjceks://file/Users/mohit.gupta/Downloads/azure.jceks");
    CredentialProvider credentialProvider = CredentialProviderFactory.getProviders(c).get(0);
    for(String key : keys) {
      CredentialProvider.CredentialEntry entry = credentialProvider.getCredentialEntry(key);
      if (entry == null) {
        throw new IOException(String.format("No credential entry found for %s", key));
      } else {
        keyPasses.put(key, String.valueOf(entry.getCredential()));
        //System.out.println(String.valueOf(entry.getCredential());
      }
    }
    return keyPasses;
  }
}

