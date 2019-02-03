# AzureKeyVaultClient

It is a Java Application to fetch credentials saved in Azure's KeyVault.


## What is it about?

Its a simple java application to fetch credentials stored in Azure's KeyVault which is a credential storage provided by
Microsoft for its cloud offering - Azure. The credentials can be used to store username, passowrds, app secrets,
servcie pronciples for access to other Azure services, etc.

While there are different ways to store this information in KeyVault like keys, secrets, certificates, etc but here we explore
method of fetching secrets which are essentially key/value pairs where key could be a string to refer to the credential
saved with keyVault. `key` is what you will distribute to other applications in their configs or even code, but `value` 
is what you would like the applciation to fetch by itself from keyVault given the `key`.

The purpose for writing this applciation was to fetch these details for a java app that needed to access ADLS. As the app could
not ask users to specify credentials for accessing ADLS in its config and the same app instance needed to be used by
multiple users who may obtain their Service principle to access ADLS at different times (before or after the app is
deployed), I tried an attempt to identify how these credentials can be fetched from KeyVault in general.

As a result, the application can be deployed once and the users may come and go at any time with their keys to fetch
corresponding service principles from `KeyVault` to acess their authorised ADLS data without requiring to restart the
application.


## How it works?

To access `KeyVault`, this applciation would also need to be authorised the access to `KeyVault`. 
The trick used for this applcaition is that it is given an access to `KeyVault` from Azure portal and Service
Princple(SP) generated by Azure for this app are itself provided in a jceks file on a node on which this applciation
would run. If the app would run as a spark app, then jceks path can be changed to include `hdfs` instead of `file` and
same code should work just fine (ofcourse assuming some one copied the required jceks file in HDFS before launching this
app).
Once `jceks` is read to fetch SP details for the ADLS, the app can be extended to access data from/to ADLS.

*NOTE* `jceks` file can be generated using `hadoop credentials` utility from command shell from any node of a hadoop
cluster. One can specify SP attributes as a key/value pair to generate this file using above utility and the resultant
file is an encrypted binary file that can be read using `Hadoop Credentials API`.
This app only demonstrates till fetching keys from `KeyVault`. ADLS is used just an example to describe possible
usecases for this app.

## How to build?

```
mvn clean packages
```

## How to run?
```
java -cp target/AzureKeyVaultDemo.jar com.mohit.azureKeyVault.AzureKeyVaultDemo <KeyVaultURLToAccess>
```



