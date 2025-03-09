import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';
import '../Style/PlatformStyle.css';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import { useLocation, useNavigate } from 'react-router-dom';
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";



export default function Platform() {
  const [userDetails, setUserDetails] = useState({});
  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [isSimulating, setIsSimulating] = useState(false);
  const [measurements, setMeasurements] = useState([]); // Adăugăm stare pentru măsurători

  const [messages,setMessages] = useState([]);
  const [message,setMessage] = useState("");
  const [typing,setTyping] = useState(false);
  const [stompClient, setStompClient] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();
  const user = location.state.user;
  const userId = user.id;
  const userData2 = null;
    


  useEffect(() => {
    if (userId) {
      // Obține utilizatorul cu dispozitivele asociate
      axios
        .post(`${process.env.REACT_APP_BACKEND1_URL}/User/GetUserById`, userId, {
          headers: {
            "Authorization": `Bearer ${localStorage.getItem("jwtToken")}`, // Token JWT
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          const userData = response.data;
         // userData2 = userData;
          setUserDetails(userData); // Setează detaliile utilizatorului
          setDevices(userData.devices); // Setează dispozitivele asociate
        })
        .catch((error) => {
          console.error('Eroare la obținerea datelor utilizatorului:', error);
        });
    }
  }, [userId]);

  useEffect(() => {
    if (userId) {
      console.log("Backend URL:", process.env.REACT_APP_BACKEND5_URL);

      const socket = new SockJS(`${process.env.REACT_APP_BACKEND5_URL}/ws`);
      console.log("sock+"+socket);
      const stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log(str),
        onConnect: () => {
          // Subscriere la mesaje
          console.log("Conectat la STOMP!");
          console.log("numeess"+user.name);
          stompClient.subscribe(`/chat/${user.name}/message`, (message) => {
            console.log("Mesaj primit:", message.body);

            const parsedMessage = JSON.parse(message.body);
            setMessages((prev) => [...prev, parsedMessage]);
          });
  
          // Subscriere la notificări "typing"
          stompClient.subscribe(`/chat/${user.name}/typing`, (notification) => {
            const parsedNotification = JSON.parse(notification.body);
            alert(`Adminul tasteaza`);

          //  console.log('Mesaj primit:',notification.body);
                setTyping(true);
          });
         
          
        },
        onStompError: (frame) => {
          console.error("Eroare STOMP:", frame);
        },
      });
  
      stompClient.activate();
      setStompClient(stompClient);
  
      // Cleanup
      return () => {
        stompClient.deactivate();
      };
    }
  }, [userId]);
  
  
  const sendMessage = () => {
    if (stompClient &&stompClient.connected && message.trim() !== "") {
      const chatMessage = {
        receiver: "Admin", // Identificator unic pentru sesiune
        sender: userDetails.name, // Nume utilizator
        content: message,
        timestamp: Date.now(),
      };
      setMessages((prevMessages) => [...prevMessages, chatMessage]);
    setMessage(""); 
      stompClient.publish({
        destination: "/chat/message/admin",
         // Destinația din backend (controller STOMP)
        body: JSON.stringify(chatMessage), // Mesajul trimis în format JSON
      });
      console.log("Message sent:", chatMessage);
    } else {
      console.error("STOMP connection is not active or message is empty");
    }
  };
  
  // const handleReadMessage = (messageId) => {
  //   if (stompClient) {
  //     const readNotification = {
  //       messageId: messageId, // ID-ul mesajului citit
  //       user: activeTab, // Utilizatorul care citește mesajul
  //     };
  
  //     stompClient.send("/chat/admin/read", {}, JSON.stringify(readNotification));
  //   }
  // };
  

  const handleTyping = () => {
    if (stompClient) {
      const typingNotification = {
        sessionId: userId,
        user: user.name,
        isTyping: true,
      };
      console.log("Sending typing notification to /chat/admin/typing:", typingNotification);

      stompClient.publish({
        destination: "/chat/admin/typing",
        body: JSON.stringify(typingNotification)
      });  
    }
  };
  
  


  const handleStartSimulation = () => {
    if (!selectedDevice) return;

    axios
      .post(`${process.env.REACT_APP_BACKEND4_URL}/simulator/setDeviceId`, null, {
        params: { deviceId: selectedDevice, userId: user.id  },
      })
      .then(() => {
        setIsSimulating(true);
        console.log(`Simularea a început pentru dispozitivul: ${selectedDevice}`);
      })
      .catch((error) => {
        console.error('Eroare la pornirea simulării:', error);
      });
  };
  const handleStopSimulation = () => {
    if (!user.id) return; // Verifică dacă există un ID de utilizator
  
    axios
      .post(`${process.env.REACT_APP_BACKEND4_URL}/simulator/stop`, null, {
        params: { userId: user.id },
      })
      .then(() => {
        setIsSimulating(false);
        console.log(`Simularea a fost oprită pentru utilizatorul: ${user.id}`);
      })
      .catch((error) => {
        console.error('Eroare la oprirea simulării:', error);
      });
  };
  
  const handleLogout = () => {
    navigate('/Login');
    localStorage.removeItem("jwtToken");

  };

  // Funcția pentru a obține măsurătorile
  const fetchMeasurements = () => {
    if (!selectedDevice) return; // Verificăm dacă un dispozitiv este selectat

    axios
      .get(`${process.env.REACT_APP_BACKEND4_URL}/simulator/getMeasurements`, {
        params: { userId: user.id },
      })
      .then((response) => {
        setMeasurements(response.data); // Setează măsurătorile în stare
        console.log('Măsurători obținute:', response.data);
      })
      .catch((error) => {
        console.error('Eroare la obținerea măsurătorilor:', error);
      });
  };
  return (
    <div className="platform-container">
      <div className="buttons">
        <Button variant="contained" onClick={handleLogout}>
          Deconectare
        </Button>
      </div>

      <h3 style={{ margin: '10px 20px 30px 5px', fontSize: '2rem' }}>
        Bun venit, {user.name}
      </h3>
      <h2>Dispozitive asociate:</h2>

      {/* Afișarea dispozitivelor într-un tabel */}
      <TableContainer component={Paper}>
        <Table aria-label="device table">
          <TableHead>
            <TableRow>
              <TableCell>Selecție</TableCell>
              <TableCell>ID Dispozitiv</TableCell>
              <TableCell align="right">Descriere</TableCell>
              <TableCell align="right">Adresă</TableCell>
              <TableCell align="right">Consum</TableCell>
              <TableCell align="right">Consum Maxim</TableCell>

            </TableRow>
          </TableHead>
          <TableBody>
            {devices &&
              devices.map((device) => (
                <TableRow key={device.id}>
                  <TableCell>
                    <RadioGroup
                      value={selectedDevice}
                      onChange={() => setSelectedDevice(device.id)}
                    >
                      <FormControlLabel
                        value={device.id}
                        control={<Radio />}
                        label=""
                      />
                    </RadioGroup>
                  </TableCell>
                  <TableCell component="th" scope="row">
                    {device.id}
                  </TableCell>
                  <TableCell align="right">{device.description}</TableCell>
                  <TableCell align="right">{device.address}</TableCell>
                  <TableCell align="right">{device.consumption}</TableCell>
                  <TableCell align="right">{device.maxHourlyConsumption}</TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Butoane pentru simulare */}
      <div style={{ marginTop: '20px' }}>
        <Button
          variant="contained"
          color="primary"
          onClick={handleStartSimulation}
          disabled={!selectedDevice || isSimulating}
        >
          Start Simulare
        </Button>
        <Button
          variant="contained"
          color="secondary"
          onClick={handleStopSimulation}
          disabled={!isSimulating}
          style={{ marginLeft: '10px' }}
        >
          Stop Simulare
        </Button>
        <Button
          variant="contained"
          color="success"
          onClick={fetchMeasurements}
          disabled={!selectedDevice}
          style={{ marginLeft: '10px' }}
        >
          Obține Măsurători
        </Button>
      </div>

      {/* Afișează măsurătorile */}
      {measurements.length > 0 && (
        <div style={{ marginTop: '20px' }}>
          <h3>Măsurători:</h3>
          <TableContainer component={Paper}>
            <Table aria-label="measurements table">
              <TableHead>
                <TableRow>
                  <TableCell>Timp</TableCell>
                  <TableCell>Valoare</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {measurements.map((measurement, index) => (
                  <TableRow key={index}>
                    <TableCell>{measurement.timestamp}</TableCell>
                    <TableCell>{measurement.measurementValue}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      )}
      <div className="chat-container">
      <h3>Chat cu Admin</h3>
      <div className="chat-window">
        {/* Afișare mesaje */}
        {messages.map((msg, index) => (
          <div key={index}>
            <b>{msg.sender}</b>: {msg.content}
          </div>
        ))}
        {/* Indicator pentru "Typing" */}
        {typing && (
          <div className="typing-indicator">
            <i>{typing}</i>
          </div>
        )}
      </div>
      <div className="input-container">
        {/* Input pentru mesaj */}
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyUp={handleTyping}
          placeholder="Scrie un mesaj..."
        />
        <button onClick={sendMessage}>Trimite</button>
      </div>
    </div>

    </div>
  );
}
