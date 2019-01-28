package com.mohit.azureKeyVault;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.microsoft.azure.keyvault.KeyVaultClient;
//import com.microsoft.azure.keyvault.KeyVaultClientService;
//import com.microsoft.azure.keyvault.KeyVaultConfiguration;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.webkey.JsonWebKeyEncryptionAlgorithm;

import com.microsoft.azure.keyvault.models.SecretBundle;

public class AzureKeyVaultDemo {

	public static void main(String[] args)
			throws InterruptedException, ExecutionException, URISyntaxException, UnsupportedEncodingException {
		
    String clientId = args[0];  
    String clientKey = args[1];  
    String vaultURI = args[2]; 

    // ClientSecretKeyVaultCredential is the implementation of KeyVaultCredentials
    KeyVaultClient client = new KeyVaultClient(new ClientSecretKeyVaultCredential(clientId, clientKey));
		System.out.println(client);

		System.out.println("calling client.getSecret");
    SecretBundle secret = client.getSecret(vaultURI, "clientId" );
		System.out.println("after client.getSecret");
    System.out.println(secret.value());
	}
}

