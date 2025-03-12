package group.chon.jasonembedded;

import jason.JasonException;
import jason.infra.local.RunLocalMAS;

import java.io.*;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws JasonException, IOException {
        Logger logger = Logger.getLogger("jasonEmbedded");
        logger.info("Version 25.3.12 (Argo+Hermes+Mailer+Velluscinum)");
        File mas2jFile = new File(args[0]);
        File mas2jAbsolutePath = new File(mas2jFile.getAbsolutePath());
        File mas2jLoggingPropertiesFile = new File(mas2jAbsolutePath.getParent()+File.separator+ "logging.properties");


        InputStream inputStream = null;

        if((args.length>1) && ((args[1].equalsIgnoreCase("-gui")) || (args[1].equalsIgnoreCase("--gui")))) {
            inputStream = Main.class.getResourceAsStream("/logging.properties-gui");
        }else if((args.length>1) && ((args[1].equalsIgnoreCase("-console")) || (args[1].equalsIgnoreCase("--console")))) {
            inputStream = Main.class.getResourceAsStream("/logging.properties-console");
        }else {
            if(!mas2jLoggingPropertiesFile.exists()) {
                inputStream = Main.class.getResourceAsStream("/logging.properties-console");
            }
        }

        if(inputStream != null) {
            try (BufferedReader buffered = new BufferedReader(new InputStreamReader(inputStream));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(mas2jLoggingPropertiesFile))) {
                String line;
                while ((line = buffered.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        RunLocalMAS.main(args);
    }
}