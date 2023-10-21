// Agent inmetAgent in project inmetAlert

/* Initial beliefs and rules */
// Provided by Alert-AS - Centro Virtual para Avisos de Eventos Meteorológicos Severos --> https://alertas2.inmet.gov.br/
inmetAlertAS("https://apiprevmet3.inmet.gov.br/avisos/rss").

myCity(0).
// myCity(3303302).
// Niteroi-RJ -- Provided by IBGE - Instituto Brasileiro de Geografia e Estatística --> https://www.ibge.gov.br/explica/codigos-dos-municipios.php

/* Initial goals */
!conf.

/* Plans */
+!conf
: inmetAlertAS(URL) 
& myCity(COD) <- 
    .inmetGovBrClear;
    .inmetGovBrCheck(URL,COD);
    .wait(3000);
    .stopMAS.


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
