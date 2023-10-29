// Agent inmetAgent in project inmetAlert
/* Initial beliefs and rules */
// Provided by Alert-AS - Centro Virtual para Avisos de Eventos Meteorológicos Severos --> https://alertas2.inmet.gov.br/
inmetAlertAS("https://apiprevmet3.inmet.gov.br/avisos/rss").

myCity(3303302).
// Niteroi-RJ -- Provided by IBGE - Instituto Brasileiro de Geografia e Estatística --> https://www.ibge.gov.br/explica/codigos-dos-municipios.php

/* Initial goals */
!start.

/* Plans */
+!start
: inmetAlertAS(URL) 
& myCity(COD) <- 
 // .inmetGovBrClear;
  .inmetGovBrCheck(URL,COD);
  .randomUUID(MyUUID);
  .connectCN("skynet.chon.group",5500,MyUUID);
  .randomUUID(R);
  .create_mas("extra/newMAS.zip",console,
                [adamUUID(R),skynet("skynet.chon.group",5500),
                "!tell","+!tell:adamUUID(MyUUID) & skynet(SRV,Port) <- .connectCN(SRV,Port,MyUUID); .wait(10000); .stopMAS.",
                "+helloAdam[source(X)] <- .print(\"I am listen: \",X); .sendOut(X,tell,listen); .wait(2000); .stopMAS."]);
  .wait(3000);
  .print("Calling Adam");
  .sendOut(R,tell,helloAdam).

+listen[source(X)] <- .print("Adam is alive in ",X); .stopMAS.

+inmetAlert(AlertID,Event,Serverity,Certainty,Time,ResponseType,Description,Instruction,Link)[source(inmetGovBR)] <-
    .print("New Alert: ",AlertID,
    "\n Description: ",Description,
    "\n Event : ",Event, 
    "\n Serverity: ",Serverity,
    "\n Certainty: ",Certainty,
    "\n When: ",Time,
    "\n Type: ",ResponseType,
    "\n What to do: ",Instruction,
    "\n More info: ",Link,
    "\n").
