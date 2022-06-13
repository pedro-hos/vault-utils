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

    private static final String DEFAULT_SECRET_KEY = "somearbitrarycrazystringthatdoesnotmatter";
    private static final String DEFAULT_ITERATION_CODE = "65536";
    private static final String DEFAULT_SALT = "1234abcd";

    @Inject
    CommandLine.IFactory factory; 
    
    @Option(names = {"-s", "--salt"}, defaultValue = DEFAULT_SALT, description = "(optional) 8 character salt")
    String salt;
    
    @Option(names = {"-i", "--iteration"}, defaultValue = DEFAULT_ITERATION_CODE, description = "(optional) Iteration count")
    int iterationCount;
    
    @Option(names = {"-sk", "--secrect-key"}, defaultValue = DEFAULT_SECRET_KEY, description = "(optional) Secret Key")
    String secretKey;
    
    @Option(names = {"-p", "--keystore-password"}, description = "(mandatory) Keystore password", required = true)
    String keystorePassword;

    @Override
    public void run() {
        String encrypted = EncryptionUtil.encrypt(keystorePassword, secretKey, salt, iterationCount);
        
        System.out.println("\n");
        System.out.println("##################################################################################");
        System.out.println("Please add the following paramenters on your application.properties file, and replace the <key> value!");
        System.out.println("quarkus.file.vault.provider.<key>.encrypted=true");
        
        if(!DEFAULT_SALT.equals(salt)) {
            System.out.println("quarkus.file.vault.provider.<key>.salt=" + salt);
        }
        
        if(!DEFAULT_SECRET_KEY.equals(secretKey)) {
            System.out.println("quarkus.file.vault.provider.<key>.secretKey=" + secretKey);
        }
        
        if(Integer.parseInt(DEFAULT_ITERATION_CODE) != iterationCount) {
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
