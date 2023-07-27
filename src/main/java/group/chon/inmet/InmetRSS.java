package group.chon.inmet;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Instant;

public class InmetRSS {
    private InmetArrayAlerts inmetArrayAlerts = new InmetArrayAlerts();
    //private String outputFilePath = "inmetRSS.xml";
    private String url;
    public InmetRSS(String rssURL){
        this.url = rssURL;
        this.read();
    }

    public InmetRSS(){

    }

    private void read(){
        try {
            this.downloadRSS(url, "inmetRSS.xml");
            this.lerXML("inmetRSS.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getHasNewItem(){
        return inmetArrayAlerts.getHasNewItem();
    }

    public InmetAlert getLastUnperceivedAlert(){
        return inmetArrayAlerts.getLastUnperceivedAlert();
    }

    public Integer getAlertID(){
        return 0;
    }
    public static void main(String[] args) {
        InmetRSS inmetRSS = new InmetRSS("https://apiprevmet3.inmet.gov.br/avisos/rss");
        InmetAlert inmetAlert = null;
        while(inmetRSS.getHasNewItem()){
            inmetAlert = inmetRSS.getLastUnperceivedAlert();
            System.out.print(inmetAlert.getId()+" ");
            System.out.println(inmetAlert.getDescription());
        }

    }

    private void downloadRSS(String url, String outputFilePath) throws IOException {
        Path diretorioPath = Paths.get(".rss");
        //outputFilePath = ".rss/"+outputFilePath;
        if (!(Files.exists(diretorioPath) && Files.isDirectory(diretorioPath))) {
            Files.createDirectory(diretorioPath);
        }

        if(isFileMoreOldOrNotExists(".rss/"+outputFilePath,5)) {
            System.out.println("[Inmet] Downloading... "+outputFilePath);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent();
                         OutputStream outputStream = new FileOutputStream(".rss/"+outputFilePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void lerXML(String xmlFilePath) {
        xmlFilePath = ".rss/"+xmlFilePath;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            // Obtendo a lista de elementos <item>
            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);

                // Obtem o link do alerta e captura o ID
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                String[] linkSeg = link.split("/");
                Integer alertID = Integer.parseInt(linkSeg[linkSeg.length - 1]);

                // Se Alerta não existir, realiza o cadastro
                if(!inmetArrayAlerts.alertExists(alertID)){
                    InmetAlert alert = new InmetAlert(alertID,
                            item.getElementsByTagName("title").item(0).getTextContent(),
                            link);

                    String alertPathFile = ".rss/"+alertID+".xml";

                    //Baixando informações do Alerta
                    File file = new File(alertPathFile);
                    if (!file.exists()) {
                        downloadRSS(link,alertID+".xml");
                    }
                    DocumentBuilderFactory alertFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder alertBuilder = alertFactory.newDocumentBuilder();
                    Document alertDoc = alertBuilder.parse(new File(alertPathFile));
                    Element info = (Element) alertDoc.getElementsByTagName("info").item(0);

                    alert.setCategory(info.getElementsByTagName("category").item(0).getTextContent());
                    alert.setEvent(info.getElementsByTagName("event").item(0).getTextContent());
                    alert.setResponseType(info.getElementsByTagName("responseType").item(0).getTextContent());
                    alert.setUrgency(info.getElementsByTagName("urgency").item(0).getTextContent());
                    alert.setSeverity(info.getElementsByTagName("severity").item(0).getTextContent());
                    alert.setCertainty(info.getElementsByTagName("certainty").item(0).getTextContent());
                    alert.setSenderName(info.getElementsByTagName("senderName").item(0).getTextContent());
                    alert.setDescription(info.getElementsByTagName("description").item(0).getTextContent());
                    alert.setInstruction(info.getElementsByTagName("instruction").item(0).getTextContent());
                    alert.setWeb(info.getElementsByTagName("web").item(0).getTextContent());

                    NodeList alertParameters = alertDoc.getElementsByTagName("parameter");
                    for (int j = 0; j < alertParameters.getLength(); j++) {
                        Element parameter = (Element) alertParameters.item(j);
                        String valueName = parameter.getElementsByTagName("valueName").item(0).getTextContent();
                        String value = parameter.getElementsByTagName("value").item(0).getTextContent();
                        if (valueName.equals("ColorRisk")){
                            alert.setColorRisk(value);
                        }else if(valueName.equals("TimeStampDateOnSet")){
                            alert.setTimeStampDateOnSet(Long.parseLong(value));
                        }else if(valueName.equals("TimeStampDateExpires")){
                            alert.setTimeStampDateExpires(Long.parseLong(value));
                        }else if(parameter.getElementsByTagName("valueName").item(0).getTextContent().equals("Municipios")){
                            ArrayList<IBGEMunicipio> ibgeMunicipios = new ArrayList<>();

                            String municipios = parameter.getElementsByTagName("value").item(0).getTextContent();
                            Pattern pattern = Pattern.compile("\\((\\d+)\\)");
                            Matcher matcher = pattern.matcher(municipios);

                            while (matcher.find()) {
                                Integer codigo = Integer.parseInt(matcher.group(1));
                                IBGEMunicipio codIBGE = new IBGEMunicipio(codigo);
                                ibgeMunicipios.add(codIBGE);
                            }
                            alert.setIbgeMunicipios(ibgeMunicipios);
                        }
                    }
                    inmetArrayAlerts.addItem(alert);
                }else{
                    System.out.print(".");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean isRightNow(Long timeStampDateOnSet, Long timeStampDateExpires) {
        long currentTimestamp = Instant.now().getEpochSecond();
        if(timeStampDateExpires>currentTimestamp){
            if(timeStampDateOnSet<currentTimestamp){
                return true;
            }
        }
        return false;
    }

    public Boolean isFuture(Long timeStampDateOnSet, Long timeStampDateExpires) {
        long currentTimestamp = Instant.now().getEpochSecond();
        if(timeStampDateExpires>currentTimestamp){
            if(timeStampDateOnSet>currentTimestamp){
                return true;
            }
        }
        return false;
    }

   private Boolean isFileMoreOldOrNotExists(String filePath, Integer oldInMinutes) {
    File file = new File(filePath);
    if (file.exists()) {
        long ultimaModificacao = file.lastModified();
        long tempoAtual = System.currentTimeMillis();

        long diferencaEmMilissegundos = tempoAtual - ultimaModificacao;
        long cincoMinutosEmMilissegundos = oldInMinutes * 60 * 1000; // 5 minutos em milissegundos

        if (diferencaEmMilissegundos > cincoMinutosEmMilissegundos) {
            return true;
        } else {
            return false;
        }
    }
    return true;
    }

    public void cleanCache(String strOpt){
        File diretorio = new File(strOpt);
        apagarDiretorioRecursivamente(diretorio);
    }

    private void apagarDiretorioRecursivamente(File diretorio) {
        if (diretorio.isDirectory()) {
            File[] conteudo = diretorio.listFiles();
            if (conteudo != null) {
                for (File arquivo : conteudo) {
                    apagarDiretorioRecursivamente(arquivo);
                }
            }
        }
        diretorio.delete();
    }
}
