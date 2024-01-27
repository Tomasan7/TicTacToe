<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Protocol Debugger</title>
</head>
<body>

<#-- TODO: Also use the kotlin frontend for this page -->

<h1>Protocol Debugger</h1>

<div>
    <style>
        .packet-forms-container
        {
            display: flex;
            flex-direction: row;
            gap: 1rem;
            flex-wrap: wrap;
        }

        .packet-form
        {
            display: flex;
            flex-direction: column;
            gap: .5rem;
            border-style: solid;
            padding: 10px;
        }

        .field-container
        {
            display: flex;
            flex-direction: row;
            gap: .5rem;
        }

        .packet-form input, .packet-form button, .packet-form label
        {
            width: 100px;
        }
    </style>
    <h2>Send Packet</h2>
    <div class="packet-forms-container">
        <#list packets as packet>
            <form id="packet-${packet.id}" class="packet-form">
                <h3>${packet.name}</h3>
                <#list packet.fields as field>
                    <div class="field-container">
                        <label for="${packet.id}-${field.name}">${field.name}:</label>
                        <input class="field" type="text" id="${packet.id}-${field.name}" name="${field.name}"
                               placeholder="${field.type}">
                    </div>
                </#list>
                <button type="button" onclick="sendPacket(${packet.id})">Send</button>
            </form>
        </#list>
    </div>
</div>

<div>
    <h2>Incoming Packets</h2>
    <ul id="incomingPackets"></ul>
</div>

<script>
    const socket = new WebSocket("ws://localhost/ws");

    socket.onmessage = function (event)
    {
        displayIncomingPacket(event.data);
    };

    function sendPacket(packetId)
    {
        const packetForm = document.getElementById("packet-" + packetId);
        const fieldInputs = packetForm.getElementsByClassName("field");
        const packetData = {};

        for (let fieldInput of fieldInputs)
            packetData[fieldInput.name] = fieldInput.value;

        const packetDataJson = JSON.stringify(packetData);

        let frame = `${r"${packetId}"}=${r"${packetDataJson}"}`

        console.log(frame)

        socket.send(frame);
    }

    function displayIncomingPacket(packet)
    {
        const incomingPacketsList = document.getElementById("incomingPackets");
        const listItem = document.createElement("li");
        listItem.textContent = packet;
        incomingPacketsList.appendChild(listItem);
    }
</script>

</body>
</html>
