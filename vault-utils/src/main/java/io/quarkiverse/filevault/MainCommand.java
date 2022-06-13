package io.quarkiverse.filevault;

import javax.inject.Inject;

import io.quarkiverse.filevault.util.EncryptionUtil;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@QuarkusMain
@Command(name = "Encrypt Secret Util", mixinStandardHelpOptions = true)
public class MainCommand implements Runnable, QuarkusApplication {

    @Inject
    CommandLine.IFactory factory; 
    
    @Option(names = {"-s", "--salt"}, defaultValue = "1234abcd", description = "8 character salt")
    String salt;
    
    @Option(names = {"-i", "--iteration"}, defaultValue = "65536", description = "Iteration count")
    int iterationCount;
    
    @Option(names = {"-sk", "--secrect-key"}, defaultValue = "somearbitrarycrazystringthatdoesnotmatter", description = "Secret Key")
    String secretKey;
    
    @Option(names = {"-p", "--keystore-password"}, description = "Keystore password", required = true)
    String keystorePassword;

    @Override
    public void run() {
        String encrypted = EncryptionUtil.encrypt(keystorePassword, secretKey, salt, iterationCount);
        
        System.out.println("\n");
        System.out.println("##################################################################################");
        System.out.println("Please add the following paramenters on your application.properties file, and replace the <key> value!");
        System.out.println("quarkus.file.vault.provider.<key>.encrypted=true");
        
        if(!"1234abcd".equals(salt)) {
            System.out.println("quarkus.file.vault.provider.<key>.salt=" + salt);
        }
        
        if(!"somearbitrarycrazystringthatdoesnotmatter".equals(secretKey)) {
            System.out.println("quarkus.file.vault.provider.<key>.secretKey=" + secretKey);
        }
        
        if(65536 != iterationCount) {
            System.out.println("quarkus.file.vault.provider.<key>.iteration-count=" + iterationCount);
        }
        
        System.out.println("quarkus.file.vault.provider.<key>.secret=" + encrypted);
        System.out.println("##################################################################################");
        System.out.println("\n");
    }

    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(this, factory).execute(args);
    }

}
