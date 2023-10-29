//----------------------------------------------------------------------------
// Copyright (C) 2003  Rafael H. Bordini, Jomi F. Hubner, et al.
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://www.inf.ufrgs.br/~bordini
// http://www.das.ufsc.br/~jomi
//
//----------------------------------------------------------------------------

package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class create_mas extends DefaultInternalAction {
   private ArrayList<Literal> listOfBelieves = new ArrayList<>();
   private ArrayList<Plan> listOfPlans = new ArrayList<>();
   private ArrayList<String> listOfIntentions = new ArrayList<>();

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        String randomDir = null;
        if(args.length==2){
            randomDir = createRandomDir();
            descompactarZip(args[0].toString().replace("\"", ""),randomDir);
            runNewMAS(randomDir,args[1].toString());
            return true;
        }else if(args.length==3){
            try{
                randomDir = createRandomDir();
                descompactarZip(args[0].toString().replace("\"", ""),randomDir);

                if(args[2].isList()) {
                    if(!processList(args[2])){
                        return false;
                    }
                }else{
                    if(!processTerm(args[2])){
                        return false;
                    }
                }

                if(!addOnFly(randomDir)){
                    return false;
                }

                runNewMAS(randomDir,args[1].toString());
                return true;

            }catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }else{
            return false;
        }


    }

    private boolean runNewMAS(String path, String outputType){
        ProcessBuilder processoBuilder = new ProcessBuilder(
                "java",
                "-jar",
                absoluteFrameworkLocation(),
                encontrarArquivoMas2j(path),
                "-"+outputType);
        try {
            if(outputType.equals("console")){
                String logFile = path+File.separator+"out.log";
                System.out.println("[JasonEmbedded] the logFile is available at "+logFile);
                File arquivoDeLog = new File(logFile);
                arquivoDeLog.createNewFile();
                processoBuilder.redirectOutput(arquivoDeLog);

                String errorFile = path+File.separator+"agents.log";
                System.out.println("[JasonEmbedded] the agentsLog is available at "+errorFile);
                File arquivoDeErro = new File(errorFile);
                arquivoDeErro.createNewFile();
                processoBuilder.redirectError(arquivoDeErro);
            }
            processoBuilder.directory(new File(path));
            processoBuilder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean processTerm(Term arg){
        try{
            if(arg.isLiteral()){
                listOfBelieves.add(ASSyntax.parseLiteral(arg.toString()));
            }else if(arg.isString()){
                try{
                    listOfPlans.add(ASSyntax.parsePlan(removerAspas(arg.toString())));
                }catch (Exception ex){
                    listOfIntentions.add(removerAspas(arg.toString()));
                }
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private boolean processList(Term arg){
        try {
            ListTerm t = ASSyntax.parseList(arg.toString());
            for (int i = 0; i < t.size(); i++) {
                if(!processTerm(t.get(i))){
                    return false;
                }
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    private String absoluteFrameworkLocation() {
        try {
            URL jarUrl = create_mas.class.getProtectionDomain().getCodeSource().getLocation();
            return new File(jarUrl.toURI()).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createRandomDir() {
        try {
            String diretorioTemporario = System.getProperty("java.io.tmpdir");
            String nomeDiretorio = "mas_" + System.currentTimeMillis();
            String caminhoDiretorioTemporario = diretorioTemporario + System.getProperty("file.separator") + nomeDiretorio;
            Path diretorioTemp = Files.createDirectory(Path.of(caminhoDiretorioTemporario));
            return caminhoDiretorioTemporario;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean descompactarZip(String arquivoZip, String diretorioDestino) {
        byte[] buffer = new byte[1024];

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(arquivoZip))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                String nomeArquivo = zipEntry.getName();
                File arquivoDescompactado = new File(diretorioDestino, nomeArquivo);

                if (zipEntry.isDirectory()) {
                    arquivoDescompactado.mkdirs();
                } else {
                    arquivoDescompactado.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(arquivoDescompactado)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }

                zipEntry = zis.getNextEntry();
            }
            System.out.println("[JasonEmbedded] new MAS unzipped in "+diretorioDestino);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean addOnFly(String randomDir) {
        File arquivo = new File(randomDir, "_onFly.asl");
        try {
            FileWriter writer = new FileWriter(arquivo);
            for(Literal literal: listOfBelieves){
                writer.write(literal.toString()+". \n");
            }
            for(String intention:listOfIntentions ){
                writer.write(intention+". \n");
            }
            for(Plan plan: listOfPlans){
                writer.write(plan.toString()+"\n");
            }
            writer.close();
            System.out.println("[JasonEmbedded] new Beliefs and Plans included in "+randomDir+"/_onFly.asl");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String encontrarArquivoMas2j(String diretorio) {
        File pasta = new File(diretorio);

        if (pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String nome) {
                    return nome.toLowerCase().endsWith(".mas2j");
                }
            });

            if (arquivos != null && arquivos.length > 0) {
                return arquivos[0].getName();
            }
        }

        return null; // Retorna null se nenhum arquivo .mas2j for encontrado
    }

    public static String removerAspas(String texto) {
        if (texto != null && texto.length() >= 2) {
            if (texto.startsWith("\"") && texto.endsWith("\"")) {
                texto = texto.substring(1, texto.length() - 1);
            }
        }
        return texto.replace("\\\"","\"");
    }
}