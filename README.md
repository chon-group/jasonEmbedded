# jasonEmbedded


|![jasonEmbedded](https://github.com/user-attachments/assets/15f66ef3-b14d-41b5-a90d-429734f44278)|
|:--:|
|Embedded artificial intelligence in Internet of Things (IoT) devices is presented as an option to reduce connectivity dependence, allowing decision-making directly at the edge computing layer.The Multi-agent Systems (MAS) embedded into IoT devices enables, in addition to the ability to perceive and act in the environment, new characteristics like pro-activity, deliberation, and collaboration capabilities to these devices. A few new frameworks and extensions enable the construction of agent-based IoT devices. However, no framework allows constructing them with hardware control, adaptability, and fault tolerance, besides agents’ communicability and mobility.|
|This project presents an extension of the [Jason framework](https://github.com/jason-lang/jason) for developing Embedded MAS with BDI agents capable of controlling hardware, communicating, and moving between IoT devices capable of dealing with fault tolerance.|

## How to Install?
+ GNU/Linux --> [jasonEmbedded APT Package](https://github.com/chon-group/dpkg-jasonembedded)

## DESCRIPTION
|Agent|New Action|Description|
|-|-|-|
|ARGO|.argo.port(___S___)|Defines a serial communication port with an _IoT low-end device_. Where ___S___ is a literal that represents a serial port (i.e., ttyACM0).|
||.argo.limit(___N___)|Defines an interval for the cycle of environmental perception. Where ___N___ is a positive number (N>0) that represents an interval in milliseconds|
||.argo.percepts(_close_\|_open_)|Listens or not the environmental perceptions.|
||.argo.act(___O___)|Sends an order to the _IoT low-end device_ to execute. Where ___O___ is a literal that represents an order for the microcontroller to execute.|
|Hermes|.hermes.configureContextNetConnection( "___X___",___G___,___E___,___U___)|Configures an ContextNet network. Where ___X___ is a string that represents an network name; ___G___ is a literal that represents the FQDN or the network address of an gateway; ___E___ is a number that represents the network port of an gateway; ___U___ is a literal that represents the identification of the device in the network.|
||.hermes.connect("___X___")|Joins at "___X___" ContextNet network.|
||.hermes.sendOut(___D___,___f___,___M___)|Dispatches a message to another MAS. Where ___D___ is a literal that represents the identification of the recipient MAS; ___f___ is a illocutionary force (_tell \| untell \| achieve \| unachieve_); ___M___ is a literal that represents the message.|
||.hermes.moveOut(___D___,___b___,___A___)|Carries over the agents to other MAS. Where ___D___ is a literal that represents the identification of the recipient MAS; ___b___ is a bio-inspired protocol (_inquilinism \| mutualism \| predation_); ___A___ is one, all, or a set of agents (i.e., all, agent or \[agent<sub>1</sub>, agent<sub>2</sub>, agent<sub>n</sub>\])|
||.hermes.disconnect("___X___");|Disconnects at "___X___" ContextNet network.|
|Mailer|.mailer.credentials(___E___,___K___)|Set email credentials. Where ___E___ is a string that represents the email account (e.g., "_agent@example.com_"); ___K___ is a string that represents the e-mail password.|
||.mailer.eMailService(\[___I___,___p___\],\[___O___,___q___\])|Set email provider configurations. Where ___I___ is a string that represents the FQDN of the input e-mail server (e.g., "_imap.example.com_"); ___p___ is a literal that represents the input protocol (e.g., _imaps_); ___O___ is a string that represents the FQDN of the output email server(e.g., "_smtp.example.com_"); ___q___ is a literal that represents the output protocol (e.g., _smtpOverTLS_).|
||.mailer.sendEMail(___D___,___f___,___M___)|Dispatches a email message to another MAS or Human. Where ___D___ is a literal that represents the destination email recipient; ___f___ is a illocutionary force (_tell \| untell \| achieve \| unachieve_); ___M___ is a literal that represents the message.|
|Jason|.velluscinum.buildWallet(___w___)|Generates a digital wallet and returns the belief: _w(___P___,___Q___)_. Where ___P___ e ___Q___ are literals that represent the agent's key pair.|
||.velluscinum.deployNFT(___S___,___P___,___Q___,___I___,___M___,___b___)| Registers an asset and returns the belief: _b_(___A___). Where ___A___ is a literal that represents a indivisible asset; ___S___ is a literal that represents the address of a DLT node; ___P___ and ___Q___ are literals that represent the agent's key pair; ___I___ is a key-value array that represents the immutable data of an asset; ___M___ is a key-value array representing asset or transaction metadata;|
||.velluscinum.transferNFT(___S___,___P___,___Q___,___A___,___R___,___M___,___b___)| Transfers an asset and returns the belief: _b_(___T___). Where ___T___ is a literal that represents a transaction performed in the DTL; ___S___ is a literal that represents the address of a DLT node; ___P___ and ___Q___ are literals that represent the agent's key pair; ___A___ is a literal that represents a indivisible asset; ___R___ is a literal that represents the public key of a recipient agent; ___M___ is a key-value array representing asset or transaction metadata;|
||.velluscinum.deployToken(___S___,___P___,___Q___,___I___,___V___,___b___)| Creates ___V___ units from an asset, returns the belief: _b_(___C___). Where ___C___ is a literal that represents a divisible asset; ___S___ is a literal that represents the address of a DLT node; ___P___ and ___Q___ are literals that represent the agent's key pair; ___I___ is a key-value array that represents the immutable data of an asset.|
||.velluscinum.transferToken(___S___,___P___,___Q___,___C___,___R___,___V___,___b___)| Transfers ___V___ units of ___C___ and returns the belief: _b_(__T__). Where ___T___ is a literal that represents a transaction performed in the DTL; ___S___ is a literal that represents the address of a DLT node; ___P___ and ___Q___ are literals that represent the agent's key pair; ___C___ is a literal that represents a divisible asset; ___R___ is a literal that represents the public key of a recipient agent; ___V___ is a literal that represents the number of parts of a ___C___.|
||.velluscinum.stampTransaction(___S___,___P___,___Q___,___T___) | Stamps a transaction (___T___). Where ___S___ is a literal that represents the address of a DLT node; ___P___ and ___Q___ are literals that represent the agent's key pair.|
||.velluscinum.tokenBalance(___S___,___P___,___Q___,___C___,___q___)|Check the wallet ___Q___ and return the belief: _q_(___C___,___V___). Where ___C___ is a literal that represents a divisible asset; ___V___ is a literal that represents the number of parts of a ___C___; ___S___ is a literal that represents the address of a DLT node; ___P___ and ___Q___ are literals that represent the agent's key pair.|

## Publications
+ Brazilian Conference on Intelligent Systems 2023 (Full paper) - [A Spin-off Version of Jason for IoT and Embedded Multi-Agent Systems](https://www.researchgate.net/publication/374620450_A_Spin-off_Version_of_Jason_for_IoT_and_Embedded_Multi-Agent_Systems)
  
## COPYRIGHT
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />jasonEmbedded is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>. The licensor cannot revoke these freedoms as long as you follow the license terms:

* __Attribution__ — You must give __appropriate credit__ like below:

Pantoja, C.E., Jesus, V.S.d., Lazarin, N.M., Viterbo, J. (2023). A Spin-off Version of Jason for IoT and Embedded Multi-Agent Systems. In: Naldi, M.C., Bianchi, R.A.C. (eds) Intelligent Systems. BRACIS 2023. Lecture Notes in Computer Science(), vol 14195. Springer, Cham. [https://doi.org/10.1007/978-3-031-45368-7_25](https://www.researchgate.net/publication/374620450_A_Spin-off_Version_of_Jason_for_IoT_and_Embedded_Multi-Agent_Systems)

<details>
<summary> Bibtex citation format</summary>

```
@InProceedings{jasonEmbedded,
doi="10.1007/978-3-031-45368-7_25",
author="Pantoja, Carlos Eduardo
and Jesus, Vinicius Souza de
and Lazarin, Nilson Mori
and Viterbo, Jos{\'e}",
editor="Naldi, Murilo C.
and Bianchi, Reinaldo A. C.",
title="A Spin-off Version of Jason for IoT and Embedded Multi-Agent Systems",
booktitle="Intelligent Systems",
year="2023",
publisher="Springer Nature Switzerland",
address="Cham",
pages="382--396",
isbn="978-3-031-45368-7"
}



```
</details>
